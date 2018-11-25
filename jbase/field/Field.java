package jbase.field;

import jbase.database.Database;
import jbase.exception.*;
import jbase.acl.*;

import java.util.ArrayList;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.UUID;


/**
 * Represents a field in a JBase database
 * @author Bryan McClain
 */
public abstract class Field<T extends Serializable> implements Serializable, JBaseField {

	private final String name;			// Name of this field
	private final FieldType type;		// Type of this field
	protected final UUID uuid;			// Unique UUID for the field (Used by delete method)
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
		this.uuid = UUID.randomUUID();
	}


	/**
	 * Convert a JBase field interface into an actual field object
	 * @return Field object
	 */
	public Field toField() {
		return this;
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
	 * Get the class for the type of data stored in this field.
	 *  This uses some fancy reflection found on Stack Overflow.
	 *  See {@linktourl https://stackoverflow.com/questions/15084670/how-to-get-the-class-of-type-variable-in-java-generics}
	 *
	 * @return Class for the data stored in this field
	 */
	public Class<T> getDataType() {
        ParameterizedType parameterizedType = (ParameterizedType) getClass()
                .getGenericSuperclass();

        @SuppressWarnings("unchecked")
        Class<T> ret = (Class<T>) parameterizedType.getActualTypeArguments()[0];
        return ret;
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





	/**
	 * Delete this field from the database
	 * @throws JBaseFieldActionDenied User doesn't have permission to delete this field
	 */
	public void deleteField() throws JBaseFieldActionDenied {
		if (!this.db.getACL().canDo(this,FieldAction.DELETE_FIELD)) {
			throw new JBaseFieldActionDenied(db.currentUser(),this,FieldAction.DELETE_FIELD);
		}
		deleteInternal();
	}


	/**
	 * Internal delete method for the field
	 */
	protected abstract void deleteInternal();


	/**
	 * Compare the UUID of two field objects
	 * @param key The UUID to test
	 * @return True if the UUID's are equal, false if not
	 */
	public boolean validateUUID(UUID key) {
		return this.uuid.equals(key);
	}

}
