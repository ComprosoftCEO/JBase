package jbase.ui;

import java.util.Scanner;

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

	/**
	 * Get a single line of input from the user
	 * @param prompt String to display before the text
	 * @return Line of user input
	 */
	public static String readLine(String prompt) {
		System.out.print(prompt);
		return scan.nextLine();
	}

}
