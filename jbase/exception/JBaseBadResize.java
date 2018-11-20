package jbase.exception;

import jbase.field.Field;

/**
 * Exception thrown when an invalid amount is passed to the resize method
 * @author Bryan McClain
 */
public class JBaseBadResize extends JBaseFieldException {

	private final int toAdd;


	/**
	 * Construct a new Bad Resize exception
	 * @param field The field causing this exception
	 * @param toAdd Number of rows trying to be added
	 */
	public JBaseBadResize(Field field, int toAdd) {
		super(field,"Bad resize given (To Add: "+toAdd+")");
		this.toAdd = toAdd;
	}

	/**	
	 * Get the invalid number of rows to add
	 * @return To add
	 */
	public int getToAdd() {
		return this.toAdd;
	}
}
