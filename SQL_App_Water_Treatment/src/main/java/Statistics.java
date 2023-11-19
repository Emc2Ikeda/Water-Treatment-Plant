import com.sun.jdi.IntegerType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Statistics {
    public static void Statistics () throws SQLException, ClassNotFoundException, IOException {
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
                            avgMonthlyWaterConsumptionByAgricultureBusiness(connection);
                            break;
                        case 'b':
                            avgMonthlyWaterConsumptionByIndustrialBusiness(connection);
                            break;
                        case 'c':
                            avgMonthlyWaterWasteByAgriculturalBusiness(connection);
                            break;
                        case 'd':
                            avgMonthlyWaterWasteByIndustrialBusiness(connection);
                            break;
                        case 'e':
                            avgMonthlyWaterWasteWaterConsumptionByBothBusinesses(connection);
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
    private static void avgMonthlyWaterConsumptionByAgricultureBusiness(Connection conn) throws SQLException, IOException {

            /*In this method your SQL Query should return the BusinesID, BusinessName, the sum of average water consumption
            *  for all facilities that this business used. This report for agricultural businesses */

        String query = "select bf.Business_ID, b.Business_Name, round(sum(bf.Average_Water_Consumption), 2) as Water_Consumption" +
                " from business_facility_used as bf, business as b" +
                " where bf.Business_ID = b.Business_ID and b.Business_Type = \"Agriculture\"" +
                " group by bf.Business_ID";

        String title = "Average Monthly Water Consumption Report for Agricultural Business";

        Statistics.executeSelectQuery(conn, query, title);
    }

    private static void avgMonthlyWaterConsumptionByIndustrialBusiness(Connection conn) throws SQLException, IOException {
        /*In this method your SQL Query should return the BusinesID, BusinessName, the sum of average water consumption
         *  for all facilities that this business used. This report for industrial businesses */

        String query = "select bf.Business_ID, b.Business_Name, round(sum(bf.Average_Water_Consumption),2) as Water_Consumption" +
                " from business_facility_used as bf, business as b" +
                " where bf.Business_ID = b.Business_ID and b.Business_Type = \"Industrial\"" +
                " group by bf.Business_ID";

        String title = "Average Monthly Water Consumption Report for Industrial Business";

        Statistics.executeSelectQuery(conn, query, title);

    }
    private static void avgMonthlyWaterWasteByAgriculturalBusiness(Connection conn) throws SQLException, IOException {
        /*In this method your SQL Query should return the BusinesID, BusinessName, the sum of average water wasted
         *  for all facilities that this business used. This report for agricultural businesses */

        String query = "select bf.Business_ID, b.Business_Name, round(sum(bf.Average_Water_Wasted),2)" +
                " from business_facility_used as bf, business as b" +
                " where bf.Business_ID = b.Business_ID and b.Business_Type = \"Agricultural\"" +
                " group by bf.Business_ID";

        String title = "Average Monthly Water Wasted Report for Agricultural Business";

        Statistics.executeSelectQuery(conn, query, title);
    }
    private static void avgMonthlyWaterWasteByIndustrialBusiness(Connection conn) throws SQLException, IOException {
        /*In this method your SQL Query should return the BusinesID, BusinessName, the sum of average water wasted
         *  for all facilities that this business used. This report for industrial businesses */

        String query = "select bf.Business_ID, b.Business_Name, round(sum(bf.Average_Water_Wasted),2) as Water_Wasted" +
                " from business_facility_used as bf, business as b" +
                " where bf.Business_ID = b.Business_ID and b.Business_Type = \"Industrial\"" +
                " group by bf.Business_ID";

        String title = "Average Monthly Water Wasted Report for Industrial Business";

        Statistics.executeSelectQuery(conn, query, title);


    }
    private static void avgMonthlyWaterWasteWaterConsumptionByBothBusinesses(Connection conn) throws SQLException, IOException {
        /*In this method your SQL Query should return the Business Type, the sum of average water consumption, the sum of average water wasted
         *  for all facilities that these types of businesses used. This report for both agricultural and industrial businesses */

        String query = "sum(bf.Average_Water_Wasted) as Water_Wasted, sum(bf.Average_Water_Consumption) as Water_Consumption," +
                " b.Business_Type, round((sum(bf.Average_Water_Consumption) - sum(bf.Average_Water_Wasted)), 2) as Water_Treatment_Needed, " +
                "+ wt.Facility_ID, wt.Facility_Name " +
                " from business as b" +
                " join business_facility_used as bf" +
                " on b.Business_ID = bf.Business_ID" +
                " join water_treatment_facility as wt" +
                " on bf.Facility_ID = wt.Facility_ID" +
                " group by b.Business_Type, wt.Facility_ID, wt.Facility_Name";

        String title = "Water Consumption and Water Treatment Report ";

        Statistics.executeSelectQuery(conn, query, title);
    }

    private static void option2(Connection conn) throws SQLException, IOException {
        HashMap<Integer, String> waterTreatmentMap = new HashMap();

        //STEP1: CREATE VARIABLE OF TYPE STATEMENT
        Statement stmt = conn.createStatement();

        String queryTreatment = "select distinct Treatment_Description" +
                " from water_treatment_performed";

        // Step 2: Declare a variable with ResultSet type
        ResultSet resultSet = stmt.executeQuery(queryTreatment);

        //get result as Metadata. Read by column
        ResultSetMetaData metadata = resultSet.getMetaData();
        //create variable for water treatment choice
        int choiceOption = 1;

        // put water treatment choice number with the respective water treatment option into the hashmap
        while(resultSet.next()){
            waterTreatmentMap.put(choiceOption, resultSet.getString(1));
            choiceOption++;
        }

        System.out.println("Water Treatment options: ");
        //iterate the hashmap and display water treatment choices for a user
        for(Map.Entry<Integer, String> set: waterTreatmentMap.entrySet()){
            System.out.println(set.getKey() + ": " + set.getValue());
        }
        //get a water treatment choice from a user
        int treatmentChoice = Integer.parseInt(readEntry("Please select water treatment option. Put the respective number: "));
        //Scenario 2
        //set a water treatment option depending on a user choice
        String waterTreatmentOption = waterTreatmentMap.get(treatmentChoice);

        //Display regulation for a water treatment option selected by a user;
        String title = "Water Treatment Regulation: ";
        String query = "select r.Regulation_Name, r.Regulation_Approval_Date as Approval_Date, r.Regulation_Description as Description" +
                " from water_treatment_performed as wtp, regulation as r" +
                " where wtp.Regulation_ID = r.Regulation_ID and wtp.Treatment_Description = " + "'" +waterTreatmentOption +"'";

        Statistics.executeSelectQuery(conn, query, title);
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
        System.out.println("Business and Water Consumption Reports ");
        System.out.println("(a) Water Consumption for agricultural businesses. ");
        System.out.println("(b) Water Consumption for industrial businesses ");
        System.out.println("(c) Water Wasted for agricultural businesses");
        System.out.println("(d) Water Wasted for industrial businesses");
        System.out.println("(e) Water Consumption and Water Wasted for both agricultural and industrial businesses");
        System.out.println("(q) Quit. \n");
    }

    private static void executeSelectQuery(Connection conn, String query, String title) throws SQLException, IOException {

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
}


