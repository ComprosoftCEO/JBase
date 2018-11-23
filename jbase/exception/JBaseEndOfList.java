package jbase.exception;

import jbase.field.Field;


/**
 * Exception thrown when a call to next() or pre() has no next or previous entry
 * @author Bryan McClain
 */
public class JBaseEndOfList extends JBaseFieldException {

	/**
	 * Construct a new End of List Exception
	 * @param field The field that caused this exception
	 */
	public JBaseEndOfList(Field field) {
		super(field, "End of List");
	}
}

