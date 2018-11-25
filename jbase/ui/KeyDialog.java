package jbase.ui;

import jbase.database.Database;
import jbase.field.*;
import jbase.exception.*;

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

		boolean running = true;
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
	 * Dump all records in the database. Prints them in a nicely formatted table.
	 */
	private void viewRecords() {
		ChildField children[] = this.key.allChildren();
		String table[][] = new String[this.key.inUse()+1][children.length+1];

		//Get the header
		table[0][0] = this.key.getName();
		for (int i = 0; i < children.length; ++i) {
			table[0][i] = children[i].toField().getName();
		}

		//Keep going until the end of the list
		try {
			int row = -1;
			int tableRow = 1;
			while(true) {
				row = this.key.next(row);
				table[tableRow][0] = this.key.get(row).toString();
				for (int i = 0; i < children.length; ++i) {
					table[tableRow][i] = children[i].toField().get(row).toString();
				}
				tableRow+=1;
			}
		} catch (JBaseEndOfList eol) {/* Don't throw an error */
		} catch (JBaseException ex) {
			System.out.println(ex.getMessage()+"\n");
		}

		//Figure out the biggest entry in each column
		int bigCol[] = new int[children.length+1];
		for (int i = 0; i < table[0].length; ++i) {
			//Get the biggest entry in each column
			int max = 0;
			for (int j = 0; j < table.length; ++j) {
				if (table[j][i].length() > max) {max = table[j][i].length();}
			}
			bigCol[i] = max;
		}

		//Now print the table
		for (int i = 0; i < table.length; ++i) {
			System.out.print("|");
			for (int j = 0; j < table[i].length; ++j) {
				System.out.print(String.format("%-"+bigCol[j]+"s|",table[i][j]));
			}
			System.out.println("");
		}
	}
}
