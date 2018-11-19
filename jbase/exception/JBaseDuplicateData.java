package jbase.exception;

import jbase.field.Field;


/**
 * Exception thrown when trying to insert into a key where the data already exists
 * @author Bryan McClain
 */
public class JBaseDuplicateData extends JBaseFieldException {

	/**
	 * Construct a new JBase Duplicate Data exception
	 * @param field The field that caused this exception
	 */
	public JBaseDuplicateData(Field field) {
		super(field,"Trying to insert duplicate data");
	}

}
