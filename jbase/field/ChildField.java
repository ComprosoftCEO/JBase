package jbase.field;


/**
 * Represents a field that has a parent (or owner) field
 * @author Bryan McClain
 */
public interface ChildField extends JBaseField {


	/**
	 * Resize the child field to match the depth of the parent
	 * @param parent The parent field for this child
	 */
	public void resize(ParentField parent);


	/**
	 * Get the field that owns this field.
	 *  Only used by items and foreign keys
	 * @return Owner Key field, or null if it doesn't exist
	 */
	public abstract ParentField getOwner();
}
