import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class Statistics {
//    public static void main(String args[]) {
//        Connection conn = null;
//        try {
//            //Step 1: Load the JDBC driver(You have to have the connector Jar file in your project Class path)
//            Class.forName("com.mysql.cj.jdbc.Driver");
//
//            //Connect to the database(Change the URL)
//            String url = "jdbc:mysql://localhost:3306/water_treatment?serverTimezone=UTC&useSSL=TRUE";
//            String user, pass;
//            user = readEntry("UserId : ");
//            pass = readEntry("Password: ");
//            conn = DriverManager.getConnection(url, user, pass);
//            boolean done = false;
//            do {
//                System.out.print("Type in your option: ");
//                System.out.flush();
//                String ch = readLine();
//                System.out.println();
//                switch (ch.charAt(0)) {
//                    case 'a': avgMonthlyWaterConsumptionByAgricultureBusiness(conn);
//                        break;
//                    case 'b':
//                        avgMonthlyWaterConsumptionByIndustrialBusiness(conn);
//                        break;
//                    case 'c':
//                        avgMonthlyWaterWasteByAgriculturalBusiness(conn);
//                        break;
//                    case 'd':
//                        avgMonthlyWaterWasteByIndustrialBusiness(conn);
//                        break;
//                    case 'e':
//                        avgMonthlyWaterWasteWaterConsumptionByBothBusinesses(conn);
//                        break;
//                    case 'f':
//                        deleteFacility(conn);
//                        break;
//
//                    case 'q': done = true;
//                        break;
//                    default:
//                        System.out.println(" Not a valid option ");
//                        break;
//                } //switch
//
//            } while (!done);
//
//
//        } catch (ClassNotFoundException e) {
//            System.out.println("Could not load the driver");
//        } catch (SQLException ex) {
//            System.out.println(ex);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (conn != null) {
//                try {
//                    conn.close();
//                } catch (SQLException e) {
//                    System.out.println(e);
//                }
//            }
//        }
//    }
    
    public static void avgMonthlyWaterConsumptionByAgricultureBusiness(Connection conn) throws SQLException, IOException {

            /*In this method your SQL Query should return the BusinesID, BusinessName, the sum of average water consumption
            *  for all facilities that this business used. This report for agricultural businesses */

        String query = "select bf.Business_ID, b.Business_Name, sum(bf.Average_Water_Consumption) as Water_Consumption" +
                " from business_facility_used as bf, business as b" +
                " where bf.Business_ID = b.Business_ID and b.Business_Type = \"Agriculture\"" +
                " group by bf.Business_ID";

        String title = "Average Monthly Water Consumption Report for Agricultural Business";

        Statistics.executeSelectQuery(conn, query, title);
    }

    public static void avgMonthlyWaterConsumptionByIndustrialBusiness(Connection conn) throws SQLException, IOException {
        /*In this method your SQL Query should return the BusinesID, BusinessName, the sum of average water consumption
         *  for all facilities that this business used. This report for industrial businesses */

        String query = "select bf.Business_ID, b.Business_Name, sum(bf.Average_Water_Consumption) as Water_Consumption" +
                " from business_facility_used as bf, business as b" +
                " where bf.Business_ID = b.Business_ID and b.Business_Type = \"Industrial\"" +
                " group by bf.Business_ID";

        String title = "Average Monthly Water Consumption Report for Industrial Business";

        Statistics.executeSelectQuery(conn, query, title);

    }
    public static void avgMonthlyWaterWasteByAgriculturalBusiness(Connection conn) throws SQLException, IOException {
        /*In this method your SQL Query should return the BusinesID, BusinessName, the sum of average water wasted
         *  for all facilities that this business used. This report for agricultural businesses */

        String query = "select bf.Business_ID, b.Business_Name, sum(bf.Average_Water_Wasted)" +
                " from business_facility_used as bf, business as b" +
                " where bf.Business_ID = b.Business_ID and b.Business_Type = \"Agricultural\"" +
                " group by bf.Business_ID";

        String title = "Average Monthly Water Wasted Report for Agricultural Business";

        Statistics.executeSelectQuery(conn, query, title);
    }
    public static void avgMonthlyWaterWasteByIndustrialBusiness(Connection conn) throws SQLException, IOException {
        /*In this method your SQL Query should return the BusinesID, BusinessName, the sum of average water wasted
         *  for all facilities that this business used. This report for industrial businesses */

        String query = "select bf.Business_ID, b.Business_Name, sum(bf.Average_Water_Wasted) as Water_Wasted" +
                " from business_facility_used as bf, business as b" +
                " where bf.Business_ID = b.Business_ID and b.Business_Type = \"Industrial\"" +
                " group by bf.Business_ID";

        String title = "Average Monthly Water Wasted Report for Industrial Business";

        Statistics.executeSelectQuery(conn, query, title);


    }
    public static void avgMonthlyWaterWasteWaterConsumptionByBothBusinesses(Connection conn) throws SQLException, IOException {
        /*In this method your SQL Query should return the Business Type, the sum of average water consumption, the sum of average water wasted
         *  for all facilities that these types of businesses used. This report for both agricultural and industrial businesses */

        String query = "select b.Business_Type as Business_Type, sum(bf.Average_Water_Wasted) as Water_Wasted, sum(bf.Average_Water_Consumption) as Water_Consumption " +
                " from business_facility_used as bf, business as b" +
                " where bf.Business_ID = b.Business_ID" +
                " group by b.Business_Type";

        String title = "Average Monthly Water Consumption and Water Wasted Report for Agricultural and Industrial Businesses";

        Statistics.executeSelectQuery(conn, query, title);
    }

//    public static void deleteFacility(Connection conn) throws SQLException, IOException {
//        //Scenario 2
//
//        String titleList = "Water Treatment Regulation: ";
//        String queryList = "select r.Regulation_Name, r.Regulation_Approval_Date as Approval_Date, r.Regulation_Description as Description" +
//                " from water_treatment_performed as wtp, regulation as r" +
//                " where wtp.Regulation_ID = r.Regulation_ID and wtp.Treatment_Description = \"Purity Treatment\"";
//
//        Statistics.executeSelectQuery(conn, queryList, titleList);
//
//    }

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

    public static void printStatMenu() {
        System.out.println("\n        QUERY OPTIONS ");
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