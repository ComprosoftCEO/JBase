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
}
