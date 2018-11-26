package jbase.exception;

/**
 * Exception thrown when trying to open a database that doesn't exist
 * @author Bryan McClain
 */
public class JBaseDatabaseNotFound extends JBaseException {

	private final String dbname;


	/**
	 * Construct a new Database Not Found exception
	 * @param dbname Name of the database not found
	 */
	public JBaseDatabaseNotFound(String dbname) {
		super("Database '"+dbname+"' doesn't exist!");
		this.dbname = dbname;
	}

	/**
	 * Get the database name that doesn't exist
	 * @return DB Name
	 */
	public String getDBName() {
		return this.dbname;
	}

}
