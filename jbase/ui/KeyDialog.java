package jbase.ui;

import jbase.database.Database;
import jbase.field.*;

import java.util.Set;
import java.util.HashSet;


/**
 * Dialog for interacting with a key field
 * @author Bryan McClain
 */
public class KeyDialog implements JBaseDialog {


	private final Database db;
	private final KeyField key;

	/**
	 * Create a new Key Field dialog
	 * @param db The database for this dialog
	 * @param key The key field for this dialog
	 */
	public KeyDialog(Database db, KeyField key) {
		this.db = db;
		this.key = key;
	}

	/**
	 * Show the dialog for this interface
	 */
	public void showDialog() {
		while(runMenu());
	}


	/**
	 * Get all children for this key field
	 * @return All Children (as a String)
	 */
	private Set<String> allChildren() {
		ChildField[] children = this.key.allChildren();
		Set<String> items = new HashSet<String>();
		for (ChildField child : children) {
			items.add(child.toField().getName());
		}
		return items;
	}


	/**
	 * Run the main menu for this dialog
	 * @return True if to run again, false if not
	 */
	private boolean runMenu() {
		System.out.println("\n=== "+key.getName()+" ===");

		System.out.println("Children: ");
		JBaseDialog.printCollection(this.allChildren());

		System.out.println("\n NI - New Item");
		System.out.println(" NF - New Foreign Key");
		System.out.println(" DF - Delete field");
		System.out.println(" I  - New record");
		System.out.println(" D  - Delete record");
		System.out.println(" V  - View all records");
		System.out.println(" Q  - Quit\n");

		boolean running = false;
		while (running) {
			String line = JBaseDialog.readLine("> ");
			switch(line) {
				case "Q": return false;
				case "V": viewRecords(); break;
				default: 
					System.out.println("Unknown command '"+line+"'");
					continue;
			}

			running = false;
		}

		return true;
	}



	/**
	 * Dump all records in the database
	 */
	private static void viewRecords() {
		
	}
}
