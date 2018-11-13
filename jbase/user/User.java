package jbase.user;

import jbase.Database;
import jbase.acl.ACL;
import java.util.Arrays;
import java.security.MessageDigest;

public class User {

	private final String username;			// Username for this User (does not change)
	private double salt;					// Salt for the password hash
	private byte[] password;				// Hashed password for this user
	private final ACL acl;					// Access Control List

	private final Database db;				// Database the User belongs to
	private final User creator;				// Creator of this User object (null = root user)

	private static MessageDigest hasher;	// Used for hashing the password




	//Static initialization for the class
	static {
		try {
			hasher = MessageDigest.getInstance("SHA-256");
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
	}




	/**
	 * Construct a new User object
	 *
	 * @param database The database this user belongs to
	 * @param username The username for this User object
	 * @param password The password for this user
	 * @param creator The creator of this user (NULL = this is the root user)
	 */
	public User(Database db, String username, String password, User creator) {
		this.username = username;
		this.salt = Math.random();
		this.password = hashPassword(password,salt);

		this.db = db;
		this.creator = creator;
		this.acl = new ACL(db,this);
	}



	/**
	 * Get the username for this User object
	 * @return Username
	 */
	public String getUsername() {
		return this.username;
	}



	/**
	 * Hash a password using the password and the given salt
	 * @param password The password to hash
	 * @param salt The salt for the password
	 * @return The hash for this password, salt combination
	 */
	private static byte[] hashPassword(String password, double salt) {
		String toHash = (salt+password);
		hasher.update(toHash.getBytes());
		return hasher.digest();
	}



	/**
	 * Test if the password string equals the internal password of this User object
	 * @param password The password to check
	 * @return True if the password is correct, false otherwise
	 */
	public boolean validatePassword(String password) {
		return Arrays.equals(this.password, hashPassword(password, this.salt));
	}



	/**
	 * Update the password for this user
	 *
	 * @param oldPassword The old password
	 * @param newPassword The new password
	 * @return True if the update was successful, false otherwise
	 */
	public boolean updatePassword(String oldPassword, String newPassword) {
		if (!validatePassword(oldPassword)) {return false; /* Old Password is Invalid */}

		//Password is good, so update the password
		this.salt = Math.random();
		this.password = hashPassword(newPassword,salt);
		return true;
	}


	/**
	 * Test if this is the root user (has a NULL creator)
	 * @return True if this is the root user, false otherwise
	 */
	public boolean isRoot() {
		return (this.creator == null);
	}


	/**
	 * Get the creator of this user (might be NULL if it is the root)
	 * @return Creator
	 */
	public User getCreator() {
		return this.creator;
	}


	/**
	 * Get the Access Control List for this User
	 * @return Access Control List (ACL)
	 */
	public ACL getACL() {
		return this.acl;
	}
}
