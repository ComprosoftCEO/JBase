package jbase.field;

import jbase.exception.*;
import jbase.database.*;


/**
 * Field owned by a key that "points" to another key
 * @author Bryan McClain
 */
public class ForeignKeyField extends ItemField<Integer> implements PointerField {

	private final PointableField point;


	/**
	 * Construct a new foreign key field in the database.
	 *  Depth is inherited from the owner field.
	 *
	 * @param db The database for this field
	 * @param name The name of this field
	 * @param owner Key that owns this field
	 * @param point Key that this field points to
	 */
	public ForeignKeyField(Database db, String name, ParentField owner, PointableField point) {
		super(FieldType.FOREIGN_KEY,db,name,owner);
		this.point = point;
	}


	/**
	 * Get the key field that this field points to.
	 *  Only used by foreign keys.
	 * @return Point field, or null if it doesn't exist
	 */
	@Override
	public PointableField getPoint() {
		return this.point;
	}



	/**
	 * Get a value stored at a given row in the field
	 * @param row The row to retrieve
	 * @return The value stored at the row (can be -1)
	 *
	 * @throws JBaseBadFieldAction The field doesn't support this action
	 * @throws JBaseFieldActionDenied User doesn't have permission to execute this action
	 * @throws JBaseBadRow Invalid row given to retrieve
	 */
	public Integer get(int row)
	  throws JBaseBadFieldAction, JBaseFieldActionDenied, JBaseBadRow {
		if (!db.getACL().canDo(this,FieldAction.GET)) {
			throw new JBaseFieldActionDenied(db.currentUser(),this,FieldAction.GET);
		}

		if (row < 0 || row >= this.depth) {
			throw new JBaseBadRow(this,row);
		}

		return (Integer) values.get(row);
	}


	/**
	 * Store a value stored at a given row in the field
	 * @param row The row to store
	 * @param val The value stored at the row (-1 = No Row)
	 *
	 * @throws JBaseBadFieldAction The field doesn't support this action
	 * @throws JBaseFieldActionDenied User doesn't have permission to execute this action
	 * @throws JBaseBadRow Invalid row given for storage
	 */
	@Override
	public void put(int row, Integer val)
	  throws JBaseBadFieldAction, JBaseFieldActionDenied, JBaseBadRow {
		if (!db.getACL().canDo(this,FieldAction.GET)) {
			throw new JBaseFieldActionDenied(db.currentUser(),this,FieldAction.GET);
		}

		if (row < 0 || row >= this.depth) {
			throw new JBaseBadRow(this,row);
		}

		if (!point.isValidRow(val)) {
			throw new JBaseBadRow(this,val);
		}

		values.set(row,val);
	}


	/**
	 * Delete this field from the database
	 */
	protected void deleteInternal() {
		super.deleteInternal();
		this.point.deletePointer(this);
	}
}
