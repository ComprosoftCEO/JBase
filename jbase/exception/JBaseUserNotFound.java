package jbase.exception;

import jbase.database.Database;

/**
 * Exception thrown when a user cannot be found in a database
 * @author Bryan McClain
 */
public class JBaseUserNotFound extends JBaseDatabaseException {

	private final String user;	//User that wasn't found

	/**
	 * Construct a User Not Found exception
	 *
	 * @param db Database causing this exception
	 * @param user User that wasn't found
	 */
	public JBaseUserNotFound(Database db, String user) {
		super(db,"User '"+user+"' not found");
		this.user = user;
	}

	/**
	 * Get the user that wasn't found
	 * @return User
	 */
	public String getUser() {
		return this.user;
	}
}
