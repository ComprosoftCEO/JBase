package jbase;


import jbase.field.*;
import jbase.user.*;
import jbase.exception.*;

import java.util.HashMap;
import java.util.Arrays;
import java.util.Set;


public final class Database {

	private final String dbname;				// Name of the database
	private HashMap<String,Field> fields;		// List of fields this database owns
	private HashMap<String,User> users;			// List of users in the database
	private User currentUser;					// Current user logged in to the database

	//Global list of all active databases
	private static HashMap<String,Database> allDatabases = new HashMap<String,Database>(); 


	/**
	 * Create a new Database object
	 *
	 * @param dbname Name of the database
	 * @param rootUser Username for the root user of the database
	 * @param rootPass Password for the root user of the database
	 */
	private Database(String dbname, String rootUser, String rootPass) {
		this.dbname = dbname;
		this.fields = new HashMap<String,Field>();
		this.users = new HashMap<String,User>();

		//Create the root User
		this.currentUser = new User(this,rootUser,rootPass,null);
		this.users.put(rootUser,this.currentUser);
	}


	/**
	 * Create a new Database object, and add it to the internal list of databases
	 *
	 * @param dbname Name of the database
	 * @param rootUser Username for the root user of the database
	 * @param rootPass Password for the root user of the database
	 * @return The newly created database
	 * 
	 * @throws JBaseException
	 */
	public static Database newDatabase(String dbname, String rootUser, String rootPass) throws JBaseException {

		//Make sure the database doesn't already exist
		if (allDatabases.containsKey(dbname)) {
			throw new JBaseException("This Database Already Exists!");
		}

		//Create the database
		Database db = new Database(dbname,rootUser,rootPass);
		allDatabases.put(dbname,db);
		return db;
	}


	/**
	 * Open an existing database in the system
	 *
	 * @param dbname Name of the database
	 * @param username Username for the Database user
	 * @param password Password for the Database user
	 * @return The newly created database
	 *
	 * @throws JBaseException
	 */
	public static Database getDatabase(String dbname, String username, String password) throws JBaseException {

		//Find the database (or throw an error if it doesn't exist)
		Database db = allDatabases.get(dbname);
		if (db == null) {throw new JBaseException("That Database Does Not Exist!");}

		//Try to log in to the database
		User u = db.users.get(username);
		if (u == null) {throw new JBaseException("Invalid Username or Password");}
		if (!u.validatePassword(password)) {throw new JBaseException("Invalid Username or Password");}

		//We are all good to go!
		db.currentUser = u;
		return db;
	}


	/**
	 * Get a list of all currently loaded databases
	 * @return String array of all databases
	 */
	public static String[] allDatabases() {
		Set<String> vals = allDatabases.keySet();
		return (String[]) vals.toArray();
	}


	/**
	 * Get the name of this database
	 * @return Database Name
	 */
	public String getDBName() {
		return this.dbname;
	}





	public void dropDatabase() throws JBaseException {

	}

	public void saveDatabase(String filename) {

	}


}
