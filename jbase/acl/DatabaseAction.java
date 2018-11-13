package jbase.acl;

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

	public String actionName() {
		return JBaseAction.enumToString(this);
	}
}
