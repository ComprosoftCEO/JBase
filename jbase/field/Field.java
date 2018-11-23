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



	/**
	 * Construct a new field object
	 *
	 * @param db Field database
	 * @param name The name of this field
	 * @param type Type of this field (key, item, foreign key, etc.)
	 */
	public Field(Database db, String name, FieldType type) {
		this.db = db;
		this.name = name;
		this.type = type;
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
	public abstract int getDepth();


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
	 * @throws JBaseBadFieldAction The field doesn't support the resize action
	 * @throws JBaseFieldActionDenied User doesn't have permission to execute this action
	 * @throws JBaseBadResize Invalid size passed to function
	 */
	public abstract void resize(int toAdd)
	  throws JBaseBadFieldAction, JBaseFieldActionDenied, JBaseBadResize;


	/**
	 * Get the key field that owns this field.
	 *  Only used by items and foreign keys
	 * @return Owner Key field, or null if it doesn't exist
	 */
	public abstract KeyField getOwner();



	/**
	 * Get the key field that this field points to.
	 *  Only used by foreign keys.
	 * @return Point field, or null if it doesn't exist
	 */
	public abstract KeyField getPoint();


	/**
	 * Insert a new value into a key, automatically sorting the values
	 *
	 * @param val The value to insert
	 * @return The row of the newly inserted item
	 *
	 * @throws JBaseBadFieldAction The field doesn't support this action
	 * @throws JBaseFieldActionDenied User doesn't have permission to execute this action
	 * @throws JBaseDuplicateData Cannot insert duplicate data into a the field
	 * @throws JBaseOutOfMemory No more space to insert any more values
	 */
	public abstract int insert(T val)
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
	 * @throws JBaseEndOfList Reached the end of the list
	 */
	public abstract int next(int startRow)
	  throws JBaseBadFieldAction, JBaseFieldActionDenied, JBaseBadRow, JBaseEndOfList;


	/**
	 * Iterate over a sorted field, and go to the previous value
	 * @param startRow The row to start at (or a negative number to start at the root)
	 * @return The previous row
	 *
	 * @throws JBaseBadFieldAction The field doesn't support this action
	 * @throws JBaseFieldActionDenied User doesn't have permission to execute this action
	 * @throws JBaseBadRow Row given that is greater than or equal to the depth
	 * @throws JBaseEndOfList Reached the end of the list
	 */
	public abstract int pre(int startRow)
	  throws JBaseBadFieldAction, JBaseFieldActionDenied, JBaseBadRow, JBaseEndOfList;
}
