package com.augmentedsociety.myphr.persistence;

/**
 * The exception is thrown when a TDG cannot read data due to an error. This
 * exception is independent of the persistence implementation and can be used in
 * the Domain layer without any reference to the implementation details
 * 
 * @author Yuri Kitaev
 * 
 */
@SuppressWarnings("serial")
public class PersistenceException extends Exception
{

  @SuppressWarnings("unused")
	private static final long SERIAL_VERSION_UID = 1L;

  public PersistenceException()
  {
  }

  public PersistenceException(String iDetailMessage)
  {
    super(iDetailMessage);
  }

  public PersistenceException(Throwable iThrowable)
  {
    super(iThrowable);
  }

  public PersistenceException(String iDetailMessage, Throwable iThrowable)
  {
    super(iDetailMessage, iThrowable);
  }

}
