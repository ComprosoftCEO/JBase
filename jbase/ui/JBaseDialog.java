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
	 */
	public static <E> void printCollection(Collection<E> c) {
		int i = 1;
		for (E item: c) {
			System.out.println(i+": "+item.toString());
			++i;
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
}
