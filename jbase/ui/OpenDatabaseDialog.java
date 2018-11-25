package jbase.ui;

import jbase.database.Database;
import jbase.exception.*;
import java.util.Set;

/**
 * Dialog to open an existing loaded database
 * @author Bryan McClain
 */
public class OpenDatabaseDialog implements JBaseDialog {

	/**
	 * Show the dialog for this interface
	 */
	public void showDialog() {

		Set<String> allDB = Database.allDatabases();
		if (allDB.size() <= 0) {
			System.out.println("No databases to open!\n");
			return;
		}

		//Print out all existing databases
		JBaseDialog.printCollection(allDB);
		System.out.println("");

		//Make sure user enters valid database name
		String dbname = JBaseDialog.readNotNull("Database Name: ", true);
		while (!allDB.contains(dbname)) {
			System.out.println(">  Database '"+dbname+"' doesn't exists!  <");
			dbname = JBaseDialog.readNotNull("Database Name: ", true);
		}

		//Get username and password (cannot be empty)
		String username = JBaseDialog.readNotNull("Username: ",true);
		String password = JBaseDialog.readNotNull("Password: ",true);

		//Try to open the database
		Database db;
		try {
			db = Database.getDatabase(dbname,username,password);
		} catch (JBaseException ex) {
			System.out.println(ex.getMessage()+"\n");
			return;
		}

		//Open the database dialog
		DatabaseDialog dialog = new DatabaseDialog(db);
		dialog.showDialog();
	}
}
