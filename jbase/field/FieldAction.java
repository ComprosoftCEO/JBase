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
	 * Get a string to represent this action
	 * @return Action Name
	 */
	public String actionName() {
		return JBaseAction.enumToString(this);
	}
}
