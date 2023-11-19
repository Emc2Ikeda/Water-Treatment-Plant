import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class Updates {
    public static void Updates() throws SQLException, ClassNotFoundException, IOException {
        System.out.println("Required admin access!");

        Connection adminConnection = new MySQLConnection().getConnection();

        boolean done = false;
        do {
            printMenu();
            System.out.print("Type in your option: ");
            System.out.flush();
            String ch = readLine();
            System.out.println();
            Connection connection = MySQLConnection.getInstance().getConnection();
            try {
                switch (ch.charAt(0)) {
                    case 'a':
                        deleteWaterTreatmentFacility(connection);
                        break;
                    case 'b':
                        deleteWaterSource(connection);
                        break;
                    case 'c':
                        deleteBusiness(connection);
                        break;
                    case 'd':
                        deleteRegulation(connection);
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
    private static void deleteWaterTreatmentFacility(Connection conn) throws SQLException, IOException {

        String title = "Water Treatment Facility Information";

        //In this method your SQL Query should return the list of water treatment facilities id and name to select what should be deleted

        String queryWaterTreatmentFacility = "select Facility_ID as Facility_Id, Facility_Name as Facility_name" +
                " from water_treatment_facility" ;

        String nameToDelete = "Water Treatment Facility_Id";

        String choiceToDelete = executeSelectQueryDelete(conn, queryWaterTreatmentFacility, title, nameToDelete);

        String queryDelete = "delete from water_treatment_facility where facility_id = " + choiceToDelete;

        Updates.executeDeleteStatement(conn, queryDelete);

    }
    private static void deleteWaterSource(Connection conn) throws SQLException, IOException {
        String title = "Water Source Information";

        //In this method your SQL Query should return the list of water source id and location to select what should be deleted

        String queryWaterSource = "select Water_Source_ID as Water_Source_Id, Water_Source_Location as Location" +
                " from water_source" ;

        String nameToDelete = "Water_Source_Id";

        String choiceToDelete = executeSelectQueryDelete(conn, queryWaterSource, title, nameToDelete);

        String queryDelete = "delete from water_source where Water_Source_ID = " + choiceToDelete;

        Updates.executeDeleteStatement(conn, queryDelete);

    }
    private static void deleteRegulation(Connection conn) throws SQLException, IOException {
        String title = "Water Regulation Information";

        //In this method your SQL Query should return the list of regulations id and regulation name to select what should be deleted

        String queryRegulation = "select Regulation_ID as Regulation_Id, Regulation_Name as Regulation_Name" +
                " from regulation" ;

        String nameToDelete = "Regulation_Id";

        String choiceToDelete = executeSelectQueryDelete(conn, queryRegulation, title, nameToDelete);

        String queryDelete = "delete from regulation where Regulation_ID = " + choiceToDelete;

        Updates.executeDeleteStatement(conn, queryDelete);


    }
    private static void deleteBusiness(Connection conn) throws SQLException, IOException {
        String title = "Business Information";

        //In this method your SQL Query should return the list of regulations id and regulation name to select what should be deleted

        String queryBusiness = "select Business_ID as Business_Id, Business_Name as Business_Name" +
                " from business" ;

        String nameToDelete = "Business_Id";

        String choiceToDelete = executeSelectQueryDelete(conn, queryBusiness, title, nameToDelete);

        String queryDelete = "delete from business where Business_ID = " + choiceToDelete;

        Updates.executeDeleteStatement(conn, queryDelete);

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
        System.out.println("*****************************************************************************");
        System.out.println("                               ***********");
        System.out.println("Delete Options: ");
        System.out.println("(a) Delete Water Treatment Facility Operation ");
        System.out.println("(b) Delete Water Source Operation ");
        System.out.println("(c) Delete Business Operation");
        System.out.println("(d) Delete Regulation Operation");
        System.out.println("(q) Quit. \n");
    }

    private static String executeSelectQueryDelete(Connection conn, String query, String title, String nameToDelete) throws SQLException, IOException {
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

        String choiceToDelete = readEntry("Please select " + nameToDelete + " to delete: ");

        return choiceToDelete;
    }
    private static void executeDeleteStatement(Connection conn, String query) throws SQLException, IOException {
        PreparedStatement preparedStm = conn.prepareStatement(query);
        preparedStm.execute();
        conn.close();

        System.out.println("The operation Delete processed successfully");

    }
}

