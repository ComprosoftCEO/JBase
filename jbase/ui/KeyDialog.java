package jbase.ui;

import jbase.JBaseAction;
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
	 * Print out the children to the console (along with field type)
	 */
	private void printChildren() {
		ChildField[] children = this.key.allChildren();
		int i = 1;
		for (ChildField child : children) {
			Field f = child.toField();
			System.out.print(i+": "+f.getName()+
							 " ("+JBaseAction.enumToString(f.getType()));
		
			//Show the field pointing to	
			if (f.getType() == FieldType.FOREIGN_KEY) {
				ForeignKeyField fkey = (ForeignKeyField) f;
				PointableField pointable = fkey.getPoint();
				System.out.print(" -> "+pointable.toField().getName());
			}
			System.out.println(")");

			++i;
		}
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
	 * Run the main menu for this dialog
	 * @return True if to run again, false if not
	 */
	private boolean runMenu() {
		System.out.println("\n=== "+key.getName()+" ===");

		System.out.println("Depth: "+this.key.getDepth());
		System.out.println("In Use: "+this.key.inUse());
		System.out.println("Children: ");
		printChildren();

		System.out.println("\n NI - New Item");
		System.out.println(" NF - New Foreign Key");
		if (this.allChildren().size() > 0) {System.out.println(" DF - Delete field");}
		System.out.println(" I  - New record");
		if (this.key.inUse() > 0) {System.out.println(" D  - Delete record");}
		System.out.println(" V  - View all records");
		System.out.println(" Q  - Quit\n");

		boolean running = true;
		while (running) {
			String line = JBaseDialog.readNotNull("> ",true);
			switch(line.toUpperCase()) {
				case "Q": return false;
				case "NI": newItem(); break;
				case "NF": newForeignKey(); break;
				case "I": newRecord(); continue;
				case "V": viewRecords(); continue;
				case "DF":
					if (this.allChildren().size() > 0) {
						if (deleteField()) {break;} else {continue;}
					}
				case "D":
					if (this.key.inUse() > 0) {deleteRecord(); continue;}
				default: 
					System.out.println("Unknown command '"+line+"'");
					continue;
			}

			running = false;
		}

		return true;
	}



	/**
	 * Create a new item field
	 */
	private void newItem() {
		//Get the new item field
		Set<String> allFields = this.allFields();
		String newItem = JBaseDialog.readNotNull("New Item Name: ", true);
		while (allFields.contains(newItem)) {
			System.out.println("*** Field '"+newItem+"' already exists ***");
			newItem = JBaseDialog.readNotNull("New Item Name: ", true);
		}

		try {
			this.db.newItem(newItem,this.key);
		} catch (JBaseException ex) {
			System.out.println(ex.getMessage()+"\n");
		}
	}



	/**
	 * Create a new foreign key field
	 */
	private void newForeignKey() {
		//Get the new foreign key name
		Set<String> allFields = this.allFields();
		String newItem = JBaseDialog.readNotNull("New Foreign Key Name: ", true);
		while (allFields.contains(newItem)) {
			System.out.println("*** Field '"+newItem+"' already exists ***");
			newItem = JBaseDialog.readNotNull("New Foreign Key Name: ", true);
		}

		//Get what field it points to:
		Set<String> allKeys = this.allKeys();
		System.out.println("\nDatabase Keys: ");
		JBaseDialog.printCollection(allKeys);
		System.out.println("");

		String pointerField = JBaseDialog.readNotNull("Pointer Field: ", true);
		while(!allKeys.contains(pointerField)) {
			System.out.println("*** Field '"+newItem+"' does not exist ***");
			pointerField = JBaseDialog.readNotNull("Pointer Field: ", true);
		}

		try {
			this.db.newForeignKey(newItem,this.key,(KeyField) this.db.getField(pointerField));
		} catch (JBaseException ex) {
			System.out.println(ex.getMessage()+"\n");
		}
	}


	/**
	 * Delete a child field from this key
	 * @return True to redraw, false otherwise
	 */
	private boolean deleteField() {

		//Get the field to delete
		Set<String> allFields = this.allChildren();
		String toDelete = JBaseDialog.readNotNull("Field to Delete: ", true);
		if (!allFields.contains(toDelete)) {
			System.out.println("*** Field '"+toDelete+"' does not exist ***\n");
			return false;
		}

		try {
			this.db.getField(toDelete).deleteField();

		} catch (JBaseException ex) {
			System.out.println(ex.getMessage()+"\n");
			return false;
		}

		return true;
	}



	/**
	 * Create a new record in the database
	 */
	private void newRecord() {

	}



	/**
	 * Delete a record from the database
	 */
	private void deleteRecord() {

	}


	/**
	 * Dump all records in the database. Prints them in a nicely formatted table.
	 */
	private void viewRecords() {
		ChildField children[] = this.key.allChildren();
		String table[][] = new String[this.key.inUse()+1][children.length+2];

		//Get the header
		table[0][0] = "Row: ";
		table[0][1] = this.key.getName();
		for (int i = 0; i < children.length; ++i) {
			table[0][i+2] = children[i].toField().getName();
		}

		//Keep going until the end of the list
		try {
			int row = -1;
			int tableRow = 1;
			while(true) {
				row = this.key.next(row);
				table[tableRow][0] = Integer.toString(row);
				table[tableRow][1] = this.key.get(row).toString();
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
				if (table[j][i].length() > max) {
					max = table[j][i].length();
				}
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
