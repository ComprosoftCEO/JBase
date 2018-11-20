package jbase.exception;

import jbase.field.Field;

/**
 * Exception thrown when trying to insert into a key field with no space left
 * @author Bryan McClain
 */
public class JBaseOutOfMemory extends JBaseFieldException {

	/**
	 * Construct a new Out of Memory exception
	 * @param field The field that is out of memory
	 */
	public JBaseOutOfMemory(Field field) {
		super(field,"Out of Memory to Insert");
	}

}
