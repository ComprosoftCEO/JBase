package jbase;


/**
 * Represents an action that can occur in JBase
 */
public interface JBaseAction {


	/**
	 * Get a string to describe this action that occured
	 * @return String to describe this action
	 */
	public String actionName();



	/**
	 * Convert an Enum value to a camel case string.
	 *	The underscore _ is converted to a space
	 *
	 * @param val The num value to convert to a string
	 * @return Camel case string to represent the Enum value
	 */
	public static <T extends Enum<T>> String enumToString(T val) {
		String[] parts = val.name().split("_");
		String retVal = "";
		for (int i = 0; i < parts.length; i++) {
		
			if (i > 0) {retVal += " "; /* Add a space btwn words */}

			//Convert to camel case
			String part = parts[i];	
			retVal += part.substring(0,1).toUpperCase();	//First letter is upper
			retVal += part.substring(1).toLowerCase();		//All other letters are lower case
		}

		return retVal;		
	}
}
