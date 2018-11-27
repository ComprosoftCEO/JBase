package jbase.ui;

import jbase.database.*;
import jbase.exception.*;
import java.util.Set;


/**
 * Dialog for creating and deleting users in the database
 * @author Bryan McClain
 */
public class UserDialog implements JBaseDialog {

	private final Database db;


	/**
	 * Create a new User Dialog
	 * @param db The database for this dialog
	 */
	public UserDialog(Database db) {
		this.db = db;
	}


	/**
	 * Show the dialog for this interface
	 */
	public void showDialog() {
		while(runMenu());
	}

	/**
	 * Print out all users to the terminal
	 */
	private void printUsers() {
		Set<String> allUsers = this.db.allUsers();
		int i= 1;
		for (String u : allUsers) {
			System.out.print(i+": "+u);
			
			if (u.equals(this.db.currentUser())) {
				System.out.print(" (Current User)");
			}

			System.out.println("");
		}
	}



	/**
	 * Run the main menu for this dialog
	 * @return True if to run again, false if not
	 */
	private boolean runMenu() {
		System.out.println("\n=== Users Dialog ===");
		
		Set<String> allUsers = this.db.allUsers();
		System.out.println("Users: ");
		this.printUsers();

		//Print commands
		System.out.println("\n N - New User");
		if (allUsers.size() > 0) {
			System.out.println(" D - Delete User");
			System.out.println(" E - Edit User Permissions");
		}
		System.out.println(" Q - Quit\n");	

		//Read in commands and do them
		boolean running = true;
		while(running) {
			String line = JBaseDialog.readNotNull("> ",true);
			switch(line.toUpperCase()) {
				case "Q": return false;
				case "N": if (newUser()) {break;} else {continue;}
				case "D": if (allUsers.size() > 0) {if (deleteUser()) {break;} else {continue;}}
				case "E": if (allUsers.size() > 0) {if (editUser()) {break;} else {continue;}}
				default:
					System.out.println("Unknown command '"+line+"'");
					continue;
			}

			running = false;
		}

		return true;
	}


	/**
	 * Create a new user in the database
	 * @return True if to redraw, false otherwise
	 */
	private boolean newUser() {

		String username = JBaseDialog.readUnique("New User: ",this.db.allUsers(),"*** That user already exists! ***", true);
		String password = JBaseDialog.readNotNull("Password: ", true);

		try {
			this.db.newUser(username,password);
		} catch (JBaseException ex) {
			System.out.println("*** "+ex.getMessage()+" ***");
			return false;
		}
		return true;
	}


	/**
	 * Delete an existing user in the database
	 * @return True if to redraw, false otherwise
	 */
	private boolean deleteUser() {

		String username = JBaseDialog.readExisting("User to delete: ", this.db.allUsers(), "*** That user doesn't exist! ***", false);
		if (username == null) {return false;}

		try {
			this.db.deleteUser(username);
		} catch (JBaseException ex) {
			System.out.println("*** "+ex.getMessage()+" ***");
			return false;
		}
		return true;
	}


	/**
	 * Edit an existing user in the database
	 * @return True if to redraw, false otherwise
	 */
	private boolean editUser() {
		String username = JBaseDialog.readExisting("User to edit: ", this.db.allUsers(), "*** That user doesn't exist! ***", true);
		try {
			(new ACLDialog(this.db,username)).showDialog();
		} catch (JBaseException ex) {
			System.out.println("*** "+ex.getMessage()+" ***"); /* Should not happen */
			return false;
		}
		return true;
	}

}
