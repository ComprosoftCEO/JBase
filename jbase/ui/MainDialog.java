package jbase.ui;

import java.util.Scanner;

/**
 * Main dialog class for interacting with JBase using CLI
 * @author Bryan McClain
 */
public class MainDialog implements JBaseDialog {

	/**
	 * Show the dialog for this interface
	 */
	public void showDialog() {
		System.out.println("=============================");
		System.out.println("|   JBase Database Engine   |");
		System.out.println("=============================");
		System.out.println("  Created by Bryan McClain\n");

		//Run the menu until it quits
		while(runMenu());
	}


	/**
	 * Run the main menu for this dialog
	 * @return True if to run again, false if not
	 */
	private boolean runMenu() {
		System.out.println("N - New Database");
		System.out.println("L - Load Database");
		System.out.println("O - Open Database");
		System.out.println("Q - Quit");

		boolean running = true;
		Scanner scan = new Scanner(System.in);
		while(running) {
			String line = scan.nextLine();
			
		}

		return false;
	}


	public static void main(String[] args) {
		JBaseDialog d = new MainDialog();
		d.showDialog();
	}
}
