package jbase.field;

/**
 * Represents an "Item" in our database (field owned by a key)
 * @author Bryan McClain
 */
public class ItemField<T extends Serializable> extends Field<T> {

	private final KeyField owner;


	/**
	 * Construct a new item field in the database
	 *
	 * @param db The database for this field
	 * @param name The name of this field
	 * @param owner Key that owns this field
	 */
	public ItemField(Database db, String name, KeyField owner) {
		super(db,name,FieldType.ITEM,owner.getDepth());
		this.owner = owner;
	}

}
