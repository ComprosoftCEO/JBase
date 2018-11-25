package jbase.exception;

import jbase.database.Database;

/**
 * Exception that results when trying to load a database that already exists
 * @author Bryan McClain
 */
public class JBaseDuplicateDatabase extends JBaseDatabaseException {

	/**
	 * Construct a new duplciate database exception
	 * @param db The database that already exists
	 */
	public JBaseDuplicateDatabase(Database db) {
		super(db,"Duplicate database exists!");
	}

}
