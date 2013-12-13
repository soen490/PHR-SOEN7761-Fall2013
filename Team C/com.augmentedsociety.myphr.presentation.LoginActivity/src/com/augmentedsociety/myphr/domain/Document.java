package com.augmentedsociety.myphr.domain;

/**
 * A document is any document that the patient needs to 
 * present to the doctor / other party. A document has a
 * description, an image, creation dates.
 * 
 * @author psyomn
 *
 */
public class Document
{
	
	/**
	 * Description of the document 
	 */
	private String mDescription;
	
	/**
	 * Title of the document
	 */
	private String mTitle;
	
	/**
	 * This is the absolute path to the external image file of the document.
	 * 
	 * http://developer.android.com/guide/topics/data/data-storage.html#filesExternal
	 */
	private String mImageData; 

	/**
	 * Construct a document object with standard parameters.
	 * 
	 * @param iDescription the description of the document
	 * @param iImageData
	 *   The byte array of the image which is presented as a string.
	 */
	public Document(String iTitle, String iDescription, String iImageData)
	{
		mTitle = iTitle;
		mImageData = iImageData; 
		mDescription = iDescription;
	}
	
	/**
	 * getter for description
	 * @return the description of the document
	 */
	public String getDescription()
	{
		return mDescription;
	}

	/**
	 * setter for the description
	 * @param iDescription the description to set
	 */
	public void setDescription(String iDescription)
	{
		mDescription = iDescription;
	}

	/**
	 * getter for the title
	 * @return the title of the document
	 */
	public String getTitle()
	{
		return mTitle;
	}

	/**
	 * The setter for the title 
	 * @param iTitle the title to set
	 */
	public void setTitle(String iTitle)
	{
		mTitle = iTitle;
	}

	/**
	 * get the image path
	 * @return the image path
	 */
	public String getImageData()
	{
		return mImageData;
	}

	/**
	 * setter for the image data
	 * @param iImagePath the image data to set
	 */
	public void setImageData(String iImagePath)
	{
		mImageData = iImagePath;
	}

}
