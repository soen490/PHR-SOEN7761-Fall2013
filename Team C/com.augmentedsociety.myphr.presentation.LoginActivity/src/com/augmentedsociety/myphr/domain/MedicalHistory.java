package com.augmentedsociety.myphr.domain;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Immunization class type
 * 
 * @author Roger Makram
 *
 */

public class MedicalHistory
{
	private Long mID;
	private Date mDate;
	private String mDescription;
	private String mLocation;
	
	

	/**
	 * @param mID
	 * @param mDate
	 * @param mDescription
	 * @param mLocation
	 */
	public MedicalHistory(Long iID, Date iDate, String iDescription,
			String iLocation)
	{
		this.mID = iID;
		this.mDate = iDate;
		this.mDescription = iDescription;
		this.mLocation = iLocation;
	}



	/**
	 * @return the mDate
	 */
	public Date getDate()
	{
		return mDate;
	}



	/**
	 * @param mDate the mDate to set
	 */
	public void setDate(Date iDate)
	{
		this.mDate = iDate;
	}



	/**
	 * @return the mDescription
	 */
	public String getDescription()
	{
		return mDescription;
	}



	/**
	 * @param iDescription the mDescription to set
	 */
	public void setDescription(String iDescription)
	{
		this.mDescription = iDescription;
	}



	/**
	 * @return the mLocation
	 */
	public String getLocation()
	{
		return mLocation;
	}



	/**
	 * @param iLocation the mLocation to set
	 */
	public void setLocation(String iLocation)
	{
		this.mLocation = iLocation;
	}



	/**
	 * @return the mID
	 */
	public Long getID()
	{
		return mID;
	}



	public String getDateString()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy, h:mm a");
		return sdf.format(mDate);
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "MedicalHistory [mID=" + mID + ", mDate=" + this.getDateString()
				+ ", mDescription=" + mDescription + ", mLocation=" + mLocation + "]";
	}
	

	
	
}
