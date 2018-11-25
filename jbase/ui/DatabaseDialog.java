package jbase.ui;

import jbase.database.*;

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
		System.out.println("\n=== "+this.db.getDBName()+" ===");
		System.out.println(" F - Fields Menu");
		System.out.println(" U - Users Menu");
		System.out.println(" S - Save Database");
		System.out.println(" R - Restore Database");
		System.out.println(" D - Drop Database");
		System.out.println(" Q - Quit (Logout)");

		boolean running = true;
		while(running) {
			String line = JBaseDialog.readLine("> ");
		
			JBaseDialog d;
			switch(line.toUpperCase()) {
				case "Q": return false;
				case "F": d = new SelectKeyDialog(this.db); break;
				default:
					System.out.println("Unknown command '"+line+"'");
					continue;
			}

			d.showDialog();
			running = false;
		}

		return true;
	}
}
