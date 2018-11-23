package jbase.field;


/**
 * Represents a field that has a parent (or owner) field
 * @author Bryan McClain
 */
public interface ChildField {


	/**
	 * Resize the child field to match the depth of the parent
	 * @param parent The parent field for this child
	 */
	public void resize(ParentField parent);

}
