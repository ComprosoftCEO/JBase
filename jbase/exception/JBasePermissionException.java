package jbase.exception;

import jbase.JBaseAction;

/**
 * Exception that occurs whenever a user doesn't have permission to do an action
 * @author Bryan McClain
 */
public class JBasePermissionException extends JBaseException {

	private final String user;			// User that performed this action
	private final JBaseAction action;	// Action the user was trying to do


	/**
	 * Construct a new JBase Permission Exception with the default message.
	 *
	 * @param user The user performing this action
	 * @param action The action that the user performed
	 */
	public JBasePermissionException(String user, JBaseAction action) {
		this(user,action,action.toString()+": User '"+user+"' does not have permission to do this");
	}


	/**
	 * Construct a new JBase Permission Exception with a custom message
	 *
	 * @param user The user performing this action
	 * @param action The action that the user performed
	 * @param message Overrides the default message
	 */
	public JBasePermissionException(String user, JBaseAction action, String message) {
		super(message);
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
