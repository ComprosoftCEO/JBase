package jbase.field;

import jbase.database.Database;
import jbase.exception.*;
import jbase.acl.*;

import java.util.ArrayList;
import java.io.Serializable;


/**
 * Represents a field in a JBase database
 * @author Bryan McClain
 */
public abstract class Field<T extends Serializable> implements Serializable {

	private final String name;			// Name of this field
	private final FieldType type;		// Type of this field
	protected final Database db;		// Database object for this field
	protected ArrayList<T> values;		// Values stored in this field



	/**
	 * Construct a new field object
	 *
	 * @param db Field database
	 * @param name The name of this field
	 * @param type Type of this field (key, item, foreign key, etc.)
	 * @param depth Initial number of rows in the field
	 */
	public Field(Database db, String name, FieldType type, int depth) {
		this.db = db;
		this.name = name;
		this.type = type;
		this.values = new ArrayList<T>(depth);
	}


	/**
	 * Get the name of this field
	 * @return Name
	 */
	public String getName() {
		return this.name;
	}


	/**
	 * Get the type of this field (key, item, foreign key, etc.)
	 * @return Field Type
	 */
	public FieldType getType() {
		return this.type;
	}

	/**
	 * Get the depth of this field (number of rows stored)
	 * @return Depth
	 */
	public int getDepth() {
		return this.values.size();
	}


	/**
	 * Get the database object associated with this field
	 * @return Database
	 */
	public final Database getDatabase() {
		return this.db;
	}

	/**
	 * Add more rows to this field
	 * @param toAdd Number of rows to add
	 * @throws JBaseException, JBasePermissionException
	 */
	public void resize(int toAdd) throws JBaseException, JBasePermissionException {
		if (toAdd <= 0) {throw new JBaseException("toAdd <= 0");}
		if (!db.getACL().canDo(this,FieldAction.RESIZE_FIELD)) {
		//	throw new JBasePermissionException(FieldAction.RESIZE_FIELD, this.db.currentUser());
		}
	
		//Resize the arraylist to the new capacity
		this.values.ensureCapacity(this.getDepth() + toAdd);
	}



	/**
	 * Insert a new value into a key, automatically sorting the values
	 *
	 * @param val The value to insert
	 * @throws JBaseBadFieldAction The field doesn't support this action
	 * @throws JBaseFieldActionDenied User doesn't have permission to execute this action
	 * @throws JBaseDuplicateData Cannot insert duplicate data into a the field
	 * @throws JBaseOutOfMemory No more space to insert any more values
	 */
	public abstract void insert(T val)
	  throws JBaseBadFieldAction, JBaseFieldActionDenied, JBaseDuplicateData, JBaseOutOfMemory;


	/**
	 * Delete a value from the key, automatically sorting the values
	 *
	 * @param val The value to delete
	 * @throws JBaseBadFieldAction The field doesn't support this action
	 * @throws JBaseFieldActionDenied User doesn't have permission to execute this action
	 * @throws JBaseDataNotFound Data doesn't exist in the field
	 */
	public abstract void delete(T val)
	  throws JBaseBadFieldAction, JBaseFieldActionDenied, JBaseDataNotFound;


	/**
	 * Get a value stored at a given row in the field
	 * @param row The row to retrieve
	 * @return The value stored at the row (can be null)
	 *
	 * @throws JBaseBadFieldAction The field doesn't support this action
	 * @throws JBaseFieldActionDenied User doesn't have permission to execute this action
	 * @throws JBaseBadRow Invalid row given to retrieve
	 */
	public abstract T get(int row)
	  throws JBaseBadFieldAction, JBaseFieldActionDenied, JBaseBadRow;


	/**
	 * Store a value stored at a given row in the field
	 * @param row The row to store
	 * @param val The value stored at the row
	 *
	 * @throws JBaseBadFieldAction The field doesn't support this action
	 * @throws JBaseFieldActionDenied User doesn't have permission to execute this action
	 * @throws JBaseBadRow Invalid row given for storage
	 */
	public abstract void put(int row, T val)
	  throws JBaseBadFieldAction, JBaseFieldActionDenied, JBaseBadRow;


	/**
	 * Find a value stored in a field
	 * @param val The value to find
	 * @return Row where the value is stored
	 *
	 * @throws JBaseBadFieldAction The field doesn't support this action
	 * @throws JBaseFieldActionDenied User doesn't have permission to execute this action
	 * @throws JBaseDataNotFound Data doesn't exist in the field
	 */
	public abstract int find(T val)
	  throws JBaseBadFieldAction, JBaseFieldActionDenied, JBaseDataNotFound;

	/**
	 * Iterate over a sorted field, and go to the next value
	 * @param startRow The row to start at (or a negative number to start at the root)
	 * @return The next row
	 *
	 * @throws JBaseBadFieldAction The field doesn't support this action
	 * @throws JBaseFieldActionDenied User doesn't have permission to execute this action
	 * @throws JBaseBadRow Row given that is greater than or equal to the depth
	 */
	public abstract int next(int startRow)
	  throws JBaseBadFieldAction, JBaseFieldActionDenied, JBaseBadRow;


	/**
	 * Iterate over a sorted field, and go to the previous value
	 * @param startRow The row to start at (or a negative number to start at the root)
	 * @return The previous row
	 *
	 * @throws JBaseBadFieldAction The field doesn't support this action
	 * @throws JBaseFieldActionDenied User doesn't have permission to execute this action
	 * @throws JBaseBadRow Row given that is greater than or equal to the depth
	 */
	public abstract int pre(int startRow)
	  throws JBaseBadFieldAction, JBaseFieldActionDenied, JBaseBadRow;
}
