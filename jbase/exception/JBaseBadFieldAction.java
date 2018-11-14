package jbase.exception;

import jbase.JBaseAction;
import jbase.field.*;


/**
 * This is thrown when trying to do an action on a field that doesn't
 *  support the action
 *
 * @author Bryan McClain
 */
public class JBaseBadFieldAction extends JBaseException {

	private final Field field;			// Field throwing this error
	private final FieldAction action;	// Action causing this error

	/**
	 * Create a new Bad Field Action exception
	 * @param field Field this action is being performed on
	 * @param action The action being performed
	 */
	public JBaseBadFieldAction(Field field, FieldAction action) {
		super("Cannot perform '"+action.actionName()+"' "+
			  "on field '"+field.getName()+"' "+
			  "(Type: "+JBaseAction.enumToString(field.getType())+")");

		this.field = field;
		this.action = action;
	}

	/**
	 * Get the field that threw this exception
	 * @return Field
	 */
	public Field getField() {
		return this.field;
	}

	/**
	 * Get the field action that caused this exception
	 * @return FieldAction
	 */
	public FieldAction getAction() {
		return this.action;
	}

}
