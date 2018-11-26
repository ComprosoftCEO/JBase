package jbase.ui;

import jbase.database.*;
import jbase.exception.*;

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
	public void showDialog() {
		while(runMenu());
	}


	/**
	 * Run the main menu for this dialog
	 * @return True if to run again, false if not
	 */
	private boolean runMenu() {

		//Show the list of commnads
		System.out.println("\n=== "+this.db.getDBName()+" ===");
		System.out.println(" F - Fields Menu");
		//System.out.println(" U - Users Menu");
		System.out.println(" S - Save Database");
		System.out.println(" R - Restore Database");
		System.out.println(" D - Drop Database");
		System.out.println(" Q - Quit (Logout)\n");

		//Read in commands and do them
		boolean running = true;
		while(running) {
			String line = JBaseDialog.readNotNull("> ",true);
		
			switch(line.toUpperCase()) {
				case "Q": return false;
				case "F": (new SelectKeyDialog(this.db)).showDialog(); break;
				case "S": saveDatabase(); continue;
				case "R": if (restoreDatabase()) {return false;} else {continue;}
				case "D": if (dropDatabase()) {return false;} else {continue;}
				default:
					System.out.println("Unknown command '"+line+"'");
					continue;
			}

			running = false;
		}

		return true;
	}



	/**
	 * Save the database to a file
	 */
	private void saveDatabase() {
		String filename = JBaseDialog.readNotNull("Filename: ",true);
		try {
			this.db.saveDatabase(filename);
		} catch (JBaseException ex) {
			System.out.println("*** "+ex.getMessage()+" ***\n");
			return;
		}
		System.out.println("Database Saved!");
	}


	/**
	 * Restore the database from a file
	 * @return True if the database was successfully restored, false otherwise
	 */
	private boolean restoreDatabase() {
		String filename = JBaseDialog.readNotNull("Filename: ",true);
		try {
			this.db.restoreDatabase(filename);
		} catch (JBaseException ex) {
			System.out.println("*** "+ex.getMessage()+" ***\n");
			return false;
		}
		System.out.println("Database Restored!");
		System.out.println("Note: You must log in again");
		return true;
	}



	/**
	 * Drop the database from memory
	 * @return True if the database was really dropped, false otherwise
	 */
	private boolean dropDatabase() {
		if (JBaseDialog.readYesNo("Really Drop Database?")) {
			try {
				this.db.dropDatabase();
			} catch (JBaseException ex) {
				System.out.println("*** "+ex.getMessage()+" ***\n");
				return false;
			}
		
			return true;
		}
		return false;
	}
}
