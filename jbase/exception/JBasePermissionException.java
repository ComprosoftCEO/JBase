package jbase.exception;

import jbase.JBaseAction;
import jbase.database.*;


/**
 * Specific exception that occurs whenever a user doesn't have permission to do an action
 * @author Bryan McClain
 */
public class JBasePermissionException extends JBaseException {

	private final JBaseAction action;		// Action the user was trying to do
	private final String user;				// User that performed this action


	/**
	 * Construct a new JBase Permission Exception
	 * @param action The action that the user performed
	 * @param user The user performing this action
	 */
	public JBasePermissionException(JBaseAction action, String user) {
		super(action.actionName()+": User '"+user+"' does not have permission to do this");
		this.action = action;
		this.user = user;
	}

	/**
	 * Get the action that was called
	 * @return Action
	 */
	public JBaseAction getAction() {
		return this.action;
	}


	/**
	 * Get the user that executed the action
	 * @return User
	 */
	public String getUser() {
		return this.user;
	}
}
