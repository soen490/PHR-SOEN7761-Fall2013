package com.augmentedsociety.myphr.external;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

import com.augmentedsociety.myphr.domain.MapperException;
import com.augmentedsociety.myphr.domain.ReadingSource;
import com.augmentedsociety.myphr.domain.TemperatureMapper;
import com.augmentedsociety.myphr.domain.TemperatureReading;

/**
* Input Mapper for Body Temperature using FORA IR20b thermometer via Bluetooth
* @author Yuri Kitaev
*
*/
public class ForaIR20bTemperatureReader
{
  private static final String DEVICE_NAME = "TaiDoc-TM";
  private static final byte[] HANDSHAKE_1 = {0x51, 0x22, 00, 00, 00, 00, (byte) 0xa3, 0x16};
  private static final byte[] HANDSHAKE_2 = {0x51, 0x24, 00, 00, 00, 00, (byte) 0xa3, 0x18};
  private static final byte[] READINGS_REQUEST = {0x51, 0x2b, 00, 00, 00, 00, (byte) 0xa3, 0x1f};
  private static final byte[] READING1_REQUEST_1 = {0x51, 0x25, 00, 00, 00, 00, (byte) 0xa3, 0x19};
  private static final byte[] READING1_REQUEST_2 = {0x51, 0x26, 00, 00, 00, 00, (byte) 0xa3, 0x1a};
  private static final int PACKET_LEN = 8;
  private static final byte HANDSHAKE_1_ACK = 0x22;
  private static final byte HANDSHAKE_2_ACK = 0x24;
  private static final byte READINGS_REQUEST_ACK = 0x2b;
  private static final byte READING1_REQUEST_1_ACK = 0x25;
  private static final byte READING1_REQUEST_2_ACK = 0x26;
  private static byte[] mBuffer = new byte[PACKET_LEN];
  private static final UUID SERIAL_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); // UUID used for serial devices on Android
  
  /**
* Reads the last temperature from the thermometer over bluetooth and returns a TemperatureReading instance
* @param iContext Application Context
* @return Temperature reading from Bluetooth
* @throws MapperException In case of any error, MapperException is thrown with a detailed description
* @author Yuri Kitaev
*/
  public static TemperatureReading getLastTemperatureReading(Context iContext) throws MapperException
  {
    
    // 1. Check that the device is paired and get the reference to it if so
    BluetoothAdapter wBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    BluetoothDevice device = null;
    
    if (null == wBluetoothAdapter)
      throw new MapperException("Mapper could not access bluetooth.");
      
      Set<BluetoothDevice> pairedDevices = wBluetoothAdapter.getBondedDevices();
    
     // Check if the the thermometer is paired with the device in the first place
    if (pairedDevices.size() > 0) {
       for (BluetoothDevice dev : pairedDevices)
           if(DEVICE_NAME.equals(dev.getName()))
           {
               device = dev;
               break;
           }
     }
     if(null == device)
       throw new MapperException("Please pair FORA IR20b thermometer with your device first.");
     
     // 2. Establish RF Connection
     BluetoothSocket wSocket = null;
     try{
       wSocket = device.createRfcommSocketToServiceRecord(SERIAL_UUID);
     }
     catch (IOException e){
      throw new MapperException("Could not create the socket to connect to thermometer.");
     }
     catch (Exception e) {
       throw new MapperException(e.getMessage());
     }
     
     try
     {
    	 wBluetoothAdapter.cancelDiscovery();
       wSocket.connect();
       
       // 3. Handshake
       InputStream inputStream = null;
       OutputStream outputStream = null;
       try {
         inputStream = wSocket.getInputStream();
         outputStream = wSocket.getOutputStream();
       }
       catch (IOException e) {
         throw new MapperException("Failed to create I/O streams!");
       }
       
       try
       {
         send(outputStream, HANDSHAKE_1);
         readToBuffer(inputStream, mBuffer, PACKET_LEN, wSocket);
         if(mBuffer[1] != HANDSHAKE_1_ACK)
           throw new MapperException("Handshaking has failed because the first acknowledgement was incorrect.");
         
         send(outputStream, HANDSHAKE_2);
         readToBuffer(inputStream, mBuffer, PACKET_LEN, wSocket);
         if(mBuffer[1] != HANDSHAKE_2_ACK)
           throw new MapperException("Handshaking has failed because the second acknowledgement was incorrect.");
       }
       catch (IOException e) {
         throw new MapperException("Handshaking has failed: " + e.getMessage());
       }
       
       // 4. Ask for the latest reading
       byte[] part1 = new byte[PACKET_LEN];
       byte[] part2 = new byte[PACKET_LEN];
       try
       {
         send(outputStream, READINGS_REQUEST);
         readToBuffer(inputStream, mBuffer, PACKET_LEN, wSocket);
         if(mBuffer[1] != READINGS_REQUEST_ACK)
           throw new MapperException("Failed to initiate the download.");
         
         send(outputStream, READING1_REQUEST_1);
         readToBuffer(inputStream, mBuffer, PACKET_LEN, wSocket);
         if(mBuffer[1] != READING1_REQUEST_1_ACK)
           throw new MapperException("Failed to get the ACK for first part of the reading.");
         
         for(int i = 0; i < mBuffer.length; ++i)
           part1[i] = mBuffer[i];
         
         send(outputStream, READING1_REQUEST_2);
         readToBuffer(inputStream, mBuffer, PACKET_LEN, wSocket);
         if(mBuffer[1] != READING1_REQUEST_2_ACK)
           throw new MapperException("Failed to get the ACK for second part of the reading.");
         
         for(int i = 0; i < mBuffer.length; ++i)
           part2[i] = mBuffer[i];
         
         wSocket.close();
         return makeTemperatureReading(part1, part2, iContext);
       }
       catch (IOException e) {
         throw new MapperException("Failed to process the temperature request: " + e.getMessage());
       }
     }
     catch (Exception e)
     {
       if (wSocket != null){
         try{
           wSocket.close();
         }
         catch (Exception ex){}
       }
       throw new MapperException(e.getMessage());
     }
  }

  /**
* Sends the data byte array over the output stream and flushes the output
* @param iOutputStream The output stream
* @param iDataBuffer A byte array to send
* @throws IOException
*/
  private static void send(OutputStream iOutputStream, byte[] iDataBuffer) throws IOException
  {
     iOutputStream.write(iDataBuffer);
     iOutputStream.flush();
  }
  
  /**
* Reads exactly len bytes to the buffer from the specified input stream
* @param iInputStream The input stream
* @param oReadBuffer The buffer to where the data will be read. Must be at least iExpectedDataLen bytes long
* @param iExpectedDataLen The exact length of the expected data to read
* @param iSocket Socket that will be automatically closed in case of timeout
* @throws IOException
* @throws MapperException
*/
  private static void readToBuffer(InputStream iInputStream, byte[] oReadBuffer, int iExpectedDataLen, BluetoothSocket iSocket) throws IOException, MapperException
  {
    waitForInputWithTimeout(iInputStream, iSocket);
    int numBytesRead = 0;
    int bytesToRead = iExpectedDataLen;
    for(numBytesRead = 0; numBytesRead < bytesToRead; numBytesRead += iInputStream.read(oReadBuffer, numBytesRead, (bytesToRead-numBytesRead)));
  }

  /**
* Waits for the data to be available on the input stream and in case of timeout throws exception.
* Returns control as soon as there is data available and does nothing else.
* @param iInputStream The input stream
* @param iSocket Socket that will be automatically closed in case of timeout
* @throws IOException
* @throws MapperException
*/
  private static void waitForInputWithTimeout(InputStream iInputStream, BluetoothSocket iSocket) throws IOException, MapperException
  {
    int attemptsToDo = 80;
    while (iInputStream.available() <= 0)
    {
      try
      {
       Thread.sleep(100);
      }
      catch (InterruptedException e)
      {}
      attemptsToDo--;
      if(0 == attemptsToDo)
      {
        iSocket.close();
        throw new MapperException("Connection timeout.");
      }
    }
  }
  
  /**
* Factory method - creates a new valid TemperatureReading object from two parts of
* data returned from the thermometer
* @param iPart1 The first part of the reading returned from FORA IR20b
* @param iPart2 The second part of the reading returned from FORA IR20b
* @param iContext Application context
* @return TemperatureReading object created from the data extracted from the arguments
* @throws MapperException
* @author Yuri Kitaev
*/
  private static TemperatureReading makeTemperatureReading(byte[] iPart1, byte[] iPart2, Context iContext) throws MapperException
  {
    double tempC;
    float tempF;
    int year;
    int month;
    int day;
    int hour;
    int minute;

    // Refer to the FORA IR20b Protocol Reverse Engineering section for detailed explanation
    // of the data format and about how the individual parameters are encoded
    int rawDate = (iPart1[3] & 0xFF) * 0xFF + iPart1[2] & 0xFF;
    day = rawDate % 32;
    month = (rawDate >> 5) % 16;
    year = (rawDate >> 9) + 2000;
    hour = iPart1[5] & 0xFF;
    minute = iPart1[4] & 0xFF;
    tempC = ((iPart2[3] & 0xFF) * 0xFF + (iPart2[2] & 0xFF)) / (double) 10;
    tempF = (float) ((tempC * 9/5) + 32.0  + 0.2);
    
    Calendar cal = Calendar.getInstance();
    Date wDate = new Date();
    cal.setTime(wDate);
    cal.set(Calendar.MONTH, month - 1);
    cal.set(Calendar.DATE, day);
    cal.set(Calendar.YEAR, year);
    cal.set(Calendar.HOUR_OF_DAY, hour);
    cal.set(Calendar.MINUTE, minute);
    cal.set(Calendar.SECOND, 0);
    
    return new TemperatureReading(TemperatureMapper.getNextAvailableId(iContext), cal.getTime(), ReadingSource.BLUETOOTH, tempF);
  }
}