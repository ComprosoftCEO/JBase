package jbase.exception;

import jbase.database.Database;

/**
 * Exception thrown when trying to restore a database that doesn't match the name
 * @author Bryan McClain
 */
public class JBaseWrongDatabase extends JBaseDatabaseException {

	private final String dbname;

	/**
	 * Create a new Wrong Database exception
	 * @param db The database trying to be restored
	 * @param dbname The name mismatched from the database being restored
	 */
	public JBaseWrongDatabase(Database db, String dbname) {
		super(db,"Trying to restore wrong database. "+
			  "Expected '"+db.getDBName()+"', given '"+dbname+"'.");
		this.dbname = dbname;
	}

	/**
	 * Get the name that caused the exception
	 * @return Database name
	 */
	public String getDBName() {
		return this.dbname;
	}
}
