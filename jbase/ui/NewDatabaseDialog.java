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
		String dbname = JBaseDialog.readLine("Database Name: ");
		while (allDB.contains(dbname)) {
			System.out.println("**Database '"+dbname+"' already exists!**");
			dbname = JBaseDialog.readLine("Database Name: ");
		}

		//Get username
		String username = "";
		while(username.trim().equals("")) {
			username = JBaseDialog.readLine("Root Username: ");
		}

		//Get password (cannot be null)
		String password = "";
		while(password.trim().equals("")) {
			password = JBaseDialog.readLine("Root Password: ");
		}

		Database db;
		try {
			db = Database.newDatabase(dbname,username,password);
		} catch (JBaseException ex) {
			System.out.println(ex.getMessage());
			return;
		}

		//Open the database dialog
		DatabaseDialog dialog = new DatabaseDialog(db);
		dialog.showDialog();
	}

}
