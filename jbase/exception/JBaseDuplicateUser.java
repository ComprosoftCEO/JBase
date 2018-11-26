package jbase.exception;

import jbase.database.Database;

/**
 * Exception thrown if you try to create a user that already exists
 * @author Bryan McClain
 */
public class JBaseDuplicateUser extends JBaseUserException {

	/**
	 * Construct a new Duplicate User exception
	 * @param db The database causing the exception
	 * @param user The name of the user that already exists
	 */
	public JBaseDuplicateUser(Database db, String user) {
		super(db,user,"User '"+user+"' already exists!");
	}
}
