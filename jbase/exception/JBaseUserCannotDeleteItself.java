package jbase.exception;

import jbase.database.Database;

/**
 *
 *
 *
 */
public class JBaseUserCannotDeleteItself extends JBaseDatabaseException {

	private final String user;


	/**
	 * Construct a new Cannot Delete User exception
	 *
	 * @param db The database the user belongs to
	 * @param user The user trying to be deleted
	 */
	public JBaseUserCannotDeleteItself(Database db, String user) {
		super(db,"Cannot delete user '"+user+"'");
		this.user = user;
	}


	/**
	 * Get the user that failed to delete
	 * @return User
	 */
	public String getUser() {
		return this.user;
	}
}
