package jbase.database;

import jbase.JBaseAction;

/**
 * Action that can be performed on a the database as a whole
 * @author Bryan McClain
 */
public enum DatabaseAction implements JBaseAction {
	LOGIN,
	SAVE,
	RESTORE,
	DROP,
	CREATE_FIELD,
	VIEW_USERS,
	ADD_USER,
	DELETE_USER,
	EDIT_PERMISSIONS;

	/**
	 * Get a string to represent this action
	 * @return Action Name
	 */
	public String actionName() {
		return JBaseAction.enumToString(this);
	}
}
