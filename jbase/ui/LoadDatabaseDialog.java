package jbase.ui;

import jbase.database.Database;
import jbase.exception.*;

/**
 * Dialog shown to load a database from an existing file
 * @author Bryan McClain
 */
public class LoadDatabaseDialog implements JBaseDialog {

	/**
	 * Show the dialog for this interface
	 */
	public void showDialog() {
		String filename = JBaseDialog.readNotNull("Filename: ", true);

		try {
			Database.loadDatabase(filename);
		} catch (JBaseException ex) {
			System.out.println(ex.getMessage()+"\n");
			return;
		}

		System.out.println("Database Loaded!");
	}
}
