package com.augmentedsociety.myphr.domain;

import java.util.Date;
/**
 * Specifies profile reading
 * @author Surbhi Dubey 
 */

public class ProfileReading
{
	private Long mID;
	private String mLastName;
	private String mFirstName;
	private Date mDOB;
	private Long mPhoneNumber;
	private String mAddress;
	private String mEmail;
	private String mMedicare;
	private String mInsurance;
	private String mEmergencyContact1;
	private String mEmergencyContact2;


	/**
	 * General constructor
	 * @param iID
	 * @param iLastName
	 * @param iFirstName
	 * @param iDOB
	 * @param iPhoneNumber
	 * @param iAddress
	 * @param iEmail
	 * @param iMedicare
	 * @param iInsurance
	 * @param iEmergencyContact1
	 * @param iEmergencyContact2
	 */
	public ProfileReading (Long iID, String iLastName, String iFirstName, Date iDOB, long iPhoneNumber, String iAddress, 
			String iEmail, String iMedicare, String iInsurance, String iEmergencyContact1, String iEmergencyContact2)
	{
		this.mID = iID;
		this.mLastName = iLastName;
		this.mFirstName = iFirstName; 
		this.mDOB = iDOB; 
		this.mPhoneNumber = iPhoneNumber;
		this.mAddress = iAddress;
		this.mEmail = iEmail;
		this.mMedicare = iMedicare;
		this.mInsurance = iInsurance;
		this.mEmergencyContact1 = iEmergencyContact1;
		this.mEmergencyContact2 = iEmergencyContact2;

	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "ProfileReading [mID=" + mID + ", mLastName=" + mLastName
				+ ", mFirstName=" + mFirstName + ", mDOB=" + mDOB + ", mPhoneNumber="
				+ mPhoneNumber + ", mAddress=" + mAddress + ", mEmail=" + mEmail
				+ ", mMedicare=" + mMedicare + ", mInsurance=" + mInsurance
				+ ", mEmergencyContact1=" + mEmergencyContact1
				+ ", mEmergencyContact2=" + mEmergencyContact2 + "]";
	}

	public Long getID()
	{
		return mID;
	}

	public String getLastName()
	{
		return mLastName;
	}

	public void setLastName(String iLastName)
	{
		this.mLastName = iLastName;
	}

	public String getFirstName()
	{
		return mFirstName;
	}

	public void setFirstName(String iFirstName)
	{
		this.mFirstName = iFirstName;
	}

	public Date getDOB()
	{
		return mDOB;
	}

	public void setDOB(Date iDOB)
	{
		this.mDOB = iDOB;
	}

	public long getPhoneNumber()
	{
		return mPhoneNumber;
	}

	public void setPhoneNumber(long iPhoneNumber)
	{
		this.mPhoneNumber = iPhoneNumber;
	}

	public String getAddress()
	{
		return mAddress;
	}

	public void setAddress(String iAddress)
	{
		this.mAddress = iAddress;
	}

	public String getEmail()
	{
		return mEmail;
	}

	public void setEmail(String iEmail)
	{
		this.mEmail = iEmail;
	}

	public String getMedicare()
	{
		return mMedicare;
	}

	public void setMedicare(String iMedicare)
	{
		this.mMedicare = iMedicare;
	}

	public String getInsurance()
	{
		return mInsurance;
	}

	public void setInsurance(String iInsurance)
	{
		this.mInsurance = iInsurance;
	}

	public String getEmergencyContact1()
	{
		return mEmergencyContact1;
	}

	public void setEmergencyContact1(String iEmergencyContact1)
	{
		this.mEmergencyContact1 = iEmergencyContact1;
	}

	public String getEmergencyContact2()
	{
		return mEmergencyContact2;
	}

	public void setEmergencyContact2(String iEmergencyContact2)
	{
		this.mEmergencyContact2 = iEmergencyContact2;
	}



}