package jbase.exception;

import jbase.database.Database;
import java.util.UUID;

/**
 * Exception thrown when trying to restore a database that doesn't match the UUID
 * @author Bryan McClain
 */
public class JBaseWrongDatabase extends JBaseDatabaseException {

	private final UUID expect_uuid;
	private final UUID given_uuid;

	/**
	 * Create a new Wrong Database exception
	 * @param db The database trying to be restored
	 * @param expect_uuid The expected UUID
	 * @param given_uuid The mismatched UUID
	 */
	public JBaseWrongDatabase(Database db, UUID expect_uuid, UUID given_uuid) {
		super(db,"Trying to restore wrong database."+
				"Expected UUID '"+expect_uuid.toString()+", "+
				"given UUID '"+given_uuid.toString()+"'!");
		this.expect_uuid = expect_uuid;
		this.given_uuid = given_uuid;
	}


	/**
	 * UUID for the database being restord
	 * @return Expected UUID
	 */
	public UUID expectedUUID() {
		return this.expect_uuid;
	}


	/**
	 * UUID for the database trying to restored the database
	 * @return Given UUID
	 */
	public UUID givenUUID() {
		return this.given_uuid;
	}
}
