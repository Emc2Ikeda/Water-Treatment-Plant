import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class MySQLConnection implements AutoCloseable {

    private Connection connection;
    private static MySQLConnection instance;

    public static MySQLConnection getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null) {
            instance = new MySQLConnection();
        }

        return instance;
    }

    public Connection getConnection() {
        return connection;
    }


    public MySQLConnection() throws SQLException, ClassNotFoundException {
        Connection conn = null;

        do {
            try {
                System.out.println("PLease set your connection!");
                //Step 1: Load the JDBC driver(You have to have the connector Jar file in your project Class path)
                Class.forName("com.mysql.cj.jdbc.Driver");

                //Connect to the database(Change the URL)
                String url = "jdbc:mysql://localhost:3306/water_treatment?serverTimezone=UTC&useSSL=TRUE";
                String user, pass;
                user = readEntry("UserId : ");
                pass = readEntry("Password: ");
                conn = DriverManager.getConnection(url, user, pass);
            } catch (ClassNotFoundException e) {
                System.out.println("Could not load the driver");
            } catch (SQLException ex) {
                System.out.println(ex);
            }

        } while (conn == null);
        this.connection = conn;
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

    @Override
    public void close() throws Exception {
        if (this.connection != null) {
            this.connection.close();
        }
    }
}
