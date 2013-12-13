package com.augmentedsociety.myphr.persistence.dbcommands;

/**
 * Interface to be implemented by database commands.
 * 
 * @author psyomn
 */
public interface IDbCommand
{
  /**
   * Each command needs to execute, as suggested in GoF patterns.
   */
  void execute();
}
