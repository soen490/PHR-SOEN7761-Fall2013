package com.augmentedsociety.myphr.domain;

/**
 * Create new allergy activity
 * 
 * @author Roger Makram
 *
 */

public class Allergy
{
	private Long mID;
	private String mAllergic;
	private String mReaction;
	private String mSeverity;
	
	public Allergy(Long iID, String iAllergic, String iReaction, String iSeverity)
	{
		this.mID = iID;
		this.mAllergic = iAllergic;
		this.mReaction = iReaction;
		this.mSeverity = iSeverity;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Allergy [mID=" + mID + ", mAllergic=" + mAllergic + ", mReaction="
				+ mReaction + ", mSeverity=" + mSeverity + "]";
	}

	/**
	 * @return the mID
	 */
	public Long getID()
	{
		return mID;
	}

	/**
	 * @return the mAllergic
	 */
	public String getAllergic()
	{
		return mAllergic;
	}

	/**
	 * @param mAllergic the mAllergic to set
	 */
	public void setAllergic(String iAllergic)
	{
		this.mAllergic = iAllergic;
	}

	/**
	 * @return the mReaction
	 */
	public String getReaction()
	{
		return mReaction;
	}

	/**
	 * @param mReaction the mReaction to set
	 */
	public void setReaction(String iReaction)
	{
		this.mReaction = iReaction;
	}

	/**
	 * @return the mSeverity
	 */
	public String getSeverity()
	{
		return mSeverity;
	}

	/**
	 * @param mSeverity the mSeverity to set
	 */
	public void setSeverity(String iSeverity)
	{
		this.mSeverity = iSeverity;
	}
	

}
