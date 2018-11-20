package jbase.field;

import jbase.database.*;
import jbase.exception.*;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Field where each entry is unique and searchable
 * @author Bryan McClain
 */ 
public final class KeyField<T extends Comparable<T> & Serializable> extends Field<T> {

	private BSTNode<T,Integer> root;				// Stores the BTree structure
	private ArrayList<BSTNode<T,Integer>> values;	// Each BSTNode stores index to itself in the list
	private BSTNode<T,Integer> nextSpot;			// Where to insert the next item (or null if not in use)



	/**
	 * Create a new key field
	 *
	 * @param db Field database
	 * @param name The name of this field
	 * @param depth Initial number of rows in the field
	 */
	public KeyField(Database db, String name, int depth) {
		super(db,name,FieldType.KEY);

		this.root = null;
		this.values = new ArrayList<BSTNode<T,Integer>>(depth);


		//Initialize the nodes in the tree
		//  When not in use, trees serve as a linked list
		this.nextSpot = new BSTNode(null,values.size()-1);
		values.add(values.size()-1,nextSpot);
		for (int i = values.size()-2; i >= 0; ++i) {
			BSTNode<T,Integer> node = new BSTNode<T,Integer>(null,i);
			this.values.add(i,node);
			this.nextSpot = nextSpot.pushBack(node);
		}
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
		this.values.ensureCapacity(this.getDepth() + toAdd);
	}



	/**
	 * Inserts a new value into the key field
	 * @param val The value to insert into the field
	 *
	 * @throws JBaseFieldActionDenied User does not have permission to execute this action
	 * @throws JBaseDuplicateData Cannot insert duplicate data into a key field
	 * @throws JBaseOutOfMemory No more space to insert any more values
	 */
	public void insert(T val)
	  throws JBaseFieldActionDenied, JBaseDuplicateData, JBaseOutOfMemory {
		if (!db.getACL().canDo(this,FieldAction.INSERT)) {
			throw new JBaseFieldActionDenied(db.currentUser(),this,FieldAction.INSERT);
		}

		//Make sure I have space to store this value
		if (nextSpot == null) {
			throw new JBaseOutOfMemory(this);
		}

		//Get the next value from the linked list
		BSTNode<T,Integer> newNode = nextSpot;
		nextSpot = nextSpot.pop();

		//Set the values inside the node
		newNode.key = val;
		newNode.left = null;
		newNode.right = null;

		//When to set up the root
		if (root == null) {
			root = newNode;
		} else {

			//Do a BST Search to insert the key
			if (!root.insert(newNode)) {
				//Undo the update to the list
				nextSpot = nextSpot.pushBack(newNode);
				throw new JBaseDuplicateData(this);
			}
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
		if (!db.getACL().canDo(this,FieldAction.GET)) {
			throw new JBaseFieldActionDenied(db.currentUser(),this,FieldAction.GET);
		}

		if (row < 0 || row >= values.size()) {
			throw new JBaseBadRow(this,row);
		}

		return values.get(row).key;
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

		//Search the binary tree
		BSTNode<T,Integer> node;
		if ((root == null) || ((node = root.find(val)) == null)) {
			throw new JBaseDataNotFound(this);
		} 

		//Extract the value from the linked-list
		return node.value;
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
		if (!db.getACL().canDo(this,FieldAction.ITERATE)) {
			throw new JBaseFieldActionDenied(db.currentUser(),this,FieldAction.ITERATE);
		}

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
		if (!db.getACL().canDo(this,FieldAction.ITERATE)) {
			throw new JBaseFieldActionDenied(db.currentUser(),this,FieldAction.ITERATE);
		}

		return 0;
	}

}
