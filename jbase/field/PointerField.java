package jbase.field;

/**
 * Interface for a field that points to another field
 * @author Bryan McClain
 */
public interface PointerField extends JBaseField {

	/**
	 * Get the key field that this field points to.
	 *  Only used by foreign keys.
	 * @return Point field, or null if it doesn't exist
	 */
	public PointableField getPoint();

}
