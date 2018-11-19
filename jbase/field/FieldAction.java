package jbase.field;

import jbase.JBaseAction;

/**
 * Represents an action that can be done on a field
 * @author Bryan McClain
 */
public enum FieldAction implements JBaseAction {
	INSERT,
	DELETE,
	GET,
	PUT,
	FIND,
	ITERATE,	/* Next and Pre */
	SEE_FIELD,	/* Can call Database.getField() */
	DELETE_FIELD,
	RESIZE_FIELD;


	/**
	 * Convert this enum constant to a camel-case string.
	 *  Underscores _ are replaced with spaces
	 *
	 * @return String
	 */
	@Override
	public String toString() {
		return JBaseAction.enumToString(this);
	}
}
