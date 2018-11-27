package jbase.ui;

import jbase.field.*;
import jbase.database.*;
import jbase.exception.*;
import jbase.acl.*;

import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

/**
 * Dialog that allows you to edit the Access Control List of a given user
 * @author Bryan McClain
 */
public class ACLDialog implements JBaseDialog {

	private final Database db;
	private final String user;
	private final ACL acl;

	/**
	 * Create a new ACL Dialog
	 * @param db The database for this dialog
	 * @param user The user to edit the ACL for
	 * @throws JBaseUserNotFound Invalid user passed to the dialog
	 */
	public ACLDialog(Database db, String user) throws JBaseUserNotFound {
		this.db = db;
		this.user = user;
		this.acl = this.db.getUserACL(user);
	}


	/**
	 * Show the dialog for this interface
	 */
	public void showDialog() {
		while(runMenu());
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
	 * Print the complete list of ACL permissions
	 */
	private void printACL() {

		//1. Print the database permissions
		HashMap<DatabaseAction,PermissionType> database = this.acl.getDatabasePermissions();
		if (database.size() <= 0) {
			System.out.println("Database Permissions: None");
		} else {
			System.out.println("Database Permissions:");
			JBaseDialog.printMap(database,true);
			System.out.println("");
		}

		//2. Print the global field permissions
		HashMap<FieldAction,PermissionType> global = this.acl.getGlobalPermissions();
		if (global.size() <= 0) {
			System.out.println("Global Field Permissions: None");
		} else {
			System.out.println("Global Field Permissions:");
			JBaseDialog.printMap(global,true);
			System.out.println("");
		}

		//3. Print out any field specific permissions
		Field[] allFields = this.db.allFields();
		for (Field f : allFields) {
			HashMap field = this.acl.getFieldPermissions(f);
			if (field.size() > 0) {
				System.out.println("\n"+f.getName()+" Permissions:");
				JBaseDialog.printMap(field,true);
			}
		}

	}


	/**
	 * Run the main menu for this dialog
	 * @return True if to run again, false if to stop
	 */
	public boolean runMenu() {
		
		//Print out the basic information
		System.out.println("\n=== ACL For "+this.user+" ===\n");
		printACL();

		//Print out the commands
		System.out.println("\n D - Set database permission");
		System.out.println(" G - Set global field permission");
		System.out.println(" F - Edit field specific permission");
		System.out.println(" Q - Quit\n");

		//Run the menu
		boolean running = true;
		while(running) {

			String line = JBaseDialog.readNotNull("> ",true);
			switch(line.toUpperCase()) {
				case "Q": return false;
				case "D": databasePermission(); break;
				case "G": globalPermission(); break;
				case "F": if(fieldPermission()) {break;} else {continue;}
				default: 
					System.out.println("Unknown command '"+line+"'");
					continue;
			}

			running = false;
		}

		return true;
	}



	/**
	 * Update a database permission
	 */
	private void databasePermission() {

		//Ask the user which permission
		DatabaseAction act = JBaseDialog.readEnum(DatabaseAction.class,"\nPick Database Action: ","Action: ", true);
		PermissionType type = JBaseDialog.readEnum(PermissionType.class,"\nPick Permission Type: ","Type: ",true);

		try {
			this.acl.setPermission(act,type);
		} catch (JBaseException ex) {
			System.out.println("*** "+ex.getMessage()+" ***");
		}
	}




	/**
	 * Update a global field permission
	 */
	private void globalPermission() {

		//Ask the user which permission
		FieldAction act = JBaseDialog.readEnum(FieldAction.class,"\nPick Global Field Action: ","Action: ", true);
		PermissionType type = JBaseDialog.readEnum(PermissionType.class,"\nPick Permission Type: ","Type: ",true);

		try {
			this.acl.setPermission(act,type);
		} catch (JBaseException ex) {
			System.out.println("*** "+ex.getMessage()+" ***");
		}
	}



	/**
	 * Update a field specific permission
	 * @return If true, redraw the menu
	 */
	private boolean fieldPermission() {

		Set<String> allFields = this.allFields();
		if (allFields.size() <= 0) {
			System.out.println("*** No fields in the database ***");
			return false;
		}

		//Print out the list of all fields in the database
		System.out.println("All Fields: ");
		JBaseDialog.printCollection(this.allFields(), true);
		System.out.println("");

		String field = JBaseDialog.readExisting("Field: ", this.allFields(), "*** That field does not exist ***",true);
		Field f = this.db.getField(field);

		while(editFieldACL(f));
		return true;
	}



	/**
	 * Edit the ACL permissions for a single field.
	 *  This functionality is too small to use a full blown class.
	 *
	 * @param field The field to edit permissions for
	 * @return True if to redraw again, false to quit
	 */
	private boolean editFieldACL(Field field) {

		//Print out all of the permissions
		System.out.println("\n=== Edit "+field.getName()+" Permissions ===");
		HashMap fmap = this.acl.getFieldPermissions(field);
		if (fmap.size() > 0) {JBaseDialog.printMap(fmap,true);}
		System.out.println("");

		//Print out the list of commands
		System.out.println(" E - Edit Field Permission");
		System.out.println(" Q - Quit\n");

		//Read commands
		boolean running = true;
		while(running) {
			
			String line = JBaseDialog.readNotNull("> ",true);
			switch(line.toUpperCase()) {
				case "Q": return false;
				case "E": fieldPermission(field); break;
				default: 
					System.out.println("Unknown command '"+line+"'");
					continue;
			}

			running = false;
		}
		return true;
	}


	/**
	 * Update a field specific permission
	 * @param field The field to update permissions for
	 */
	private void fieldPermission(Field field) {

		//Ask the user which permission
		FieldAction act = JBaseDialog.readEnum(FieldAction.class,"\nPick Field Action: ","Action: ", true);
		PermissionType type = JBaseDialog.readEnum(PermissionType.class,"\nPick Permission Type: ","Type: ",true);

		try {
			this.acl.setPermission(field,act,type);
		} catch (JBaseException ex) {
			System.out.println("*** "+ex.getMessage()+" ***");
		}
	}
}
