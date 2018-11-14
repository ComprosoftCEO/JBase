package jbase.exception;


/**
 * Generic JBase excption
 * @author Bryan McClain
 */
public class JBaseException extends RuntimeException {

	/**
	 * Throw a new Generic JBase exception
	 * @param message String message to display for the exception
	 */
	public JBaseException(String message) {
		super(message);
	}

}
