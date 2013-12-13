package com.augmentedsociety.myphr.domain;

@SuppressWarnings("serial")
public class MapperException extends Exception
{

  @SuppressWarnings("unused")
	private static final long SERIAL_VERSION_UID = 1L;

  public MapperException(){}

  public MapperException(String iDetailMessage)
  {
    super(iDetailMessage);
  }

  public MapperException(Throwable iThrowable)
  {
    super(iThrowable);
  }

  public MapperException(String iDetailMessage, Throwable iThrowable)
  {
    super(iDetailMessage, iThrowable);
  }

}
