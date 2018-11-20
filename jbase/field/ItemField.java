package jbase.field;

import java.io.Serializable;

/**
 * Represents an "Item" in our database (field owned by a key)
 * @author Bryan McClain
 */
public class ItemField<T extends Serializable> extends Field<T> implements Serializable {

	private final KeyField owner;
	private ArrayList<T> values;


	/**
	 * Construct a new item field in the database.
	 *  Depth is inherited from the owner field.
	 *
	 * @param db The database for this field
	 * @param name The name of this field
	 * @param owner Key that owns this field
	 */
	public ItemField(Database db, String name, KeyField owner) {
		super(db,name,FieldType.ITEM);
		this.owner = owner;
		this.values = new ArrayList<T>(owner.getDepth());
	}

}
