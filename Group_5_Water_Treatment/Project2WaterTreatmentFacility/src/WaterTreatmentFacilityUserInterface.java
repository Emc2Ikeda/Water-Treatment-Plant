import java.sql.*;
import java.io.*;

/** Name: WaterTreatmentFaciliyInterface.java
 * @author Ema Ikeda, Mihai Siia, Elena Ochkina
 * Description: Interface for Project 2. Covers the following scenarios:
 * 1. 
 * 2. 
 * 3. 
 */

public class WaterTreatmentFacilityUserInterface {

    public static void main(String args[]) {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/water_treatment?serverTimeZone=UTC&useSSL=TRUE";
            String user, pass;
            user = readEntry("userid : "); // root
            pass = readEntry("password: "); // manager
            conn = DriverManager.getConnection(url, user, pass);

            boolean done = false;
        	
            do { // load main menu
            	printMenu();
                System.out.print("Type in your option: ");
                System.out.flush();
                String ch = readLine();
                System.out.println();
                switch (ch.charAt(0)) {
                    case '1':  // load Scenario 2
                    	boolean sc2Done = false;
                    	do {
                        	printMenuSc2();
                    		System.out.print("Type in your option: ");
            				System.out.flush();
        					String sc2Choice = readLine();
        					System.out.println();
        					switch (sc2Choice.charAt(0)) {
        						case 'a': // load sub menu for scenario 2
        							boolean doneSub = false;
        			  	          	do {
        			            	  printSc2SubMenu();
        			    	          System.out.print("Type in your option: ");
        			    	          System.out.flush();
        			    	          String subMenuChoice = readLine();
        			    	          switch (subMenuChoice.charAt(0)) {
        			    	          	case '1': printTreatmentFacilitiesSummary(conn);		    	     
        			    	          		break;
        			    	          	case '2': printWaterSourceSummaryReport(conn);
        			    	          		break;
        			    	          	case 'q': doneSub = true;
        			    	          		break;
        			    	          	default:
        			    	          		System.out.println(" Not a valid option ");
        			    	          		break;
        			    	          }
        			  	          	} while (!doneSub);
        							break;
        						case 'b':
        							listWaterTreatments(conn); // show available water treatments
        			          	  	System.out.println("Enter regulation id: ");
        			          	  	int id = Integer.parseInt(readLine());
        			          	  	if (id >= 1 && id <= 4) { // current range of id is 1-4 and does not update in Scenario 1
        			          	  		findRegulations(conn, id);
        			          	  	}
        			          	  break;
        						case 'q': // quit to main menu
        							sc2Done = true;
        							break;
        						default:
        							break;
        					}
                    	} while (!sc2Done);
                        break;
                    case '2': // businesses
                        break;
                    case '3': // call statistics and data analysis
                        boolean statDone = false; // exit statistics menu if true
                        do {
                        	Statistics.printStatMenu();
                            System.out.print("Type in your option: ");
                            System.out.flush();
                            String statChoice = readLine(); // choice for statistics submenu
                            System.out.println();
                            switch (statChoice.charAt(0)) {
                                case 'a': Statistics.avgMonthlyWaterConsumptionByAgricultureBusiness(conn);
                                    break;
                                case 'b':
                                	Statistics.avgMonthlyWaterConsumptionByIndustrialBusiness(conn);
                                    break;
                                case 'c':
                                    Statistics.avgMonthlyWaterWasteByAgriculturalBusiness(conn);
                                    break;
                                case 'd':
                                	Statistics.avgMonthlyWaterWasteByIndustrialBusiness(conn);
                                    break;
                                case 'e':
                                    Statistics.avgMonthlyWaterWasteWaterConsumptionByBothBusinesses(conn);
                                    break;
                                case 'q': statDone = true;
                                    break;
                                default:
                                    System.out.println(" Not a valid option ");
                                    break;
                            } //switch

                        } while (!statDone);
                    	break;
                    case '4': // update
                    	break;
                    case '5': done = true;
                        break;
                    default:
                        System.out.println(" Not a valid option ");
                } //switch
            } while (!done);


        } catch (ClassNotFoundException e) {
            System.out.println("Could not load the driver");
        } catch (SQLException ex) {
            System.out.println(ex);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {   
                    System.out.println(e);
                }
            }
        }
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
	
	// 	Retrieve a summary report of the water source that shows: a description of the types of water treatments it gets
	//		and a description of the types of water treatments it is supposed to get based on the regulations.
	private static void printWaterSourceSummaryReport(Connection conn) throws SQLException, IOException {
		  String query = "SELECT wtr.Water_Source_ID, wtr.Regulation_ID, wtp.Treatment_Description\r\n"
		  		+ "FROM water_treatment_regulation_applied wtr \r\n"
		  		+ "INNER JOIN water_treatment_performed wtp ON wtr.Water_Source_ID = wtp.Water_Source_ID\r\n"
		  		+ "ORDER BY Water_Source_ID;"; // query	
  		  String title = "    Water Source Summary";
  		  executeSelectQuery(conn, query, title);
	}
	
	//	Retrieve a summary report of for each facility that shows: name, years of operation, and amount of water it treats per month.
	private static void printTreatmentFacilitiesSummary(Connection conn) throws SQLException, IOException {
		  String query = "SELECT Facility_Name, TIMESTAMPDIFF(year, Facility_Start_Date, now()) as \"Years of Operation\", Facility_Water_Quantity as \"Treated Water Quantity per Month\"\r\n"
		  		+ "FROM water_treatment_facility;";
		  String title = "Water Treatment Facilities Summary";
		  executeSelectQuery(conn, query, title);
	}
	
	// 	List all the different water treatments recorded in this database. Prompt the user to 
	//select a specific water treatment in the menu by id
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
    
	private static void printMenu() {
	     System.out.println("***********************************************************");
	     System.out.println("Welcome to the DSS for Washington’s Water Treatment Facilities");
	     System.out.println("***********************************************************");
		 System.out.println("1. Water Source, Treatment & Regulations");
		 System.out.println("2. Businesses");
		 System.out.println("3. Statistics and Data Analysis");
		 System.out.println("4. Updates");
		 System.out.println("5. Quit");
	}
	
	// Print menus for scenario 2
	private static void printMenuSc2() {
	     System.out.println("***********************************************************");
	     System.out.println("Welcome to the DSS for Washington’s Water Treatment Facilities");
	     System.out.println("1. Water Source, Treatment & Regulations");
	     System.out.println("***********************************************************");
		 System.out.println("A. Current activities");
		 System.out.println("B. Water treatment regulations");
		 System.out.println("Q. Quit");
	}
	private static void printSc2SubMenu() {
		System.out.println("Select: ");
		System.out.println("1. Treatment Facilities Summary Report");
		System.out.println("2. Water Source Summary Report");
		System.out.println("Q. Quit");
	}
}
