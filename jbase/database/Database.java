package jbase.database;


import jbase.field.*;
import jbase.exception.*;
import jbase.acl.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Set;
import java.io.Serializable;


/**
 * Represents a JBase Database in memory
 * @author Bryan McClain
 */
public class Database implements Serializable {

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




	//================Save and Load================

	/**
	 * Release the database from memory
	 * @throws JBaseException
	 */
	public void dropDatabase() throws JBaseException {

	}


	/**
	 * Save the database to a file
	 * @param filename The file to save to
	 */
	public void saveDatabase(String filename) {

	}


	public void restoreDatabase(String filename) {

	}

	public static void loadDatabase(String filename) {

	}


	//============Field Actions==============



	/**
	 * Construct a new Key Field in the database
	 *
	 * @param name The name of the field
	 * @param depth The depth of this field (number of rows)
	 * @return The new key field
	 *
	 * @throws JBaseDatabaseActionDenied User doesn't have permission to create new fields
	 * @throws JBaseDuplicateField This field already exists in the database
	 */
	public <T extends Serializable & Comparable<T>> KeyField<T> newKey(String name, int depth)
	  throws JBaseDuplicateField {
		if (!getACL().canDo(DatabaseAction.CREATE_FIELD)) {
			throw new JBaseDatabaseActionDenied(currentUser(),this,DatabaseAction.CREATE_FIELD);
		}

		//Test for duplicate fields
		if (this.fields.containsKey(name)) {
			throw new JBaseDuplicateField(this,name);
		}

		KeyField<T> key = new KeyField<T>(this,name,depth);
		this.fields.put(name,key);
		return key;
	}

	/**
	 * Construct a new field in the database
	 *
	 *
	 */
	/*<T extends Serializable> Field<T> newField(FieldType type, String name, int depth, KeyField owner, KeyField point)
	  throws JBaseDatabaseActionDenied {

		if (!getACL().canDo(DatabaseAction.CREATE_FIELD)) {
			throw new JBaseDatabaseActionDenied(currentUser(),this,DatabaseAction.CREATE_FIELD);
		}


		Field f = null;
		switch(type) {
			case KEY:
				f = new KeyField<T>(this,name,depth);
				break;

			case ITEM:
				f = new ItemField<T>(this,name,owner);
				break;

			case FOREIGN_KEY:
				f = new ForeignKeyField(this,name,owner,point);
				break;
		}
	}*/

	/**
	 * Get a field from the database
	 *
	 * @param name The field to search for
	 * @return The retrieved field
	 * @throws JBaseFieldNotFound That field doesn't exist in the database
	 */
	public Field getField(String name) throws JBaseFieldNotFound {
		Field f = this.fields.get(name);
		if (f == null) {throw new JBaseFieldNotFound(this,name);}
		return f;
	}


	/**
	 * Get an array of all fields in the database
	 * @return Array of all fields
	 */
	public Field[] allFields() {
		return (Field[]) this.fields.values().toArray();
	}




	//============User Actions=============

	/**
	 * Get the username for the current user logged in to the database
	 * @return Current User (as a String)
	 */
	public String currentUser() {
		return this.currentUser.getUsername();
	}


	/**
	 * Get an array of all users visible to the current user
	 * @return List of visible users
	 */
	public String[] allUsers() {

		ArrayList<String> usrs = new ArrayList<String>();
		if (getACL().canDo(DatabaseAction.VIEW_USERS)) {
			//I can view all users			
			for (User u : users.values()) {
				usrs.add(u.getUsername());
			}

		} else {
			//I can only view users that I created
			for (User u : users.values()) {
				if (u.getCreator() == this.currentUser) {usrs.add(u.getUsername());}
			}
		}
		return (String[]) usrs.toArray();
	}


	/**
	 * Create a new user in the database
	 *
	 * @param username The username for this new user
	 * @param password The password for this new user
	 * @throws JBaseDatabaseActionDenied Current user doesn't have permission to perform this action
	 * @throws JBaseDuplicateUser User already exists in the database
	 */
	public void newUser(String username, String password)
	throws JBaseDatabaseActionDenied, JBaseDuplicateUser {
		if (!getACL().canDo(DatabaseAction.ADD_USER)) {
			throw new JBaseDatabaseActionDenied(currentUser(),this,DatabaseAction.ADD_USER);
		}

		//Make sure the user doesn't already exists
		if (this.users.containsValue(username)) {
			throw new JBaseDuplicateUser(this,username);
		}

		this.users.put(username,new User(this,username,password,this.currentUser));
	}


	/**
	 * Delete a user from the database.
	 * This merely prevents the user from being able to log in again (as it no longer exists).
	 * However, it won't interrupt a current connection to the database.
	 *
	 * @param username The user to delete
	 */
	public void deleteUser(String username) {
		if (!getACL().canDo(DatabaseAction.DELETE_USER)) {
			throw new JBaseDatabaseActionDenied(currentUser(),this,DatabaseAction.DELETE_USER);
		}

		User user = this.users.get(username);
		
		//Make sure user actually exists
		if (user == null) {
			throw new JBaseUserNotFound(this,username);
		}

		//Make sure I can see the user (If I don't have "View Users" enabled)
		if (!getACL().canDo(DatabaseAction.VIEW_USERS)) {
			if (!(user.getCreator() == this.currentUser)) {
				throw new JBaseUserNotFound(this,username);	
			}
		}

		//Cannot delete root user
		if (user.isRoot()) {
			throw new JBaseDatabaseActionDenied(currentUser(),this,DatabaseAction.DELETE_USER,"Cannot delete root user!");
		}

		this.users.remove(username);
	}



	/**
	 * Get the Access Control List for the current Database user
	 * @return Access Control List (ACL)
	 */
	public ACL getACL() {
		return this.currentUser.getACL();
	}
}
