package jbase.field;


/**
 * Represents a field that can be pointed to by a foreign key
 * @author Bryan McClain
 */
public interface PointableField extends JBaseField {

	/**
	 * Test if the given row is valid
 	 * @param row The row to test
	 */
	public boolean isValidRow(int row);


	/**
	 * Add a pointer field to the list of fields that point to this field
	 * @param pointer The field to add to the list
	 */
	public void addPointer(PointerField pointer);


	/**
	 * Remove a pointer field from the list of fields that point to this field
	 * @param pointer The pointer to remove from the list
	 */
	public void deletePointer(PointerField pointer);


	/**
	 * Get the array of all pointers for this field
	 * @return Array of pointer fields
 	 */
	public PointerField[] allPointers();
}
