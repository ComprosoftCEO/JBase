package jbase.field;

import jbase.database.*;
import jbase.exception.*;

import java.io.Serializable;


/**
 * Field where each entry is unique and searchable
 * @author Bryan McClain
 */ 
public final class KeyField<T extends Comparable<T> & Serializable>
	extends Field<T> {

	private BSTNode<T,Integer> root = null;

	/**
	 * Create a new key field
	 *
	 * @param db Field database
	 * @param name The name of this field
	 * @param depth Initial number of rows in the field
	 */
	public KeyField(Database db, String name, int depth) {
		super(db,name,FieldType.KEY,depth);
	}




	/**
	 * Inserts a new value into the key field
	 * @param val The value to insert into the field
	 *
	 * @throws JBaseFieldActionDenied User does not have permission to execute this action
	 * @throws JBaseDuplicateData Cannot insert duplicate data into a key field
	 */
	public void insert(T val)
	throws JBaseFieldActionDenied, JBaseDuplicateData {
		if (!db.getACL().canDo(this,FieldAction.INSERT)) {
			throw new JBaseFieldActionDenied(db.currentUser(),this,FieldAction.INSERT);
		}

		
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
		return null;
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
		return 0;
	}



	/**
	 * Iterate over the sorted field, and go to the next value
	 * @param startRow The row to start at (or a negative number to start at the root)
	 * @return The next row
	 *
	 * @throws JBaseFieldActionDenied User doesn't have permission to execute this action
	 * @throws JBaseBadRow Row given that is greater than or equal to the depth
	 */
	public int next(int startRow)
	  throws JBaseFieldActionDenied, JBaseBadRow {
		return 0;
	}


	/**
	 * Iterate over the sorted field, and go to the previous value
	 * @param startRow The row to start at (or a negative number to start at the root)
	 * @return The previous row
	 *
	 * @throws JBaseFieldActionDenied User doesn't have permission to execute this action
	 * @throws JBaseBadRow Row given that is greater than or equal to the depth
	 */
	public int pre(int startRow)
	  throws JBaseFieldActionDenied, JBaseBadRow {
		return 0;
	}

}
