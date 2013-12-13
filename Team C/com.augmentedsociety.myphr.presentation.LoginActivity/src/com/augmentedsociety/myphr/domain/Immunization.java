package com.augmentedsociety.myphr.domain;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Immunization class type
 * 
 * @author Rajan Jayakumar
 *
 */

public class Immunization
{
	private Long mID;
  private String mType;
  private String mName;
  private String mManufacturer;
  private String mLot_number;
  private String mRoute;
  private String mPosology;
  private Date mDate;
  private String mLocation;
  private String mDetails;
  private String mComments;
	
	public Immunization(Long iID, String iType, String iName, String iManufacturer, 
			String iLot_number, String iRoute, String iPosology, Date iDate, 
			String iLocation, String iDetails, String iComments)
	{
		this.mID = iID;
		this.mType = iType;
		this.mName = iName;
		this.mManufacturer = iManufacturer;
		this.mLot_number = iLot_number;
		this.mRoute = iRoute;
		this.mPosology = iPosology;
		this.mDate = iDate;
		this.mLocation = iLocation;
		this.mDetails = iDetails;
		this.mComments = iComments;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Immunization [mID=" + mID + ", mType" + mType + ", mName="
				+ mName + ", mManufacturer=" + mManufacturer + ", mLot_number=" 
				+ mLot_number + ", mRoute=" + mRoute + ", mPsology=" + mPosology
				+ ", mDate=" + mDate + ", mLocation=" + mLocation + ", mDetails="
				+ mDetails + ", mComments=" + mComments + "]";
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
	 * @return the mManufacturer
	 */
	public String getManufacturer()
	{
		return mManufacturer;
	}

	/**
	 * @param set the mManufacturer
	 */
	public void setManufacturer(String iManufacturer)
	{
		this.mManufacturer = iManufacturer;
	}	
	
	/**
	 * @return the mLot_number
	 */
	public String getLot_number()
	{
		return mLot_number;
	}

	/**
	 * @param set the mLot_number
	 */
	public void setLot_number(String iLot_number)
	{
		this.mLot_number = iLot_number;
	}	
	
	/**
	 * @return the mRoute
	 */
	public String getRoute()
	{
		return mRoute;
	}

	/**
	 * @param set the mRoute
	 */
	public void setRoute(String iRoute)
	{
		this.mRoute = iRoute;
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
	 * @return the mDate
	 */
	public Date getDate()
	{
		return mDate;
	}

	/**
	 * @param set the mDate
	 */
	public void setDate(Date iDate)
	{
		this.mDate = iDate;
	}	
	
	/**
	 * @return the mLocation
	 */
	public String getLocation()
	{
		return mLocation;
	}

	/**
	 * @param set the mLocation
	 */
	public void setLocation(String iLocation)
	{
		this.mLocation = iLocation;
	}	
	
	/**
	 * @return the mDetails
	 */
	public String getDetails()
	{
		return mDetails;
	}

	/**
	 * @param set the mDetails
	 */
	public void setDetails(String iDetails)
	{
		this.mDetails = iDetails;
	}	
	
	/**
	 * @return the mComments
	 */
	public String getComments()
	{
		return mComments;
	}

	/**
	 * @param set the mType
	 */
	public void setComments(String iComments)
	{
		this.mComments = iComments;
	}

	public String getDateString()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy, h:mm a");
		return sdf.format(mDate);
	}
	

}
