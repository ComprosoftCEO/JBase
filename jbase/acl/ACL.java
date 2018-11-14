package jbase.acl;

import jbase.database.*;
import jbase.field.*;
import jbase.exception.*;

import java.util.HashMap;

public class ACL {	

	private HashMap<DatabaseAction,PermissionType> database;				// Database permissions
	private HashMap<FieldAction, PermissionType> global;					// Global field permissions
	private HashMap<Field,HashMap<FieldAction,PermissionType>> field;		// Field specific permissions

	private Database db;		// Parent Database
	private User user;			// Parent user

	/**
	 * Construct a new Access Control List
	 * @param db The database for this ACL
	 * @param user The user for this ACL
	 */
	public ACL(Database db, User user) {
		this.user = user;
		this.db = db;
	}



	/**
	 * Test if a user can perform a given database action
	 * @param action The action to test
	 * @return True if the user can perform the action, false otherwise
	 */
	public boolean canDo(DatabaseAction action) {

		//All database actions have an implicit "Deny" action
		PermissionType type = database.get(action);
		if ((type == null) || (type != PermissionType.ALLOW)) {
			return false;
		} else {
			return true;
		}
	}



	/**
	 * Test if a user can perform a given field action
	 * @param field The field trying to do this action
	 * @param action The action to test
	 * @return True if the user can perform the action, false otherwise
	 */
	public boolean canDo(Field field, FieldAction action) {

		PermissionType globalType = global.get(action);
		PermissionType fieldType = null;
		PermissionType type = null;

		//Extract a local field type (if it exists)
		HashMap<FieldAction,PermissionType> fieldMap = this.field.get(field);
		if (fieldMap != null) {fieldType = fieldMap.get(action);}


		// Global field actions have an implicit "Deny"
		if ((globalType == null) || (globalType == PermissionType.NONE)) {
			globalType = PermissionType.DENY;
		}
		
		//Local overrides the global type
		if ((fieldType != null) && (fieldType != PermissionType.NONE)) {
			type = fieldType;	//OVerride the global type
		} else {
			type = globalType;	//Use the global type
		}

		if (type == PermissionType.DENY) {return false;}
		else {return true;}
	}




	/**
	 * Update a given database permission for this ACL
	 * @param action The database action to perform
	 * @param type Allow, Deny, or ignore this action
	 */
	public void setPermission(DatabaseAction act, PermissionType type) {

	}

	public void setPermission(FieldAction act, PermissionType type) {

	}

	public void setPermission(Field field, FieldAction act, PermissionType type) {

	}

}
