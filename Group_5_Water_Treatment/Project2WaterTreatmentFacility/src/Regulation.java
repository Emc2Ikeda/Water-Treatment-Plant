// Name: Regulation.java
// Author: Ema Ikeda
// Date: 12/3/22
//  Description: Covers scenario 2 that includes the following features: 
//  1. Option a: Load a submenu that gives the user the following choices: 
        //  1. Retrieve a summary report for each water treatment facility
        //  2. Retrieve a summary report of the water source
//  2. Option b: Prompt the user to select the type of water treatment, 
//  then retrieve the list of regulations that apply to the selected treatment
//  3. Option q: Return to main menu

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class Regulation {
    public static void Regulation () throws SQLException, ClassNotFoundException, IOException {
        boolean done = false;
        do {
            printMenu();
            System.out.flush();
            String ch = readLine();
            System.out.println();
            Connection connection = MySQLConnection.getInstance().getConnection();
            try {
                switch (ch.charAt(0)) {
                    case 'a':
                        loadOptionA(connection);
                        break;
                    case 'b':
                        listWaterTreatments(connection); // show available water treatments
                        System.out.println("Enter regulation id: ");
                        int id = Integer.parseInt(readLine());
                        if (id >= 1 && id <= 4) {
                            findRegulations(connection, id);
                        }
                        break;
                    case 'q':
                        done = true;
                        break;
                    default:
                        System.out.println(" Not a valid option ");
                }
            }
            catch (IOException e){
                e.printStackTrace();
            }

        } while (!done);
    }

    //  Load a submenu for option a that gives the user a following choices:
    //  1. Retrieve a summary report for each water treatment facility
    //  2. Retrieve a summary report of the water source
    private static void loadOptionA(Connection connection) throws SQLException, IOException {
        boolean done = false;
        do {
            System.out.println("Option 1 (Treatment Facilities Summary Report)");
            System.out.println("Option 2 (Water Source Summary Report)");
            System.out.println("Option q: Back to the menu");
            System.out.println("Type in your option:");
            System.out.flush();
            String ch = readLine();
            System.out.println();
            switch (ch.charAt(0)) {
                case '1':
                    printTreatmentFacilitiesSummary(connection);
                    break;
                case '2':
                    printWaterSourceSummaryReport(connection);
                    break;
                case 'q':
                    done = true;
                    break;
                default:
                    break;
            }
        } while (!done);
    }

    // 	Retrieve a summary report of the water source that shows: a description of the types of water treatments it gets
	//  and a description of the types of water treatments it is supposed to get based on the regulations.
    private static void printWaterSourceSummaryReport(Connection conn) throws SQLException, IOException {
        String query = "SELECT wtr.Water_Source_ID, wtr.Regulation_ID, wtp.Treatment_Description\r\n"
                + "FROM water_treatment_regulation_applied wtr \r\n"
                + "INNER JOIN water_treatment_performed wtp ON wtr.Water_Source_ID = wtp.Water_Source_ID\r\n"
                + "ORDER BY Water_Source_ID;"; // query
        String title = "    Water Source Summary";
        executeSelectQuery(conn, query, title);
    }
    //	Retrieve a summary report for each facility that shows: name, years of operation, and amount of water it treats per month.
    private static void printTreatmentFacilitiesSummary(Connection conn) throws SQLException, IOException {
        String query = "SELECT Facility_Name, TIMESTAMPDIFF(year, Facility_Start_Date, now()) as \"Years of Operation\", Facility_Water_Quantity as \"Treated Water Quantity per Month\"\r\n"
                + "FROM water_treatment_facility;";
        String title = "Water Treatment Facilities Summary";
        executeSelectQuery(conn, query, title);
    }

    // 	List all the different water treatments recorded in this database. Prompt the user to
    //  select a specific water treatment in the menu by entering id number from 1 to 4
    private static void listWaterTreatments(Connection conn) throws SQLException, IOException {
        Statement stmt = conn.createStatement();
        String query = "SELECT DISTINCT Treatment_Description FROM water_treatment_performed;";
        ResultSet rset = retrieveQueryResult(conn, query);

        System.out.println("    WATER TREATMENTS");
        System.out.println("--------------------------------------------------\n");
        int id = 1; // use to help select water treatment
        while(rset.next()) {
            String treatmentName = rset.getString(1);
            System.out.println(id + " " + treatmentName);
            id++;
        }
        stmt.close();
    }

    
    //  Retrieve a list of regulations that include the chosen water treatment, the approval date, and 
    //  a detailed description for each regulation.
    private static void findRegulations(Connection conn, int id) throws SQLException, IOException {
        String waterTreatmentName = "";
        switch (id) {
            case 1: waterTreatmentName = "Purity Treatment";
                break;
            case 2: waterTreatmentName = "New filtration process";
                break;
            case 3: waterTreatmentName = "Chlorine test";
                break;
            case 4: waterTreatmentName = "Purity test";
                break;
            default:
                break;
        }
        String query = "select r.Regulation_Name, r.Regulation_Approval_Date as Approval_Date, r.Regulation_Description as Description" +
                " from water_treatment_performed as wtp, regulation as r " +
                "WHERE wtp.Regulation_ID = r.Regulation_ID and wtp.Treatment_Description = \"" + waterTreatmentName + "\";";
        String title = " REGULATIONS APPLIED ON: " + waterTreatmentName;
        executeSelectQuery(conn, query, title);
    }

    // Utility function that retrieves query results and returns ResultSet
    private static ResultSet retrieveQueryResult(Connection conn, String query) throws SQLException, IOException{
        PreparedStatement p = conn.prepareStatement(query);
        ResultSet rset = p.executeQuery();
        return rset;
    }

    public static void executeSelectQuery(Connection conn, String query, String title) throws SQLException, IOException {

        //STEP1: CREATE VARIABLE OF TYPE STATEMENT
        Statement stmt = conn.createStatement();

        // Step 2: Declare a variable with ResultSet type
        ResultSet resultSet = stmt.executeQuery(query);

        //Print title of the Table result

        System.out.println(title);
        System.out.println("--------------------------------------------------");

        //get result as Metadata. Read by column
        ResultSetMetaData metadata = resultSet.getMetaData();
        int columnCount = metadata.getColumnCount();

        // Print column's labels
        for (int col = 1; col <= columnCount; col++) {
            System.out.printf("%-20s", metadata.getColumnLabel(col));
        }
        System.out.println();

        // Print data rows
        while(resultSet.next()) {
            for (int i = 1; i <= columnCount; i++) {
                System.out.printf("%-20s", resultSet.getString(i));
            }
            System.out.println();
        }
        //Close the statement
        stmt.close();
    }
    static String readEntry(String prompt) {
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

    private static void printMenu() {
        System.out.println("***********************************************************\n" +
                "      Welcome to the DSS for Washington’s Water Treatment Facilities\n" +
                "                  1. Water Source, Treatment & Regulations\n" +
                "          ***********************************************************\n" +
                "                             a. Current activities\n" +
                "                        b. Water treatment regulations\n" +
                "                                    q. Quit\n" +
                "Type in your option:");
    }
}