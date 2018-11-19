package jbase.exception;

import jbase.database.DatabaseAction;


/**
 * Thrown when a user tries to edit the ACL list
 * @author Bryan McClain
 */
public class JBaseACLEditDenied extends JBasePermissionException {

	/**
	 * Construct a new ACL Edit Denied exception
	 * @param user The user causing this exception
	 */
	public JBaseACLEditDenied(String user) {
		super(user,DatabaseAction.EDIT_PERMISSIONS);
	}

	/**
	 * Construct a enw ACL Edit Denied exception
	 * @param user The user causing this exception
	 * @param message Custom message to display
	 */
	public JBaseACLEditDenied(String user, String message) {
		super(user,DatabaseAction.EDIT_PERMISSIONS,message);
	}

}
