package jbase.exception;

import jbase.database.Database;

/**
 * Exception thrown when a user cannot be found in a database
 * @author Bryan McClain
 */
public class JBaseUserNotFound extends JBaseUserException {

	/**
	 * Construct a User Not Found exception
	 *
	 * @param db Database causing this exception
	 * @param user User that wasn't found
	 */
	public JBaseUserNotFound(Database db, String user) {
		super(db,user,"User '"+user+"' not found");
	}

}
