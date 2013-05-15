package robotinterface.util;

import java.sql.*;
import java.util.*;

/**
 * Database Manager Class.
 * <p>
 * @author Brian Bailey
 */
public class DBManager {

    private static Connection connection = null;

    /**
     * Get Connection to Database, with settings from Property File.
     * <p>
     * @return      Connection to Database.
     */
    public static Connection getConnection() {
        if (connection != null) { return connection; } 
        else {
            try {
                ResourceBundle resource = ResourceBundle.getBundle("robotinterface.util.dbProperties");
                String DB_URL = resource.getString("db.url");
                String DB_USER = resource.getString("db.username");
                String DB_PASSWORD = resource.getString("db.password");
                String DB_DRIVER = resource.getString("db.driver");

                Class.forName(DB_DRIVER);
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            } catch (ClassNotFoundException | SQLException ex) {
                System.out.println(ex.getMessage());
            }
            return connection;
        }
    }
    
}
