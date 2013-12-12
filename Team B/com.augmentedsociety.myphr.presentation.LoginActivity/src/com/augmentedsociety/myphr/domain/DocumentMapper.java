package com.augmentedsociety.myphr.domain;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.Toast;

import com.augmentedsociety.myphr.persistence.DocumentTDG;
import com.augmentedsociety.myphr.persistence.PersistenceException;

/**
 * http://android.amberfog.com/?p=296
 * 
 * The mapper for the documents
 * Placeholder until I get the db stuff sorted out...
 * @author psyomn
 *
 */
public class DocumentMapper
{
	
	public static void insert(Document iDocument, Context iContext)
	{
		ArrayList<String> wValues = new ArrayList<String>(3);
		wValues.add(iDocument.getTitle());
		wValues.add(iDocument.getDescription());
		wValues.add(iDocument.getImageData());
		try
		{
			DocumentTDG.insert(wValues, iContext);
		} 
		catch (PersistenceException e)
		{
			Toast.makeText(iContext, e.getMessage(), Toast.LENGTH_LONG);
		}
	}
	
	public static void delete(Document iDocument)
	{
		
	}
	
	public static void update(Document iDocument)
	{
		
	}
	
	public static List<Document> findAll()
	{
		ArrayList<Document> docs = new ArrayList<Document>();
		
		return docs;
	}
}
