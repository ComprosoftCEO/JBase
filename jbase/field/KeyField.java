package jbase.field;

import jbase.database.*;
import jbase.exception.*;

import java.io.Serializable;
import java.util.Stack;
import java.util.TreeMap;
import java.util.Map.Entry;

/**
 * Field where each entry is unique and searchable
 * @author Bryan McClain
 */ 
public final class KeyField<T extends Comparable<T> & Serializable> extends Field<T> {


	//Look up data by either row or value
	private TreeMap<Integer,T> by_row;		// Search for a value using the row
	private TreeMap<T,Integer> by_value;	// Search for a row using the value
	private Stack<Integer> nextRow;			// List of free rows
	private int depth;


	/**
	 * Create a new key field
	 *
	 * @param db Field database
	 * @param name The name of this field
	 * @param depth Initial number of rows in the field
	 */
	public KeyField(Database db, String name, int depth) {
		super(db,name,FieldType.KEY);

		this.by_row = new TreeMap<Integer,T>();
		this.by_value = new TreeMap<T,Integer>();
		this.nextRow = new Stack<Integer>();
		this.depth = depth;

		//Initilize the list of rows
		for (int i = 0; i < this.depth; ++i) {
			this.nextRow.push(i);
		}
	}



	/**
	 * Get the depth of this field (number of rows stored)
	 * @return Depth
	 */
	public int getDepth() {
		return this.depth;
	}


	/**
	 * Add more rows to this field
	 * @param toAdd Number of rows to add
	 *
	 * @throws JBaseFieldActionDenied User doesn't have permission to execute this action
	 * @throws JBaseBadResize Invalid size passed to function
	 */
	public void resize(int toAdd) throws JBaseException, JBasePermissionException {
		if (!db.getACL().canDo(this,FieldAction.RESIZE_FIELD)) {
			throw new JBaseFieldActionDenied(this.db.currentUser(),this,FieldAction.RESIZE_FIELD);
		}
		if (toAdd <= 0) {throw new JBaseBadResize(this,toAdd);}
	
		//Resize the arraylist to the new capacity
		int oldDepth = this.depth;
		this.depth += toAdd;

		//Add the new rows to the stack
		for (int i = oldDepth; i < this.depth; ++i) {
			this.nextRow.push(i);
		}
	}


	/**
	 * Get the key field that owns this field.
	 * @return Owner Key field, or null if it doesn't exist
	 */
	public KeyField getOwner() {
		return null;	/* Keys don't have owners */
	}

	/**
	 * Get the key field that this field points to.
	 * @return Point field, or null if it doesn't exist
	 */
	public KeyField getPoint() {
		return null;	/* Keys don't have point */
	}



	/**
	 * Inserts a new value into the key field
	 * @param val The value to insert into the field
	 * @return The row of the newly inserted item
	 *
	 * @throws JBaseFieldActionDenied User does not have permission to execute this action
	 * @throws JBaseDuplicateData Cannot insert duplicate data into a key field
	 * @throws JBaseOutOfMemory No more space to insert any more values
	 */
	public int insert(T val)
	throws JBaseFieldActionDenied, JBaseDuplicateData, JBaseOutOfMemory {
		if (!db.getACL().canDo(this,FieldAction.INSERT)) {
			throw new JBaseFieldActionDenied(db.currentUser(),this,FieldAction.INSERT);
		}

		//Make sure I have space to store this value
		if (this.nextRow.empty()) {
			throw new JBaseOutOfMemory(this);
		}

		//Make sure the value doesn't already exist
		if (by_value.containsKey(val)) {
			throw new JBaseDuplicateData(this);
		}

		//Add to the two lists
		Integer row = this.nextRow.pop();
		by_row.put(row,val);
		by_value.put(val,row);
		return row;
	}


	/**
	 * Delete a value from the key field
	 * @param val The value to delete from the field
	 *
	 * @throws JBaseFieldActionDenied User doesn't have permission to execute this action
	 * @throws JBaseDataNotFound Data doesn't exist in the key field
	 */
	public void delete(T val)
	throws JBaseFieldActionDenied, JBaseDataNotFound {
		if (!db.getACL().canDo(this,FieldAction.DELETE)) {
			throw new JBaseFieldActionDenied(db.currentUser(),this,FieldAction.DELETE);
		}

		//Make sure the value actually exists to delete
		if (!this.by_value.containsKey(val)) {
			throw new JBaseDataNotFound(this);
		}

		Integer row = this.by_value.get(val);
		this.by_value.remove(val);
		this.by_row.remove(row);

		//Make the row available to use again
		this.nextRow.push(row);
	}



