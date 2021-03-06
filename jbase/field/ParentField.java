package jbase.field;


/**
 * Interface for a field that can accept children
 * @author Bryan McClain
 */
public interface ParentField extends JBaseField {

	/**
	 * Add a child to the children in this field
	 * @param child The child field to add
	 */
	public void addChild(ChildField child);


	/**
	 * Remove a child from the children in this field
	 * @param child The child field to remove
	 */
	public void deleteChild(ChildField child);


	/**
	 * Get the list of all children for this field
	 * @return Array of children fields
	 */
	public ChildField[] allChildren();


	/**
	 * Get the depth of this field (number of rows stored)
	 * @return Depth
	 */
	public int getDepth();
}
