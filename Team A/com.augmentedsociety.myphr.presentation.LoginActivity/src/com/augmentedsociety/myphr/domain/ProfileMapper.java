package com.augmentedsociety.myphr.domain;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.widget.Toast;

import com.augmentedsociety.myphr.persistence.PersistenceException;
import com.augmentedsociety.myphr.persistence.ProfileTDG;

/**
 * Profile data mapper
 * @author Surbhi Dubey
 */

public class ProfileMapper extends ReadingDataMapper
{
	/**
	 * Hidden constructor
	 */
	private ProfileMapper()
	{

	}

	/**
	 * Returns Profile Reading objects that are in the database.
	 * 
	 * @param c
	 *          The application context
	 * @note Recreates only the new objects, since Identity Map is no longer
	 *       applied: the reason for this is because whenever data is fetched, we
	 *       know its usage right away and will not need to keep it in proximity
	 *       for later usage, but rather direct usage
	 * @return The ArrayList of all ProfileReadings in the database
	 * @throws MapperException
	 *           Thrown when there is either a problem with mapping or if the
	 *           Persistence layer returns an error
	 */
	public static ArrayList<ProfileReading> findAll(Context iC)
			throws MapperException
	{
		try
		{
			ArrayList<ArrayList<String>> resultTable = ProfileTDG.selectAll(iC);
			ArrayList<ProfileReading> resultReadings = new ArrayList<ProfileReading>(
					resultTable.size());

			for (int i = 0; i < resultTable.size(); ++i)
			{
				ArrayList<String> values = resultTable.get(i);
				ProfileReading r;

				Long id = Long.valueOf(values.get(0));
				String lastName = values.get(1);
				String firstName = values.get(2);
				Date DOB = new Date(Long.valueOf(values.get(3)));
				int phoneNumber = Integer.valueOf(values.get(4));
				String address = values.get(5);
				String email = values.get(6);
				String medicare = values.get(7);
				String insurance = values.get(8);
				String emergencyContact1 = values.get(9);
				String emergencyContact2 = values.get(10);


				r = new ProfileReading(id,lastName, firstName, DOB, phoneNumber, address,
						email, medicare, insurance, emergencyContact1, emergencyContact2);
				resultReadings.add(r);
			}
			return resultReadings;
		} catch (PersistenceException iE)
		{
			throw new MapperException(
					"The Mapper failed to obtain the Blood Sugar readings from the persistence layer. The TDG returned the following error: "
							+ iE.getMessage());
		} catch (Exception iE)
		{
			throw new MapperException(
					"The mapper failed to complete an operation for the following unforeseen reason: "
							+ iE.getMessage());
		}
	}
	/**
   * Selects one single ProfileReading by the specified ID
   * @param iProfileId The id of the ProfileReading to get from the DB
   * @param iContext Application context
   * @return
   * @throws MapperException
   */
  public static ProfileReading getOne(long iProfileId, Context iContext) throws MapperException
  {
    try
    {
      ArrayList<String> values = ProfileTDG.select(iProfileId, iContext);

      ProfileReading profile;
      Long id = Long.valueOf(values.get(0));
			String lastName = values.get(1);
			String firstName = values.get(2);
			Date DOB = new Date(Long.valueOf(values.get(3)));
			Long phoneNumber = Long.valueOf(values.get(4));
			String address = values.get(5);
			String email = values.get(6);
			String medicare = values.get(7);
			String insurance = values.get(8);
			String emergencyContact1 = values.get(9);
			String emergencyContact2 = values.get(10);

      profile = new ProfileReading(id,lastName, firstName, DOB, phoneNumber, address,
					email, medicare, insurance, emergencyContact1, emergencyContact2);

      return profile;
    } 
    catch (PersistenceException e)
    {
      throw new MapperException(
          "The Mapper failed to obtain the Medication readings from the "
          + "persistence layer. The TDG returned the following error: "
          + e.getMessage());
    } 
    catch (Exception e)
    {
      throw new MapperException(
          "The mapper failed to complete an operation for the following "
          + "unforeseen reason: "
          + e.getMessage());
    }
  }
	/**
	 * Stores the new ProfileReading
	 * 
	 * @param r
	 *          The ProfileReading object
	 * @param c
	 *          The application context
	 * @throws MapperException
	 *           Thrown if there is a problem mapping or if the persistence
	 *           returns an error
	 */
	public static void insert(ProfileReading iR, Context iC)
			throws MapperException
	{
		try
		{
			ArrayList<String> values = new ArrayList<String>(15);

			values.add(String.valueOf(iR.getID()));
			values.add(iR.getLastName().toString());
			values.add(iR.getFirstName().toString());
			values.add(String.valueOf(iR.getDOB().getTime()));
			values.add(String.valueOf(iR.getPhoneNumber()));
			values.add(iR.getAddress().toString());
			values.add(iR.getEmail().toString());
			values.add(iR.getMedicare().toString());
			values.add(iR.getInsurance().toString());
			values.add(iR.getEmergencyContact1().toString());
			values.add(iR.getEmergencyContact2().toString());

			ProfileTDG.insert(values, iC);

			Toast.makeText(iC, "Data has been saved", Toast.LENGTH_SHORT).show();
		}
		/*
		 * catch (PersistenceException iE) { throw new MapperException(
		 * "The mapper failed to complete an operation because the persistence layer returned the following error: "
		 * + iE.getMessage()); }
		 */
		catch (Exception iE)
		{
			throw new MapperException(
					"The mapper failed to complete an operation for the following unforeseen reason: "
							+ iE.getMessage());
		}
	}

	/**
	 * Updates the profile
	 */
	public static void update(ProfileReading iR, Context iContext)
			throws MapperException
	{
		try
		{
			ArrayList<String> values = new ArrayList<String>(15);
			values.add(String.valueOf(iR.getID()));
			values.add(iR.getLastName().toString());
			values.add(iR.getFirstName().toString());
			values.add(String.valueOf(iR.getDOB().getTime()));
			values.add(String.valueOf(iR.getPhoneNumber()));
			values.add(iR.getAddress().toString());
			values.add(iR.getEmail().toString());
			values.add(iR.getMedicare().toString());
			values.add(iR.getInsurance().toString());
			values.add(iR.getEmergencyContact1().toString());
			values.add(iR.getEmergencyContact2().toString());

			int wRowsAffected = ProfileTDG.update(values, iContext);

			if (0 == wRowsAffected)
			{
				throw new MapperException(
						"Failed to update the entry (rows affected = 0); incorrect id?");
			}
		} catch (PersistenceException e)
		{
			throw new MapperException(
					"The mapper failed to complete an operation because the persistence layer returned the following error: "
							+ e.getMessage());
		} catch (Exception e)
		{
			throw new MapperException(
					"The mapper failed to complete an operation for the following unforeseen reason: "
							+ e.getMessage());
		}
	}
	/**
   * Finds the next ID available for using in an Profile reading
   * initialization
   * 
   * @param iContext The application context
   * @return The next available Id
   * @throws MapperException Thrown if there is a problem mapping or if the
   *           persistence returns an error
   */
  public static Long getNextAvailableId(Context iContext) throws MapperException
  {
    try
    {
      Long id = ProfileTDG.getNextAvailableId(iContext);
      return id;
    } 
    catch (PersistenceException e)
    {
      throw new MapperException(
          "The mapper failed to complete an operation because the persistence layer returned the following error: "
              + e.getMessage());
    } 
    catch (Exception e)
    {
      throw new MapperException(
          "The mapper failed to complete an operation for the following unforeseen reason: "
              + e.getMessage());
    }
  }
}