package jbase.exception;

import jbase.database.Database;



/**
 * Generic exception that happens with the database as a whole
 * @author Bryan McClain
 */
public class JBaseDatabaseException extends JBaseException {

	private final Database db;

	/**
	 * Create a new Database Exception
	 * @param db Database throwing this exception
	 * @param message Message to display for this exception
 	 */
	public JBaseDatabaseException(Database db, String message) {
		super(message);
		this.db = db;
	}

	/**
	 * Get the database that threw this exception
	 * @return Database
	 */
	public Database getDatabase() {
		return this.db;
	}
}
