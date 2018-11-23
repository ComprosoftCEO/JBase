package jbase.field;

import java.io.Serializable;

/**
 * Single node in a Binary Search Tree. Each node can either be a tree or an
 *  entry in a linked list
 *
 * @author Bryan McClain
 */
public class BSTNode<K extends Comparable<K> & Serializable,
					 V extends Serializable> implements Serializable {

	//Yes, these are public on purpose
	public BSTNode<K,V> left = null;
	public BSTNode<K,V> right = null;
	public BSTNode<K,V> parent = null;		// Also serves as next for linked-list
	public K key;
	public V value;


	/**
	 * Construct a new Binary Search Tree node
	 * @param key The key for this node (cannot change)
	 * @param value The value for this node
	 */
	public BSTNode(K key, V value) {
		this.key = key;
		this.value = value;
	}


	/**
	 * Construct a new Binary Search Tree node
	 * @param key The key for this node (cannot change)
	 * @param value The value for this node
	 * @param left Left node
	 * @param right Right node
	 * @param parent Parent node
	 */
	public BSTNode(K key, V value, BSTNode<K,V> left, BSTNode<K,V> right, BSTNode<K,V> parent) {
		this.key = key;
		this.value = value;
		this.left = left;
		this.right = right;
		this.parent = parent;
	}



	/**
	 * Push a node onto the linked-list version of BST Nodes
	 * @param other The other node to push
	 * @return The new head of the list
	 */
	public BSTNode<K,V> pushBack(BSTNode<K,V> other) {
		other.key = null;
		other.parent = this;
		return other;
	}

	/**
	 * Pop a node off of the linked-list version of the BST Nodes.
	 *  The current head becomes the free item on the list
	 *
	 * @return The new head of the list
	 */
	public BSTNode<K,V> pop() {
		return this.parent;
	}



	/**
	 * Insert another node into the Binary Search Tree
	 * @param newNode The node to insert into this tree
	 * @return True if the insert succeeded, false if the data already existed
	 */
	public boolean insert(BSTNode<K,V> newNode) {
	
		BSTNode<K,V> currentNode = this;
		while(true) {

			int cmp = currentNode.key.compareTo(newNode.key);
			if (cmp > 0) {
				/* Insert less than */
				if (this.left != null) {
					currentNode = this.left;
				} else {
					this.left = newNode;
					newNode.parent = this.left;
					return true;
				}
			}
			else if (cmp < 0) {
				/* Insert greater than */
				if (this.right != null) {
					currentNode = this.right;
				} else {
					this.right = newNode;
					newNode.parent = this.right;
					return true;
				}
			
			}
			else if (cmp == 0) {
				/* Already exists */
				return false;
			}
		}
	}

	/**
	 * Find a value in the binary search tree
	 * @param toFind The key to find
	 * @return The node found, or null on not found
	 */
	public BSTNode<K,V> find(K toFind) {

		//Keep searching left or right until you find the node
		BSTNode<K,V> currentNode = this;
		while(currentNode != null) {
			int cmp = currentNode.key.compareTo(toFind);
			if (cmp > 0) {currentNode = this.left;}
			else if (cmp < 0) {currentNode = this.right;}
			else {return currentNode;}
		}

		return null;	//Not found
	}


	/**
	 * Delete a node from the Binary Search Tree
	 * @param toDelete The node to delete (found using a call to find)
	 * @return The new node to replace the existing node
	 */
	public BSTNode<K,V> delete(BSTNode<K,V> toDelete) {

		//Case 1: Both children are null
		if ((toDelete.left == null) && toDelete.right == null) {
			


		}


		return false;
	}
}
