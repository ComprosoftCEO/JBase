package jbase.ui;

import jbase.database.*;
import jbase.exception.*;
import jbase.acl.*;

import java.util.Set;


/**
 * Dialog that allows you to edit the Access Control List of a given user
 * @author Bryan McClain
 */
public class ACLDialog implements JBaseDialog {

	private final Database db;
	private final String user;
	private final ACL acl;

	/**
	 * Create a new ACL Dialog
	 * @param db The database for this dialog
	 * @param user The user to edit the ACL for
	 * @throws JBaseUserNotFound Invalid user passed to the dialog
	 */
	public ACLDialog(Database db, String user) throws JBaseUserNotFound {
		this.db = db;
		this.user = user;
		this.acl = this.db.getUserACL(user);
	}


	/**
	 * Show the dialog for this interface
	 */
	public void showDialog() {

	}

}
