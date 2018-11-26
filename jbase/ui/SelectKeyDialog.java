package jbase.ui;

import jbase.database.*;
import jbase.exception.*;
import jbase.field.*;

import java.util.Set;
import java.util.HashSet;

/**
 * Dialog to prompt the user to select a key field (or create a new key)
 * @author Bryan McClain
 */
public class SelectKeyDialog implements JBaseDialog {

	private final Database db;

	/**
	 * Create a new Select Key dialog
	 * @param db The database to interact with
 	 */
	public SelectKeyDialog(Database db) {
		this.db = db;
	}


	/**
	 * Get the list of all fields in the database
	 * @return List of all fields
	 */
	private Set<String> allFields() {
		Field[] allFields = this.db.allFields();
		Set<String> set = new HashSet<String>();
		for (Field f : allFields) {
			set.add(f.getName());
		}
		return set;
	}


	/**
	 * Get the list of all keys in the database
	 * @return list of all keys
	 */
	private Set<String> allKeys() {
		Field[] allFields = this.db.allFields();
		Set<String> set = new HashSet<String>();
		for (Field f : allFields) {
			if (f.getType() == FieldType.KEY) {set.add(f.getName());}
		}
		return set;
	}


	/**
	 * Print out the keys to the terminal
	 */
	private void printKeys() {
		Field[] allFields = this.db.allFields();
		int i = 1;
		for (Field f : allFields) {
			if (f.getType() == FieldType.KEY) {
				System.out.println(i+": "+f.getName()+" (Depth: "+f.getDepth()+")");
				++i;
			}
		}
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
		Set<String> allKeys = this.allKeys();
		System.out.println("\n=== "+this.db.getDBName()+" Fields: ===");
		System.out.println("Keys:");
		printKeys();

		System.out.println("\n N - Create a new key field");
		if (allKeys.size() > 0) {
			System.out.println(" E - Edit an existing key field");
			System.out.println(" D - Delete a key field");
		}
		System.out.println(" Q - Quit the menu\n");

		boolean running = true;
		while(running) {
			String line = JBaseDialog.readNotNull("> ",true);
			switch(line.toUpperCase()) {
				case "Q": return false;
				case "N": newKey(); break;
				case "E":
					if (allKeys.size() > 0) {editKey(); break;}
				case "D":
					if (allKeys.size() > 0) {
						if(deleteKey()) {break;} else {continue;}
					}
				default:
					System.out.println("Unknown command '"+line+"'");
					continue;
			}

			running = false;
		}

		return true;
	}


	/**
	 * Create a new key and open the key dialog
	 */
	private void newKey() {
		//Get the new key field
		Set<String> allFields = this.allFields();
		String newKey = JBaseDialog.readNotNull("New Field Name: ", true);
		while (allFields.contains(newKey)) {
			System.out.println("*** Field '"+newKey+"' already exists ***");
			newKey = JBaseDialog.readNotNull("New Field Name: ", true);
		}

		//Get the depth for the field
		int depth = JBaseDialog.readInt("New Field Depth: ");
		while (depth <= 0) {
			System.out.println("*** Invalid depth '"+depth+"' ***");
			depth = JBaseDialog.readInt("New Field Depth: ");
		}

		//Try to create the field
		try {
			db.<String>newKey(newKey,depth);
		} catch (JBaseException ex) {
			System.out.println(ex.getMessage()+"\n");
			return;
		}
	}


	/**
	 * Edit an existing key and open the key dialog
	 */
	private void editKey() {

		//Get the new key field
		Set<String> allKeys = this.allKeys();
		String newKey = JBaseDialog.readNotNull("Edit Key: ", true);
		while (!allKeys.contains(newKey)) {
			System.out.println("*** Unknown key field '"+newKey+"' ***");
			newKey = JBaseDialog.readNotNull("Edit Key: ", true);
		}

		//Try to open the field
		KeyField key;
		try {
			key = (KeyField) db.getField(newKey);
		} catch (JBaseException ex) {
			System.out.println(ex.getMessage()+"\n");
			return;
		}

		JBaseDialog d = new KeyDialog(this.db,key);
		d.showDialog();
	}



	/**
	 * Delete an existing key
	 * @return True if to redraw, false otherwise
	 */
	private boolean deleteKey() {

		//Get the key field
		Set<String> allKeys = this.allKeys();
		String newKey = JBaseDialog.readNotNull("Delete Key: ", true);
		if(!allKeys.contains(newKey)) {
			System.out.println("*** Unknown key field '"+newKey+"' ***\n");
			return false;
		}

		//Try to delete the field
		try {
			this.db.getField(newKey).deleteField();
		} catch (JBaseException ex) {
			System.out.println(ex.getMessage()+"\n");
			return false;
		}

		return true;
	}
}
