package jbase.field;

import java.io.Serializable;

/**
 * Convert a JBase field interface into an actual field object
 * @author Bryan McClain
 */
public interface JBaseField {


	/**
	 * Convert a JBase field interface into an actual field object
	 * @return Field object
	 */
	public Field toField();

}
