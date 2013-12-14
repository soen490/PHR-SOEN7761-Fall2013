package com.augmentedsociety.myphr.domain;

/**
 * Abstract DataMapper class; contains common methods used by several mappers
 * 
 * @author Yuri
 * 
 */
public abstract class ReadingDataMapper
{

  protected ReadingDataMapper()
  {

  }

  /**
   * Converts the integer identifier of source to the actual ENUM of
   * ReadingSource type
   * 
   * @param sourceInt The integer value of reading source
   * @return The corresponding ReadingSource value
   */
  protected static ReadingSource sourceFromInt(int iSourceInt)
  {
    switch (iSourceInt)
    {
    case 1:
      return ReadingSource.KEYED;
    case 2:
      return ReadingSource.BLUETOOTH;
    case 3:
      return ReadingSource.IMPORTED;
    default:
      return ReadingSource.OTHER;
    }
  }

  /**
   * Gets the database values for the reading source as integers
   * 
   * @param r The reading source enum
   * @return The integer representation
   */
  protected static int intFromSource(ReadingSource iR)
  {
    if (iR == ReadingSource.KEYED)
      return 1;
    else if (iR == ReadingSource.BLUETOOTH)
      return 2;
    else if (iR == ReadingSource.IMPORTED)
      return 3;
    else
      return 4;
  }
}
