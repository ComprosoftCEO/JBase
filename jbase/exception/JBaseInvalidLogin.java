package jbase.exception;

import jbase.database.Database;

/**
 * Exception thrown when a wrong credential is given trying to log into a database
 * @author Bryan McClain
 */
public class JBaseInvalidLogin extends JBaseDatabaseException {

	/**
	 * Construct a new Database Not Found exception
	 * @param db Database that caused the exception
	 */
	public JBaseInvalidLogin(Database db) {
		super(db,"Invalid username or password!");
	}
}

