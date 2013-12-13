package com.augmentedsociety.myphr.domain;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;

import com.augmentedsociety.myphr.persistence.PersistenceException;
import com.augmentedsociety.myphr.persistence.RecordDateTDG;

/**
 * Record Date Reading data mapper
 * 
 * @author Johannes Lange
 * 
 */
public class RecordDateMapper extends ReadingDataMapper
{

	/**
	 * Returns all distinct Date Reading objects that are in the database.
	 * 
	 * @param iContext
	 *          The application context
	 * 
	 * @note Recreates only the new objects, since Identity Map is no longer
	 *       applied: the reason for this is because whenever data is fetched, we
	 *       know its usage right away and will not need to keep it in proximity
	 *       for later usage, but rather direct usage
	 * 
	 * @return The ArrayList of all RecordDateReading in the database
	 * 
	 * @throws MapperException
	 *           Thrown when there is either a problem with mapping or if the
	 *           Persistence layer returns an error
	 */
	public static ArrayList<RecordDateReading> findAllDistinct(Context iContext)
			throws MapperException
	{
		try
		{
			ArrayList<ArrayList<String>> resultTable = RecordDateTDG
					.selectAll(iContext);
			ArrayList<RecordDateReading> resultReadings = new ArrayList<RecordDateReading>(
					resultTable.size());

			for (int i = 0; i < resultTable.size(); ++i)
			{
				ArrayList<String> values = resultTable.get(i);
				RecordDateReading r;
				Date date = new Date(Long.valueOf(values.get(0))); // Converts from Unix
																													 // time to Java Date

				r = new RecordDateReading( date);
				resultReadings.add(r);
			}
			return resultReadings;
		} catch (PersistenceException e)
		{
			throw new MapperException(
					"The Mapper failed to obtain the Blood Pressure readings from the persistence layer. The TDG returned the following error: "
							+ e.getMessage());
		} catch (Exception e)
		{
			throw new MapperException(
					"The mapper failed to complete an operation for the following unforeseen reason: "
							+ e.getMessage());
		}
	}

	
	/**
	 * Returns all Date Reading objects that are in the database.
	 * 
	 * @param iContext
	 *          The application context
	 * 
	 * @note Recreates only the new objects, since Identity Map is no longer
	 *       applied: the reason for this is because whenever data is fetched, we
	 *       know its usage right away and will not need to keep it in proximity
	 *       for later usage, but rather direct usage
	 * 
	 * @return The ArrayList of all RecordDateReading in the database
	 * 
	 * @throws MapperException
	 *           Thrown when there is either a problem with mapping or if the
	 *           Persistence layer returns an error
	 */
	public static ArrayList<RecordDateReading> findAll(Context iContext)
			throws MapperException
	{
		try
		{
			ArrayList<ArrayList<String>> resultTable = RecordDateTDG
					.selectAll(iContext);
			ArrayList<RecordDateReading> resultReadings = new ArrayList<RecordDateReading>(
					resultTable.size());

			for (int i = 0; i < resultTable.size(); ++i)
			{
				ArrayList<String> values = resultTable.get(i);
				RecordDateReading r;
				Long id = Long.valueOf(values.get(0));
				Date date = new Date(Long.valueOf(values.get(1))); // Converts from Unix
				long type = Long.valueOf(values.get(2));           // time to Java Date
																														
				r = new RecordDateReading(id, date, type);
				resultReadings.add(r);
			}
			return resultReadings;
		} catch (PersistenceException e)
		{
			throw new MapperException(
					"The Mapper failed to obtain the Date readings from the persistence layer. The TDG returned the following error: "
							+ e.getMessage());
		} catch (Exception e)
		{
			throw new MapperException(
					"The mapper failed to complete an operation for the following unforeseen reason: "
							+ e.getMessage());
		}
	}
	
}
