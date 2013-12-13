package com.augmentedsociety.myphr.domain.commands;

/**
 * Isolated business logic for creating a document.
 * 
 * @author psyomn
 *
 */
public class CreateDocumentCommand implements ICommand
{

	/**
	 * The path to the resource (external device resource)
	 */
	private String mPath; 
	
	/**
	 * The path to the resource thumbnail (external)
	 */
	private String mThumbPath; 
	
	/**
	 * The description of the document
	 */
	private String mDescription;
	
	/**
	 * The title of the document
	 */
	private String mTitle;
	
	/**
	 * Create the document
	 * @param iPath        Path to the image resource
	 * @param iThumbPath   Path to the thumbnail of the image resource
	 * @param iDescription Description of document (optional)
	 * @param iTitle       The title of the document (required)
	 */
	public CreateDocumentCommand(String iPath, String iThumbPath, 
			                         String iDescription, String iTitle)
	{
		mTitle       = iTitle;
		mThumbPath   = iThumbPath;
		mDescription = iDescription;
		mPath        = iPath;
	}
	
	@Override
	public void execute()
	{

	}

}
