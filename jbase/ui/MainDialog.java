package jbase.ui;

import jbase.database.Database;
import jbase.exception.*;
import java.util.Set;


/**
 * Main dialog class for interacting with JBase using CLI
 * @author Bryan McClain
 */
public class MainDialog implements JBaseDialog {

	/**
	 * Show the dialog for this interface
	 */
	public void showDialog() {
		//Run the menu until it quits
		while(runMenu());
	}


	/**
	 * Run the main menu for this dialog
	 * @return True if to run again, false if not
	 */
	private boolean runMenu() {
		System.out.println("\n=============================");
		System.out.println("|   JBase Database Engine   |");
		System.out.println("=============================");
		System.out.println("  Created by Bryan McClain\n");
		System.out.println(" N - New Database");
		System.out.println(" L - Load Database");
		System.out.println(" O - Open Database");
		System.out.println(" Q - Quit\n");

		boolean running = true;
		while(running) {
			String line = JBaseDialog.readNotNull("JBase:> ",true);
			switch(line.toUpperCase()) {
				case "Q": return false;
				case "N": newDatabase(); break;
				case "L": loadDatabase(); continue;
				case "O": if (openDatabase()) {break;} else {continue;}
				default:
					System.out.println("Unknown command '"+line+"'");
					continue;
			}

			running = false;
		}

		return true;
	}



	/**
	 * Construct a new database in memory
	 */
	private void newDatabase() {

		//Get database name
		String dbname = JBaseDialog.readUnique("Database Name: ",Database.allDatabases(),"*** That database already exists! ***", true);

		//Get username and password (cannot be empty)
		String username = JBaseDialog.readNotNull("Root Username: ",true);
		String password = JBaseDialog.readNotNull("Root Password: ",true);

		//Try to create the database
		Database db;
		try {
			db = Database.newDatabase(dbname,username,password);
		} catch (JBaseException ex) {
			System.out.println(ex.getMessage()+"");
			return;
		}
	
		//Open the database dialog
		DatabaseDialog dialog = new DatabaseDialog(db);
		dialog.showDialog();
	}



	/**
	 * Load the database from an existing file
	 */
	private void loadDatabase() {
		String filename = JBaseDialog.readNotNull("Filename: ", true);

		try {
			Database.loadDatabase(filename);
		} catch (JBaseException ex) {
			System.out.println("*** "+ex.getMessage()+" ***");
			return;
		}

		System.out.println("Database Loaded!");
	}


	/**
	 * Open an existing database
	 * @return If true, then redraw the main menu
	 */
	private boolean openDatabase() {

		Set<String> allDB = Database.allDatabases();
		if (allDB.size() <= 0) {
			System.out.println("*** No databases to open! ***");
			return false;
		}

		//Print out all existing databases
		System.out.println("Databases:");
		JBaseDialog.printCollection(allDB, true);
		System.out.println("");

		//Make sure user enters valid database name
		String dbname = JBaseDialog.readExisting("Database Name: ", allDB, "*** That database doesn't exist! ***", true);

		//Get username and password (cannot be empty)
		String username = JBaseDialog.readNotNull("Username: ",true);
		String password = JBaseDialog.readNotNull("Password: ",true);

		//Try to open the database
		Database db;
		try {
			db = Database.getDatabase(dbname,username,password);
		} catch (JBaseException ex) {
			System.out.println("*** "+ex.getMessage()+" ***");
			return false;
		}

		//Open the database dialog
		DatabaseDialog dialog = new DatabaseDialog(db);
		dialog.showDialog();
		return true;
	}



	/**
	 * Main method for the JBase Main Dialog
	 * @param args Command line arguments (not used)
	 */
	public static void main(String[] args) {
		JBaseDialog d = new MainDialog();
		d.showDialog();
	}
}
