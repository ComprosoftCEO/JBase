package jbase.exception;

import jbase.database.*;


/**
 * Exception thrown when trying to execute a database action that
 *  you don't have permission to do
 * @author Bryan McClain
 */
public class JBaseDatabaseActionDenied extends JBasePermissionException {

	private final Database db;

	/**
	 * Construct a new Database Action Denied exception
	 *
	 * @param user The user trying to perform this action
	 * @param db The database this action is being performed on
	 * @param action The action being executed
	 */
	public JBaseDatabaseActionDenied(String user, Database db, DatabaseAction action) {
		super(user,action);
		this.db = db;
	}

	/**
	 * Get the database object that caused this exception
	 * @return Database
	 */
	public Database getDatabase() {
		return this.db;
	}


	/**
	 * Get the database action that caused this exception
	 * @return Database Action
	 */
	@Override
	public DatabaseAction getAction() {
		return (DatabaseAction) super.getAction();
	}
}
