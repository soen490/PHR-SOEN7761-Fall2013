package com.augmentedsociety.myphr.domain;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Medication class type
 * 
 * @author Rajan Jayakumar
 *
 */

public class Medication
{
	private Long mID;
  private String mType;
  private String mName;
  private String mPosology;
  private String mStrength;
  private String mFrequency;
  private Date mStartDate;
  private Date mEndDate;
  private String mReasons;
  private String mDoctor;
	
	public Medication(Long iID, String iType, String iName, String iPosology, 
			String iStrength, String iFrequency, Date iStartDate, Date iEndDate, 
			String iReasons, String iDoctor)
	{
		this.mID = iID;
		this.mType = iType;
		this.mName = iName;
		this.mPosology = iPosology;
		this.mStrength = iStrength;
		this.mFrequency = iFrequency;
		this.mStartDate = iStartDate;
		this.mEndDate = iEndDate;
		this.mReasons = iReasons;
		this.mDoctor = iDoctor;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Medication [mID=" + mID + ", mType" + mType + ", mName="
				+ mName + ", mPosology=" + mPosology + ", mStrength=" 
				+ mStrength + ", mFrequeny=" + mFrequency + ", mStartDate=" + mStartDate
				+ ", mEndDate=" + mEndDate + ", mReasons=" + mReasons + ", mDoctor="
				+ mDoctor + "]";
	}

	/**
	 * @return the mID
	 */
	public Long getID()
	{
		return mID;
	}

	/**
	 * @return the mType
	 */
	public String getType()
	{
		return mType;
	}

	/**
	 * @param set the mType
	 */
	public void setType(String iType)
	{
		this.mType = iType;
	}

	/**
	 * @return the mName
	 */
	public String getName()
	{
		return mName;
	}

	/**
	 * @param set the mName
	 */
	public void setName(String iName)
	{
		this.mName = iName;
	}	
	
	/**
	 * @return the mPosology
	 */
	public String getPosology()
	{
		return mPosology;
	}

	/**
	 * @param set the mPosology
	 */
	public void setPosology(String iPosology)
	{
		this.mPosology = iPosology;
	}	
	
	/**
	 * @return the mStrength
	 */
	public String getStrength()
	{
		return mStrength;
	}

	/**
	 * @param set the mStrength
	 */
	public void setStrength(String iStrength)
	{
		this.mStrength = iStrength;
	}	
	
	/**
	 * @return the mFrequency
	 */
	public String getFrequency()
	{
		return mFrequency;
	}

	/**
	 * @param set the mFrequency
	 */
	public void setFrequency(String iFrequency)
	{
		this.mFrequency = iFrequency;
	}	
	
	/**
	 * @return the mStartDate
	 */
	public Date getStartDate()
	{
		return mStartDate;
	}

	/**
	 * @param set the mStartDate
	 */
	public void setStartDate(Date iStartDate)
	{
		this.mStartDate = iStartDate;
	}	
	
	/**
	 * @return the mEndDate
	 */
	public Date getEndDate()
	{
		return mEndDate;
	}

	/**
	 * @param set the mEndDate
	 */
	public void setEndDate(Date iEndDate)
	{
		this.mEndDate = iEndDate;
	}	
	
	/**
	 * @return the mReasons
	 */
	public String getReasons()
	{
		return mReasons;
	}

	/**
	 * @param set the mReasons
	 */
	public void setReasons(String iReasons)
	{
		this.mReasons = iReasons;
	}	
	
	/**
	 * @return the mDoctor
	 */
	public String getDoctor()
	{
		return mDoctor;
	}

	/**
	 * @param set the mDoctor
	 */
	public void setDoctor(String iDoctor)
	{
		this.mDoctor = iDoctor;
	}	

	public String getStartDateString()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy, h:mm a");
		return sdf.format(mStartDate);
	}
	
	public String getEndDateString()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy, h:mm a");
		return sdf.format(mEndDate);
	}

}
