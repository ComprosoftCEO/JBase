package jbase.exception;

import jbase.database.Database;

/**
 * Exception that results when a user action fails
 * @author Bryan McClain
 */
public class JBaseUserException extends JBaseDatabaseException {

	private final String user;


	/**
	 * Construct a new User exception
	 * @param db Database that caused this exception
	 * @param user Name of the user that caused this exception
	 * @param message Message for this exception
	 */
	public JBaseUserException(Database db, String user, String message) {
		super(db,message);
		this.user = user;
	}

	/**
 	 * Get the name of the user that caused this exception
	 * @return User
	 */
	public String getUser() {
		return this.user;
	}
}
