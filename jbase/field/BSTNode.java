package jbase.field;

import java.io.Serializable;

/**
 * Single node in a Binary Search Tree
 * @author Bryan McClain
 */
public class BSTNode<K extends Comparable<K> & Serializable,
					 V extends Serializable> implements Serializable {

	//Yes, these are public on purpose
	public BSTNode<K,V> left = null;
	public BSTNode<K,V> right = null;
	public BSTNode<K,V> parent = null;
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
				if (this.left != null) {currentNode = this.left;}
				else {this.left = newNode; return true;}
			}
			else if (cmp < 0) {
				/* Insert greater than */
				if (this.right != null) {currentNode = this.right;}
				else {this.right = newNode; return true;}
			
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
	 * @param toDelete The value to delete
	 *
	 */
	public boolean delete(K toDelete) {
		return false;
	}
}
