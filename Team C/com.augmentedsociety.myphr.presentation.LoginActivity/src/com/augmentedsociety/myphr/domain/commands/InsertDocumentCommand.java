package com.augmentedsociety.myphr.domain.commands;

import com.augmentedsociety.myphr.domain.Document;
/**
 * This command inserts a document to the database 
 * @author psyomn
 */
public class InsertDocumentCommand
{
	private Document mDocument;
	/**
	 * Initialize with the required parameters
	 * @param iTitle
	 * @param iDescription
	 * @param iData
	 */
	public InsertDocumentCommand(String iTitle, String iDescription, String iData)
	{
		mDocument = new Document(iTitle, iDescription, iData);
	}
  public void execute()
  {
  	// TODO
  	// DocumentMapper.insert(mDocument);
  }
}
