package jbase.acl;

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

	public String actionName() {
		return JBaseAction.enumToString(this);
	}
}
