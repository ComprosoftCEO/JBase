package jbase.exception;

import jbase.database.Database;

/**
 * Thrown when trying to get a field that doesn't exist
 * @author Bryan McClain
 */
public class JBaseFieldNotFound extends JBaseDatabaseException {

	private final String field;


	/**
	 * Create a new Field Not Found exception
	 * @param db The database causing this exception
	 * @param field The field that doesn't exist
	 */
	public JBaseFieldNotFound(Database db, String field) {
		super(db,"Field '"+field+"' not found!");
		this.field = field;
	}


	/**
	 * Get the field that isn't found
	 * @return Field
	 */
	public String getField() {
		return this.field;
	}
}
