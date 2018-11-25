package jbase.ui;

import jbase.database.*;
import jbase.field.*;

/**
 * Dialog for interacting with the database object
 * @author Bryan McClain
 */
public class DatabaseDialog implements JBaseDialog {

	private final Database db;

	/**
	 * Create a new Database Dialog
	 * @param db The database to interact with
 	 */
	public DatabaseDialog(Database db) {
		this.db = db;
	}


	/**
	 * Show the dialog for this interface
	 */
	public void showDialog() {}

}
