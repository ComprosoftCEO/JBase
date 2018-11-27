package jbase.ui;

import jbase.field.*;
import jbase.database.*;
import jbase.exception.*;
import jbase.acl.*;

import java.util.HashMap;

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
				System.out.println(f.getName()+" Permissions:");
				JBaseDialog.printMap(field,true);
				System.out.println("");
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
		System.out.println(" Q - Quit");

		//Run the menu
		boolean running = true;
		while(running) {

			String line = JBaseDialog.readNotNull("> ",true);

			switch(line.toUpperCase()) {
				case "Q": return false;
				case "D": break;
				case "G": break;
				case "F": continue;
				default: 
					System.out.println("Unknown command '"+line+"'");
					continue;
			}

			running = false;
		}

		return true;
	}

}