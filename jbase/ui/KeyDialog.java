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

		//General info about the key
		System.out.println("\n=== "+key.getName()+" ===");
		System.out.println("Depth: "+this.key.getDepth());
		System.out.println("In Use: "+this.key.inUse());
		System.out.println("Children: ");
		printChildren();

		//Print the list of commands
		System.out.println("\n NI - New Item");
		System.out.println(" NF - New Foreign Key");
		if (this.allChildren().size() > 0) {System.out.println(" DF - Delete field");}
		System.out.println(" I  - New record");
		if (this.key.inUse() > 0) {System.out.println(" E  - Edit record");}
		if (this.key.inUse() > 0) {System.out.println(" D  - Delete record");}
		System.out.println(" R  - Resize Key");
		System.out.println(" V  - View all records");
		System.out.println(" Q  - Quit\n");

		//Read in commands
		boolean running = true;
		while (running) {
			String line = JBaseDialog.readNotNull("> ",true);

			switch(line.toUpperCase()) {
				case "Q": return false;
				case "NI": newItem(); break;
				case "NF": newForeignKey(); break;
				case "I": newRecord(); continue;
				case "V": viewRecords(); continue;
				case "R": resizeKey(); break;
				case "DF":
					if (this.allChildren().size() > 0) {
						if (deleteField()) {break;} else {continue;}
					}
				case "E":
					if (this.key.inUse() > 0) {editRecord(); continue;}
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

		String newItem = JBaseDialog.readUnique("New Item Name: ", this.allFields(), "*** That Field Already Exists! ***", true);
		try {
			this.db.<String>newItem(newItem,this.key);
		} catch (JBaseException ex) {
			System.out.println(ex.getMessage()+"\n");
		}
	}



	/**
	 * Create a new foreign key field
	 */
	private void newForeignKey() {
		//Get the new foreign key name

		String newFKey = JBaseDialog.readUnique("New Foreign Key Name: ", this.allFields(), "*** That Field Already Exists! ***", true);

		//Get what field it points to:
		//  Help the user out by printing a list of keys
		System.out.println("Pointer Field:");
		JBaseDialog.printCollection(this.allKeys(), true);
		System.out.println("");

		String pointerField = JBaseDialog.readExisting("Pointer: ", this.allKeys(), "*** That Key Field Doesn't Exist ***", true);

		try {
			this.db.<String>newForeignKey(newFKey,this.key,(KeyField) this.db.getField(pointerField));
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
		String toDelete = JBaseDialog.readExisting("Field to Delete: ",this.allChildren(), "*** That field does not exist! ***", false);
		if (toDelete == null) {return false;}

		try {
			this.db.getField(toDelete).deleteField();
		} catch (JBaseException ex) {
			System.out.println(ex.getMessage()+"\n");
			return false;
		}

		return true;
	}



	/**
	 * Add more rows to the key field
	 */
	private void resizeKey() {
		Integer toAdd = JBaseDialog.readIntMin("Number of Rows to Add: ","*** Invalid Number (Must be > 0)! ***", 1, true);
		try {
			this.key.resize(toAdd);
		} catch (JBaseException ex) {
			System.out.println(ex.getMessage()+"\n");
		}
	}


	/**
	 * Select a record from a key field
	 *
	 * @param db the database of the field
	 * @param key The key field to get a record from
	 * @param prompt Field to display for the prompt
	 * @param loop If true, loop until the user selects a valid record. Otherwise, return an error;
	 * @return row of the record, or -1 on error
	 */
	private static int getRecord(Database db, KeyField key, String prompt, boolean loop) {

		if (key.inUse() <= 0) {return -1;}

		//Print out the records to the screen
		System.out.println("=== "+key.getName()+" ===");
		KeyDialog d = new KeyDialog(db,key);
		d.viewRecords();

		//Get a valid row from the records
		System.out.println("");
		int pRow = JBaseDialog.readInt(prompt+" Row: ");
		while(!key.isValidRow(pRow)) {
			System.out.println("*** Invalid row '"+pRow+"'! ***");
			if (!loop) {return -1;}
			pRow = JBaseDialog.readInt(prompt+": ");
		}

		return pRow;
	}




	/**
	 * Create a new record in the database
	 */
	private void newRecord() {

		//First ask for the key (make sure it is unique)
		String keyv = JBaseDialog.readNotNull(this.key.getName()+": ", true);
		int row;
		try {
			row = ((KeyField<String>) this.key).<String>insert(keyv);
		} catch (JBaseException ex) {
			System.out.println("*** "+ex.getMessage()+" ***\n");
			return;
		}

		//Now get all items
		ChildField allKids[] = this.key.allChildren();
		for (ChildField child: allKids) {
			Field f = child.toField();
			if (f.getType() == FieldType.ITEM) {
				putItem((ItemField) f, row);
			} else if (f.getType() == FieldType.FOREIGN_KEY) {
				putForeignKey((ForeignKeyField) f, row);
			}
		}

	}



	/**
	 * Put a value inside an item field
	 * @param item The item field to put
	 * @param row The row to put the new value
	 */
	private void putItem(ItemField item, int row) {
		String value = JBaseDialog.readNotNull(item.getName()+": ",true);
		try {
			item.<String>put(row,value);
		} catch (JBaseException ex) {
			System.out.println("*** "+ex.getMessage()+" ***");
		}
	}


	/**
	 * Put a value inside a foreign key field
	 * @param fkey The foreign key field to put
	 * @param row The row to put the new value
	 */
	private void putForeignKey(ForeignKeyField fkey, int row) {

		int pRow = getRecord(this.db, (KeyField) fkey.getPoint().toField(), fkey.getName(), true);

		try {
			fkey.put(row,new Integer(pRow));
		} catch (JBaseException ex) {
			System.out.println("*** "+ex.getMessage()+" ***");
		}
	}




	/**
	 * Delete a record from the database
	 */
	private void deleteRecord() {
		int pRow = getRecord(this.db, (KeyField) this.key, this.key.getName(), false);
		if (pRow < 0) {return;}

		try {
			this.key.delete(this.key.get(pRow));
		} catch (JBaseException ex) {
			System.out.println("*** "+ex.getMessage()+" ***");
		}

	}



	/**
	 * Edit an existing record in the database
	 */
	private void editRecord() {
		int row = getRecord(this.db, (KeyField) this.key, this.key.getName(), true);

		//Edit all of the children
		ChildField allKids[] = this.key.allChildren();
		for (ChildField child: allKids) {
			Field f = child.toField();
			if (f.getType() == FieldType.ITEM) {
				putItem((ItemField) f, row);
			} else if (f.getType() == FieldType.FOREIGN_KEY) {
				putForeignKey((ForeignKeyField) f, row);
			}
		}
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

				//Get the key value
				table[tableRow][0] = Integer.toString(row);
				table[tableRow][1] = this.key.get(row).toString();

				//Get all of the children values
				for (int i = 0; i < children.length; ++i) {
					String str = "(Null)";
					try {
						str = String.valueOf(children[i].toField().get(row));
					} catch (JBaseException ex) {}
					table[tableRow][i+2] = str;
				}
				tableRow+=1;
			}
		} catch (JBaseEndOfList eol) {/* Don't throw an error */
		} catch (JBaseException ex) {
			System.out.println(ex.getMessage()+"\n");
		}

		//Figure out the biggest entry in each column
		int bigCol[] = new int[children.length+2];
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
