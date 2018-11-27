package jbase.ui;

import java.util.Scanner;
import java.util.Collection;

/**
 * Interface for interacting with any JBase Dialog
 * @author Bryan McClain
 */
public interface JBaseDialog {

	public static final Scanner scan = new Scanner(System.in);

	/**
	 * Show the dialog for this interface
	 */
	public void showDialog();


	//========== Static Helper Methods ===============

	/**
	 * Get a single line of input from the user
	 * @param prompt String to display before the text
	 * @return Line of user input
	 */
	public static String readLine(String prompt) {
		System.out.print(prompt);
		return scan.nextLine();
	}


	/**
	 * Read a single line of input from the user, making
	 *  sure the entered line isn't null (also trims the string)
	 *
	 * @param prompt String to display before the text
	 * @param trim If true, then trim the string before checking
	 * @return Line of user input
	 */
	public static String readNotNull(String prompt, boolean trim) {
		String ret;
		do {
			ret = readLine(prompt);
			if (trim) {ret = ret.trim();}
		} while(ret.equals(""));
		return ret;
	}

	/**
	 * Print out a collection of items to the terminal
	 * @param c The collection to print
	 * @param useBullet If true, list fields by bullet point instead of by number
	 */
	public static <E> void printCollection(Collection<E> c, boolean useBullet) {

		//When to use bullet point vs numbering
		if (useBullet) {
			for (E item: c) {
				System.out.println("* "+item.toString());
			}
		} else {
			int i = 1;
			for (E item: c) {
				System.out.println((i++)+": "+item.toString());
			}
		}
	}


	/**
	 * Read an integer from user input
	 * @param prompt String to display before the text
	 * @return Integer read
	 */
	public static int readInt(String prompt) {
		while(true) {
			String line = readLine(prompt);
			try {
				return Integer.parseInt(line);
			} catch (NumberFormatException ex) {
				System.out.println("Invalid number '"+line+"'!");
			}
		}
	}



	/**
	 * Ask the user a Yes-No Question
	 * @param question The question to ask
	 * @return True for yes or false for no
	 */
	public static boolean readYesNo(String question) {
		System.out.println(question);

		while(true) {
			String line = readNotNull("[Y,N]: ", true).toLowerCase();
			if (line.equals("y")) {return true;}
			else if (line.equals("n")) {return false;}
		}
	}




	/**
	 * Ask the user to input a value that is not in the given collection
	 *
	 * @param prompt String to display before the text
	 * @param values List of values to check for uniqueness
	 * @param failure What to print upon failure
	 * @param loop If true, keeps looping until user inputs valid input. Otherwise, returns null on failure.
	 * @return The string, or null upon failure
	 */
	public static String readUnique(String prompt, Collection<String> values, String failure, boolean loop) {
		String line = JBaseDialog.readNotNull(prompt, true);
		while (values.contains(line)) {
			System.out.println(failure);
			if (!loop) {return null;}
			line = JBaseDialog.readNotNull(prompt, true);
		}

		return line;
	}


	/**
	 * Ask the user to input a value that is in the given collection
	 *
	 * @param prompt String to display before the text
	 * @param values List of values to check for uniqueness
	 * @param failure What to print upon failure
	 * @param loop If true, keeps looping until user inputs valid input. Otherwise, returns null on failure.
	 * @return The string, or null upon failure
	 */
	public static String readExisting(String prompt, Collection<String> values, String failure, boolean loop) {
		String line = JBaseDialog.readNotNull(prompt, true);
		while (!values.contains(line)) {
			System.out.println(failure);
			if (!loop) {return null;}
			line = JBaseDialog.readNotNull(prompt, true);
		}

		return line;
	}




	/**
	 * Ask the user to input an integer at least as small as the minimum value
	 *
	 * @param prompt String to display before the text
	 * @param failure What to print upon failure
	 * @param min Minimum value (inclusive)
	 * @param loop If true, keeps looping until user inputs valid input. Otherwise, returns null on failure.
	 * @return The read integer, or null upon failure
	 */
	public static Integer readIntMin(String prompt, String failure, int min, boolean loop) {
		int value = JBaseDialog.readInt(prompt);
		while(value < min) {
			System.out.println(failure);
			if (!loop) {return null;}
			value = JBaseDialog.readInt(prompt);
		}
		return value;
	}


	/**
	 * Ask the user to input an integer no larger than the maximum value
	 *
	 * @param prompt String to display before the text
	 * @param failure What to print upon failure
	 * @param max Maximum value (inclusive)
	 * @param loop If true, keeps looping until user inputs valid input. Otherwise, returns null on failure.
	 * @return The read integer, or null upon failure
	 */
	public static Integer readIntMax(String prompt, String failure, int max, boolean loop) {
		int value = JBaseDialog.readInt(prompt);
		while(value > max) {
			System.out.println(failure);
			if (!loop) {return null;}
			value = JBaseDialog.readInt(prompt);
		}
		return value;
	}



	/**
	 * Ask the user to input an integer in the given range 
	 *
	 * @param prompt String to display before the text
	 * @param failure What to print upon failure
	 * @param one First value (inclusive)
	 * @param two Second value (inclusive)
	 * @param loop If true, keeps looping until user inputs valid input. Otherwise, returns null on failure.
	 * @return The read integer, or null upon failure
	 */
	public static Integer readIntRange(String prompt, String failure, int one, int two, boolean loop) {

		int min = Math.min(one,two);
		int max = Math.max(one,two);

		int value = JBaseDialog.readInt(prompt);
		while((value < min) || (value > max)) {
			System.out.println(failure);
			if (!loop) {return null;}
			value = JBaseDialog.readInt(prompt);
		}
		return value;
	}
}
