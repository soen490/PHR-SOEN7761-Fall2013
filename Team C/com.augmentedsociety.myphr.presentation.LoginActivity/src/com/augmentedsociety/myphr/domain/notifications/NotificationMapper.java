package com.augmentedsociety.myphr.domain.notifications;
import java.util.ArrayList;
import java.util.Date;
import android.content.Context;

import com.augmentedsociety.myphr.domain.MapperException;
import com.augmentedsociety.myphr.domain.ReadingDataMapper;
import com.augmentedsociety.myphr.persistence.NotificationTDG;
import com.augmentedsociety.myphr.persistence.PersistenceException;

/**
 * Notification data mapper
 * 
 * @author Yuri Kitaev
 * 
 */
public class NotificationMapper extends ReadingDataMapper
{
  /**
   * Returns all Notification objects that are in the database.
   * 
   * @param iContext The application context
   * @return The ArrayList of all Notifications in the database
   * @throws MapperException Thrown when there is either a problem with mapping
   *           or if the Persistence layer returns an error
   */
  public static ArrayList<Notification> findAll(Context iContext) throws MapperException
  {
    try
    {
      ArrayList<ArrayList<String>> wValuesTable = NotificationTDG.selectAll(iContext);
      ArrayList<Notification> wStoredNotifications = new ArrayList<Notification>(wValuesTable.size());
      for (int i = 0; i < wValuesTable.size(); ++i)
      {
        ArrayList<String> values = wValuesTable.get(i);
        Notification notification;
        Long id = Long.valueOf(values.get(0));
        Date firstOccurrence = new Date(Long.valueOf(values.get(1))); // Converts from Unix time to Java Date
        boolean isRecurring = (Integer.valueOf(values.get(2)) == 1);
        Long repeatInterval = Long.valueOf(values.get(3));
        boolean enabled = (Integer.valueOf(values.get(4)) == 1);
        String title = values.get(5);

        notification = new Notification(id, firstOccurrence, isRecurring, repeatInterval, enabled, title);
        wStoredNotifications.add(notification);
      }
      return wStoredNotifications;
    } 
    catch (PersistenceException e)
    {
      throw new MapperException(
          "The Mapper failed to obtain the Notification readings from the persistence layer. The TDG returned the following error: "
              + e.getMessage());
    } 
    catch (Exception e)
    {
      throw new MapperException(
          "The mapper failed to complete an operation for the following unforeseen reason: "
              + e.getMessage());
    }
  }
  
  /**
   * Selects one single Notification by the specified ID
   * @param iNotificationId The id of the notification to get from the DB
   * @param iContext Application context
   * @return
   * @throws MapperException
   */
  public static Notification getOne(long iNotificationId, Context iContext) throws MapperException
  {
    try
    {
      ArrayList<String> fields = NotificationTDG.select(iNotificationId, iContext);

      Notification notification;
      Long id = Long.valueOf(fields.get(0));
      Date firstOccurrence = new Date(Long.valueOf(fields.get(1))); // Converts from Unix time to Java Date
      boolean isRecurring = (Integer.valueOf(fields.get(2)) == 1);
      Long repeatInterval = Long.valueOf(fields.get(3));
      boolean enabled = (Integer.valueOf(fields.get(4)) == 1);
      String title = fields.get(5);

      notification = 
      		new Notification(id, firstOccurrence, isRecurring, repeatInterval, enabled, title);

      return notification;
    } 
    catch (PersistenceException e)
    {
      throw new MapperException(
          "The Mapper failed to obtain the Notification readings from the "
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
   * Stores the new Notification
   * 
   * @param iNotification The Notification object
   * @param iContext The application context
   * @throws MapperException Thrown if there is a problem mapping or if the
   *           persistence returns an error
   */
  public static void insert(Notification iNotification, Context iContext) throws MapperException
  {
    try
    {
      int recurringInt = iNotification.isRecurring() ? 1 : 0;
      int enabledInt = iNotification.isEnabled() ? 1 : 0;
      
      ArrayList<String> values = new ArrayList<String>(10);
      values.add(String.valueOf(iNotification.getId()));
      values.add(String.valueOf(iNotification.getFirstOccurrence().getTime()));
      values.add(String.valueOf(recurringInt));
      values.add(String.valueOf(iNotification.getRepeatInterval()));
      values.add(String.valueOf(enabledInt));
      values.add(String.valueOf(iNotification.getTitle()));

      NotificationTDG.insert(values, iContext);
    } 
    catch (PersistenceException e)
    {
      throw new MapperException(
          "The mapper failed to complete an operation because the persistence "
          + "layer returned the following error: "
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
   * Updates a Notification
   * 
   * @param iNotification The Notification object
   * @param iContext The application context
   * @throws MapperException Thrown if there is a problem mapping or if the
   *           persistence returns an error
   */
  public static void update(Notification iNotification, Context iContext) throws MapperException
  {
    try
    {
      int recurringInt = iNotification.isRecurring() ? 1 : 0;
      int enabledInt = iNotification.isEnabled() ? 1 : 0;
      
      ArrayList<String> values = new ArrayList<String>(10);
      values.add(String.valueOf(iNotification.getId()));
      values.add(String.valueOf(iNotification.getFirstOccurrence().getTime()));
      values.add(String.valueOf(recurringInt));
      values.add(String.valueOf(iNotification.getRepeatInterval()));
      values.add(String.valueOf(enabledInt));
      values.add(String.valueOf(iNotification.getTitle()));

      int wRowsAffected = NotificationTDG.update(values, iContext);
      
      if (0 == wRowsAffected)
      {
        throw new MapperException("Failed to update the entry (rows affected = 0); incorrect id?");
      }
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

  /**
   * Finds the next ID available for using in Notification reading
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
      Long id = NotificationTDG.getNextAvailableId(iContext);
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
  
  /**
   * Deletes a Notification
   * 
   * @param iNotification The Notification object
   * @param iContext The application context
   * @throws MapperException Thrown if there is a problem mapping or if the
   *           persistence returns an error
   */
  public static void delete(Notification iNotification, Context iContext) throws MapperException
  {
    try
    {
      int wRowsAffected = NotificationTDG.delete(iNotification.getId(), iContext);
      
      if (0 == wRowsAffected)
      {
        throw new MapperException("Failed to delete the entry (rows affected = 0); incorrect id?");
      }
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
