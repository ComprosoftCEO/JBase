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
	public final K key;
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
	 */
	public BSTNode(K key, V value, BSTNode<K,V> left, BSTNode<K,V> right) {
		this.key = key;
		this.value = value;
		this.left = left;
		this.right = right;
	}
}
