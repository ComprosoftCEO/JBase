package jbase.exception;

import java.io.IOException;

/**
 * Thrown when a problem with file I/O happens when saving or loading
 * @author Bryan McClain
 */
public class JBaseIOException extends JBaseException {

	private final String filename;
	private final IOException except;

	/**
	 * Construct a new JBase File I/O exception
	 * @param filename The file that caused this exception
	 * @param except The IO Exception object that caused this error
	 */
	public JBaseIOException(String filename, IOException except) {
		super(except.getMessage());
		this.filename = filename;
		this.except = except;
	}

	
	/**
	 * Get the name of the file that caused the IO Exception
	 * @return Filename
	 */
	public String getFilename() {
		return this.filename;
	}

	/**
	 * Get the IO Exception that caused this exception
	 * @return IOException
	 */
	public IOException getException() {
		return this.except;
	}
}
