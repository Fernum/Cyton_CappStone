
package robotinterface.dao;

import java.sql.*;
import java.util.*;
import robotinterface.list.entry.Entry;
import robotinterface.util.DBManager;

/**
 * EntryDAO Class.
 * <p>
 * @author Bailey
 */
@SuppressWarnings("unchecked")
public class EntryDAO {
    
    static private Connection connection;

    
    /**
     * EntryDAO Constructor.
     */
    public EntryDAO() {
        connection = DBManager.getConnection();
    }
      
    
    /**
     * Connect to Database.
     */
    static public void connect() {
        connection = DBManager.getConnection();
    }
        
    
    /**
     * Disconnect from Database.
     */
    static public void disconnect() {
        if (connection != null) {
            try { connection.close(); } 
            catch (SQLException ignore) { }
        }
    }
     
    
    /**
     * Ad Entry to Database.
     * <p>
     * @param iEntry        Entry to Add to Database.
     * @return              Status of Add Operation.
     */
    static public boolean addEntry(Entry iEntry) {
        try {
            String query = "INSERT INTO Entry VALUES(?,?)";
            PreparedStatement ptmt = connection.prepareStatement(query);

            ptmt.setString(1, iEntry.getTitle());
            ptmt.setString(2, iEntry.getToolTip());

            ptmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return false;
    }
    
    
    /**
     * List Entries  in Database.
     * <p>
     * @return      List of Database Entries.
     */
    static public List listEntrys() {
        List iList = new ArrayList();
        try {
            Entry uEntry;
            String querystring = "SELECT * FROM Entry";

            PreparedStatement ptmt = connection.prepareStatement(querystring);
            ResultSet rs = ptmt.executeQuery();
            while (rs.next()) {
                uEntry = new Entry(rs.getString(1));
                iList.add(uEntry);
            }
        } catch (SQLException ex) {
            System.err.println( ex.getMessage() );
        }
        return iList;
    }
    
}
