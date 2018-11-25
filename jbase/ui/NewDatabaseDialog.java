package jbase.ui;

import jbase.database.Database;
import jbase.exception.*;

import java.util.Set;


/**
 * Dialog to create a new JBase database
 * @author Bryan McClain
 */
public class NewDatabaseDialog implements JBaseDialog {

	/**
	 * Show the dialog for this interface
	 */
	public void showDialog() {

		//Get database name
		Set<String> allDB = Database.allDatabases();
		String dbname = JBaseDialog.readNotNull("Database Name: ", true);
		while (allDB.contains(dbname)) {
			System.out.println(">  Database '"+dbname+"' already exists!  <");
			dbname = JBaseDialog.readNotNull("Database Name: ", true);
		}

		//Get username and password (cannot be empty)
		String username = JBaseDialog.readNotNull("Root Username: ",true);
		String password = JBaseDialog.readNotNull("Root Password: ",true);

		//Try to create the database
		Database db;
		try {
			db = Database.newDatabase(dbname,username,password);
		} catch (JBaseException ex) {
			System.out.println(ex.getMessage()+"\n");
			return;
		}

		//Open the database dialog
		DatabaseDialog dialog = new DatabaseDialog(db);
		dialog.showDialog();
	}

}
