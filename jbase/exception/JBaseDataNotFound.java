package jbase.exception;

import jbase.field.Field;


/**
 * Exception thrown when the data doesn't exist when trying to do a delete or find
 * @author Bryan McClain
 */
public class JBaseDataNotFound extends JBaseFieldException {

	/**
	 * Construct a new JBase Data Not Found exception
	 * @param field The field that caused this exception
	 */
	public JBaseDataNotFound(Field field) {
		super(field,"Data not found");
	}

}
