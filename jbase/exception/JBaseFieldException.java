package jbase.exception;

import jbase.field.Field;


/**
 * Generic exception for a failed action that occurs on a field
 * @author Bryan McClain
 */
public class JBaseFieldException extends JBaseException {

	private final Field field;


	/**
	 * Construct a new field exception
	 *
	 * @param field The field that caused this exception
	 * @param message The message for this exception
	 */
	public JBaseFieldException(Field field, String message) {
		super(field.getName()+": "+message);
		this.field = field;
	}


	/**
	 * Get the field that caused this exception
	 * @return Field
	 */
	public Field getField() {
		return this.field;
	}
}
