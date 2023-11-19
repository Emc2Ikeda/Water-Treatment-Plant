import java.sql.*;
import java.io.*;


/** Name: WaterTreatmentFacilityInterface.java
 * @author Ema Ikeda, Elena Ochkina, Mihai Siia
 * Description: Interface for Project 2. Covers the following scenarios:
 * 1. 
 * 2. 
 * 3. 
 */

public class WaterTreatmentFacilityUserInterface {

    public static void main(String args[]) throws SQLException, ClassNotFoundException, IOException {
		MySQLConnection.getInstance();
		mainMenuOption();
	}
	private static void mainMenuOption () throws SQLException, IOException, ClassNotFoundException {
		boolean done = false;
		do {
			printMainMenu();
			System.out.print("Type in your option: ");
			System.out.flush();
			String ch = readLine();
			System.out.println();
			switch (ch.charAt(0)) {
				case '1':
					Regulation.Regulation();
					break;
				case '2':
					Statistics.Statistics();
					break;
				case '3':
					Updates.Updates();
					break;
				case '4':
					done = true;
					break;
				default:
					System.out.println(" Not a valid option ");
			}

		} while (!done);
	}


	private static void printMainMenu() {
		System.out.println("*****************************************************************************");
		System.out.println("                               ***********");
		System.out.println("               Welcome to the DSS for Washington’s Water Treatment Facilities");
		System.out.println("                               ***********");
		System.out.println("*****************************************************************************");
		System.out.println("                   1. Water Source, Treatment & Regulations");
		System.out.println("                   2. Statistics and Data Analysis");
		System.out.println("                               3. Updates");
		System.out.println("                                4. Quit");
		System.out.println("*****************************************************************************");
		System.out.println();
	}
	private String readEntry(String prompt) {
		try {
			StringBuffer buffer = new StringBuffer();
			System.out.print(prompt);
			System.out.flush();
			int c = System.in.read();
			while(c != '\n' && c != -1) {
				buffer.append((char)c);
				c = System.in.read();
			}
			return buffer.toString().trim();
		} catch (IOException e) {
			return "";
		}
	}

	private static String readLine() {
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr, 1);
		String line = "";

		try {
			line = br.readLine();
		} catch (IOException e) {
			System.out.println("Error in SimpleIO.readLine: " +
					"IOException was thrown");
			System.exit(1);
		}
		return line;
	}

}
