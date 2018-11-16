package jbase.field;

import java.io.Serializable;

/**
 * Single node in a Red-Black Tree
 * @author Bryan McClain
 */
public class RBNode<KEY_T implements Comparable<KEY_T>, VAL_T> implements Serializable {

	public RBNode left = null;
	public RBNode right = null;
	public final KEY_T key;
	public VAL_T value;


	public RBNode(KEY_T key, VAL_T value) {
		this.key = key;
		this.value = value;
	}

	public RBNode(KEY_T key, VAL_T value, RBNode left, RBNode, right) {
		this.key = key;
		this.value = value;
		this.left = left;
		this.right = right;
	}
}
