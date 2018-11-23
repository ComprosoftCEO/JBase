package jbase.exception;

import jbase.database.Database;

/**
 * Thrown when trying to create a field that already exists
 * @author Bryan McClain
 */
public class JBaseDuplicateField extends JBaseDatabaseException {

	private final String field;


	/**
	 * Create a new Duplicate Field exception
	 * @param db The database causing this exception
	 * @param field The field that already exist
	 */
	public JBaseDuplicateField(Database db, String field) {
		super(db,"Duplicate Field '"+field+"'!");
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

