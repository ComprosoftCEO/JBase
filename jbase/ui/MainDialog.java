package jbase.ui;

/**
 * Main dialog class for interacting with JBase using CLI
 * @author Bryan McClain
 */
public class MainDialog implements JBaseDialog {

	/**
	 * Show the dialog for this interface
	 */
	public void showDialog() {
		//Run the menu until it quits
		while(runMenu());
	}


	/**
	 * Run the main menu for this dialog
	 * @return True if to run again, false if not
	 */
	private boolean runMenu() {
		System.out.println("=============================");
		System.out.println("|   JBase Database Engine   |");
		System.out.println("=============================");
		System.out.println("  Created by Bryan McClain\n");
		System.out.println(" N - New Database");
		System.out.println(" L - Load Database");
		System.out.println(" O - Open Database");
		System.out.println(" Q - Quit\n");

		boolean running = true;
		while(running) {
			String line = JBaseDialog.readLine("> ");
		
			JBaseDialog d;
			switch(line.toUpperCase()) {
				case "Q": return false;
				case "N": d = new NewDatabaseDialog(); break;
				case "L": d = new LoadDatabaseDialog(); break;
				case "O": d = new OpenDatabaseDialog(); break;
				default:
					System.out.println("Unknown command '"+line+"'");
					continue;
			}

			d.showDialog();
			running = false;
		}

		return true;
	}


	public static void main(String[] args) {
		JBaseDialog d = new MainDialog();
		d.showDialog();
	}
}