	/**
	 * Get a value stored at a given row in the field
	 * @param row The row to retrieve
	 * @return The value stored at the row (can be null)
	 *
	 * @throws JBaseFieldActionDenied User doesn't have permission to execute this action
	 * @throws JBaseBadRow Invalid row given to retrieve
	 */
	public T get(int row)
	  throws JBaseFieldActionDenied, JBaseBadRow {
		if (!db.getACL().canDo(this,FieldAction.GET)) {
			throw new JBaseFieldActionDenied(db.currentUser(),this,FieldAction.GET);
		}

		if (!this.by_row.containsKey(row)) {
			throw new JBaseBadRow(this,row);
		}

		return this.by_row.get(row);
	}



	/**
	 * Store a value stored at a given row in the key field
	 * @param row The row to store
	 * @param val The value stored at the row
	 *
	 * @throws JBaseBadFieldAction The field doesn't support this action
	 */
	public void put(int row, T val)
	  throws JBaseBadFieldAction {
		throw new JBaseBadFieldAction(this,FieldAction.PUT);
	}


	/**
	 * Find a value stored in the key field
	 * @param val The value to find
	 * @return Row where the value is stored
	 *
	 * @throws JBaseFieldActionDenied User doesn't have permission to execute this action
	 * @throws JBaseDataNotFound Data doesn't exist in the field
	 */
	public int find(T val)
	  throws JBaseFieldActionDenied, JBaseDataNotFound {
		if (!db.getACL().canDo(this,FieldAction.FIND)) {
			throw new JBaseFieldActionDenied(db.currentUser(),this,FieldAction.FIND);
		}

		//Search for the value
		if (!this.by_value.containsKey(val)) {
			throw new JBaseDataNotFound(this);
		} 

		return this.by_value.get(val);
	}



	/**
	 * Iterate over the sorted field, and go to the next value
	 * @param startRow The row to start at (or a negative number to start at the root)
	 * @return The next row
	 *
	 * @throws JBaseFieldActionDenied User doesn't have permission to execute this action
	 * @throws JBaseBadRow Bad row given
	 * @throws JBaseEndOfList Reached the end of the list
	 */
	public int next(int startRow)
	  throws JBaseFieldActionDenied, JBaseBadRow {
		if (!db.getACL().canDo(this,FieldAction.ITERATE)) {
			throw new JBaseFieldActionDenied(db.currentUser(),this,FieldAction.ITERATE);
		}

		//When to get the start
		if (startRow < 0) {
			Entry<T,Integer> entry = this.by_value.firstEntry();
			if (entry == null) {throw new JBaseEndOfList(this);}
			return entry.getValue();
		}

		//Validate the row
		if (!this.by_row.containsKey(startRow)) {
			throw new JBaseBadRow(this,startRow);
		}

		//Get the next entry, testing for the end of the list
		T value = this.by_row.get(startRow);
		Entry<T,Integer> entry = this.by_value.higherEntry(value);
		if (entry == null) {throw new JBaseEndOfList(this);}
		return entry.getValue();
	}


	/**
	 * Iterate over the sorted field, and go to the previous value
	 * @param startRow The row to start at (or a negative number to start at the root)
	 * @return The previous row
	 *
	 * @throws JBaseFieldActionDenied User doesn't have permission to execute this action
	 * @throws JBaseBadRow Row given that is greater than or equal to the depth
	 * @throws JBaseEndOfList Reached the end of the list
	 */
	public int pre(int startRow)
	  throws JBaseFieldActionDenied, JBaseBadRow, JBaseEndOfList {
		if (!db.getACL().canDo(this,FieldAction.ITERATE)) {
			throw new JBaseFieldActionDenied(db.currentUser(),this,FieldAction.ITERATE);
		}

		//When to get the start
		if (startRow < 0) {
			Entry<T,Integer> entry = this.by_value.lastEntry();
			if (entry == null) {throw new JBaseEndOfList(this);}
			return entry.getValue();
		}

		//Validate the row
		if (!this.by_row.containsKey(startRow)) {
			throw new JBaseBadRow(this,startRow);
		}

		//Get the next entry, testing for the end of the list
		T value = this.by_row.get(startRow);
		Entry<T,Integer> entry = this.by_value.lowerEntry(value);
		if (entry == null) {throw new JBaseEndOfList(this);}
		return entry.getValue();
	}

}
