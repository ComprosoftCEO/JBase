package jbase.exception;

import jbase.field.*;

public class JBaseFieldActionDenied extends JBasePermissionException {

	private final Field field;

	/**
	 * Construct a new Field Action Denied exception
	 *
	 * @param user The user trying to perform this action
	 * @param db The database this action is being performed on
	 * @param action The action being executed
	 */
	public JBaseFieldActionDenied(String user, Field field, FieldAction action) {
		super(user,action);
		this.field = field;
	}


	/**
	 * Get the field that caused this exception
	 * @return Field
	 */
	public Field getField() {
		return this.field;
	}



	/**
	 * Get the field action that caused this exception
	 * @return Field Action
	 */
	@Override
	public FieldAction getAction() {
		return (FieldAction) super.getAction();
	}
}
