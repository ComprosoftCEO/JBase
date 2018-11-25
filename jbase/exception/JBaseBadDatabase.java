package jbase.exception;


/**
 * Exception caused by trying to load a database from an invalid file
 * @author Bryan McClain
 */
public class JBaseBadDatabase extends JBaseException {

	private final String filename;

	/**
	 * Construct a new Bad Database exception
	 * @param filename The file that contains the bad database
	 */
	public JBaseBadDatabase(String filename) {
		super("Bad database '"+filename+"'!");
		this.filename = filename;
	}

	/**
	 * Get the file with the bad database
	 * @return Filename
	 */
	public String getFilename() {
		return this.filename;
	}
}
