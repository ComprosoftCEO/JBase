package jbase.field;

/**
 * Represents a type of field in the database, along with some additional metadata
 * @author Bryan McClain
 */
public enum FieldType {
	KEY          (false,false,true, false,true, true),
	ITEM         (true, false,false,true, false,false),
	FOREIGN_KEY  (true, true, false,true, false,false);


	//Meta Data for the field type
	private final boolean hasOwner;				// Does this field have an owner
	private final boolean hasPoint;				// Does this field point to another field

	private final boolean canInsertDelete;		// Can I call Insert() or Delete()
	private final boolean canPut;				// Can I call the Put() method
	private final boolean canFind;				// Can I call the Find() method
	private final boolean canIterate;			// Can I call the Next() or Pre() methods
	
	
	/**
 	 * Construct a new FieldType enumeration
	 *
	 * @param hasOwner Is this field owned by another field
	 * @param hasPoint Do I point to another field
	 * @param canInsertDelete Can I call Insert() or Delete()
	 * @param canPut Can I call the Put() method
	 * @param canFind Can I call the Find() method
	 * @param canIterate Can I call the Next() or Pre() methods
	 */
	private FieldType(boolean hasOwner, boolean hasPoint,
					 boolean canInsertDelete, boolean canPut,
					 boolean canFind, boolean canIterate) {
		this.hasOwner = hasOwner;
		this.hasPoint = hasPoint;
		this.canInsertDelete = canInsertDelete;
		this.canPut = canPut;
		this.canFind = canFind;
		this.canIterate = canIterate;
	}


	/**
	 * Returns true if this field is owned by another field (has an owner)
	 * @return Has Owner
	 */
	public boolean hasOwner() {return this.hasOwner;}

	
	/**
	 * Returns true if this field points to another field (has a point)
	 * @return Has Point
	 */
	public boolean hasPoint() {
		return this.hasPoint;
	}


	/**
	 * Can I call the Insert() and Delete() method on this field
	 * @return Can Insert and Delete
	 */
	public boolean canInsertDelete() {
		return this.canInsertDelete;
	}


	/**
	 * Can I call the Put() method on this field?
	 * @return Can Put
	 */
	public boolean canPut() {
		return this.canPut;
	}


	/**
	 * Can I call the Find() method on this field?
	 * @return Can Find
	 */
	public boolean canFind() {
		return this.canFind;
	}

	/**
	 * Can I call the Next() or Pre() methods on this field?
	 * @return Can Iterate
	 */
	public boolean canIterate() {
		return this.canIterate;
	}
}
