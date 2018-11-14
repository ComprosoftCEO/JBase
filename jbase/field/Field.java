package jbase.field;

import jbase.database.Database;
import jbase.exception.*;
import jbase.acl.*;

import java.util.ArrayList;


/**
 * Represents a field in a JBase database
 * @author Bryan McClain
 */
public abstract class Field<T> {

	private final Database db;			// Database object for this field
	private final String name;			// Name of this field
	private final FieldType type;		// Type of this field
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
	public final String getName() {
		return this.name;
	}


	/**
	 * Get the type of this field (key, item, foreign key, etc.)
	 * @return Field Type
	 */
	public final FieldType getType() {
		return this.type;
	}

	/**
	 * Get the depth of this field (number of rows stored)
	 * @return Depth
	 */
	public final int getDepth() {
		return this.values.size();
	}

	/**
	 * Add more rows to this field
	 * @param toAdd Number of rows to add
	 * @throws JBaseException, JBasePermissionException
	 */
	public final void resize(int toAdd) throws JBaseException, JBasePermissionException {
		if (toAdd <= 0) {throw new JBaseException("toAdd <= 0");}
		if (!db.getACL().canDo(this,FieldAction.RESIZE_FIELD)) {
		//	throw new JBasePermissionException(FieldAction.RESIZE_FIELD, this.db.currentUser());
		}
	
		//Resize the arraylist to the new capacity
		this.values.ensureCapacity(this.getDepth() + toAdd);
	}


	public abstract void insert(T val);
	public abstract void delete(T val);
	public abstract T get(int row);
	public abstract void put(int row, T val);
	public abstract int find(T val);
	public abstract int next(int startRow);
	public abstract int pre(int startRow);
}
