package jbase.database;

import jbase.JBaseAction;

/**
 * Action that can be performed on a the database as a whole
 * @author Bryan McClain
 */
public enum DatabaseAction implements JBaseAction {
	LOGIN,
	SAVE_DATABASE,
	RESTORE_DATABASE,
	DROP_DATABASE,
	CREATE_FIELD,
	VIEW_USERS,
	ADD_USER,
	DELETE_USER,
	EDIT_PERMISSIONS;

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
