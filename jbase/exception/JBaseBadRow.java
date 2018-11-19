package jbase.exception;

import jbase.field.Field;

/**
 * Thrown when a bad row is passed into a get, put, next, or pre method
 * @author Bryan McClain
 */
public class JBaseBadRow extends JBaseFieldException {

	private final int row;		// Row that caused this exception

	/**
	 * Construct a new Bad Row Exception
	 * @param field The field that caused this exception
	 * @param row The bad row
	 */
	public JBaseBadRow(Field field, int row) {
		super(field, "Bad row given "
					+"(Row: "+row+", Field Depth: "+field.getDepth()+")");
		this.row = row;
	}


	/**
	 * Get the row that caused this exception
	 * @return Row
	 */
	public int getRow() {
		return this.row;
	}
}
