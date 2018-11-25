package jbase.exception;

import java.io.IOException;

/**
 * Thrown when a problem with file I/O happens when saving or loading
 * @author Bryan McClain
 */
public class JBaseIOException extends JBaseException {

	private final IOException except;

	/**
	 * Construct a new JBase File I/O exception
	 * @param except The IO Exception object that caused this error
	 */
	public JBaseIOException(IOException except) {
		super(except.getMessage());
		this.except = except;
	}


	/**
	 * Get the IO Exception that caused this exception
	 * @return IOException
	 */
	public IOException getException() {
		return this.except;
	}
}
