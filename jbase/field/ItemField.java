package jbase.field;

import jbase.exception.*;
import jbase.database.*;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents an "Item" in our database (field owned by a key)
 * @author Bryan McClain
 */
public class ItemField<T extends Serializable> extends Field<T> implements ChildField {

	protected final ParentField owner;
	protected ArrayList<T> values;


	/**
	 * Construct a new item field in the database.
	 *  Depth is inherited from the owner field.
	 *
	 * @param db The database for this field
	 * @param name The name of this field
	 * @param owner Parent that owns this field
	 */
	public ItemField(Database db, String name, ParentField owner) {
		super(db,name,FieldType.ITEM);
		this.owner = owner;
		this.values = new ArrayList<T>(owner.getDepth());
		owner.addChild(this);
	}


	/**
	 * Construct a new item field in the database.
	 *  Depth is inherited from the owner field.
	 *
	 *  This constructor is protected to allow sub-classes (foreign key) to have
	 *   item-like properties but still be a distinct field
	 *
	 * @param type The type of this field
	 * @param db The database for this field
	 * @param name The name of this field
	 * @param owner Key that owns this field
	 */
	protected ItemField(FieldType type, Database db, String name, ParentField owner) {
		super(db,name,type);
		this.owner = owner;
		this.values = new ArrayList<T>(owner.getDepth());
		owner.addChild(this);
	}


	/**
	 * Get the depth of this field (number of rows stored)
	 * @return Depth
	 */
	public int getDepth() {
		return this.values.size();
	}


	/**
	 * Add more rows to this field
	 *
	 * @param toAdd Number of rows to add
	 * @throws JBaseBadFieldAction The field doesn't support the resize action
	 */
	public void resize(int toAdd) throws JBaseBadFieldAction {
		throw new JBaseBadFieldAction(this,FieldAction.RESIZE_FIELD);
	}


	/**
	 * Resize the child field to match the depth of the parent
	 * @param parent The parent field for this child
	 */
	public void resize(ParentField parent) {
		if (this.owner != parent) {return;}
		this.values.ensureCapacity(parent.getDepth());
	}



	/**
	 * Get the key field that owns this field.
	 *  Only used by items and foreign keys
	 * @return Owner Key field, or null if it doesn't exist
	 */
	public ParentField getOwner() {
		return this.owner;
	}



	/**
	 * Insert a new value into a key, automatically sorting the values
	 *
	 * @param val The value to insert
	 * @return The row of the newly inserted item
	 * @throws JBaseBadFieldAction The field doesn't support this action
	 */
	public int insert(T val) throws JBaseBadFieldAction {
		throw new JBaseBadFieldAction(this,FieldAction.INSERT);
	}

	/**
	 * Delete a value from the key, automatically sorting the values
	 *
	 * @param val The value to delete
	 * @throws JBaseBadFieldAction The field doesn't support this action
	 */
	public void delete(T val) throws JBaseBadFieldAction {
		throw new JBaseBadFieldAction(this,FieldAction.DELETE);
	}


	/**
	 * Get a value stored at a given row in the field
	 * @param row The row to retrieve
	 * @return The value stored at the row (can be null)
	 *
	 * @throws JBaseBadFieldAction The field doesn't support this action
	 * @throws JBaseFieldActionDenied User doesn't have permission to execute this action
	 * @throws JBaseBadRow Invalid row given to retrieve
	 */
	public T get(int row)
	  throws JBaseBadFieldAction, JBaseFieldActionDenied, JBaseBadRow {
		if (!db.getACL().canDo(this,FieldAction.GET)) {
			throw new JBaseFieldActionDenied(db.currentUser(),this,FieldAction.GET);
		}

		if (row < 0 || row >= values.size()) {
			throw new JBaseBadRow(this,row);
		}

		return values.get(row);
	}


	/**
	 * Store a value stored at a given row in the field
	 * @param row The row to store
	 * @param val The value stored at the row
	 *
	 * @throws JBaseBadFieldAction The field doesn't support this action
	 * @throws JBaseFieldActionDenied User doesn't have permission to execute this action
	 * @throws JBaseBadRow Invalid row given for storage
	 */
	public void put(int row, T val)
	  throws JBaseBadFieldAction, JBaseFieldActionDenied, JBaseBadRow {
		if (!db.getACL().canDo(this,FieldAction.GET)) {
			throw new JBaseFieldActionDenied(db.currentUser(),this,FieldAction.GET);
		}

		if (row < 0 || row >= values.size()) {
			throw new JBaseBadRow(this,row);
		}

		values.set(row,val);
	}




	/**
	 * Find a value stored in a field
	 * @param val The value to find
	 * @return Row where the value is stored
	 *
	 * @throws JBaseBadFieldAction The field doesn't support this action
	 */
	public int find(T val) throws JBaseBadFieldAction {
		throw new JBaseBadFieldAction(this,FieldAction.FIND);
	}

	/**
	 * Iterate over a sorted field, and go to the next value
	 * @param startRow The row to start at (or a negative number to start at the root)
	 * @return The next row
	 *
	 * @throws JBaseBadFieldAction The field doesn't support this action
	 */
	public int next(int startRow) throws JBaseBadFieldAction {
		throw new JBaseBadFieldAction(this,FieldAction.ITERATE);
	}


	/**
	 * Iterate over a sorted field, and go to the previous value
	 * @param startRow The row to start at (or a negative number to start at the root)
	 * @return The previous row
	 *
	 * @throws JBaseBadFieldAction The field doesn't support this action
	 */
	public int pre(int startRow) throws JBaseBadFieldAction {
		throw new JBaseBadFieldAction(this,FieldAction.ITERATE);
	}


	/**
	 * Internal delete method
	 */
	protected void deleteInternal() {
		this.db.deleteField(this,this.uuid);
		this.owner.deleteChild(this);
	}
}
