package pm3.dal;

import pm3.model.GearAndWeaponJobs;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GearAndWeaponJobsDao {
   
    protected ConnectionManager connectionManager;
    private static GearAndWeaponJobsDao instance = null;
    protected GearAndWeaponJobsDao() {
        connectionManager = new ConnectionManager();
    }
    
    public static GearAndWeaponJobsDao getInstance() {
        if (instance == null) {
            instance = new GearAndWeaponJobsDao();
        }
        return instance;
    }

 // Create method in GearAndWeaponJobsDao
    public GearAndWeaponJobs create(GearAndWeaponJobs gearAndWeaponJobs) throws SQLException {
        String query = "INSERT INTO GearAndWeaponJobs (itemID, jobID) VALUES (?, ?)";
        Connection connection = null;
        PreparedStatement insertStmt = null;
        
        try {
            // Get connection from connection manager
            connection = connectionManager.getConnection();
            
            // Prepare statement
            insertStmt = connection.prepareStatement(query);
            insertStmt.setInt(1, gearAndWeaponJobs.getItemID()); // Set the itemID from the provided object
            insertStmt.setInt(2, gearAndWeaponJobs.getJobID()); // Set the jobID from the provided object

            insertStmt.executeUpdate();
            return new GearAndWeaponJobs(gearAndWeaponJobs.getItemID(),gearAndWeaponJobs.getJobID());
      
 
       
        } catch (SQLException e) {
            e.printStackTrace();;
            throw e;  
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (insertStmt != null) {
                insertStmt.close();
            }
        }
       // Return null if no rows were inserted
    }


    // Read by itemID
    public GearAndWeaponJobs getByItemID(int itemID) throws SQLException {
        String query = "SELECT * FROM GearAndWeaponJobs WHERE itemID = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = connectionManager.getConnection();  // Get connection
            statement = connection.prepareStatement(query);
            statement.setInt(1, itemID);  // Set the itemID
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                int jobID = resultSet.getInt("jobID");
                return new GearAndWeaponJobs(itemID, jobID);  // Return the found object
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
        return null;  // Return null if no record is found
    }


    // Read all
    public List<GearAndWeaponJobs> getAll() throws SQLException {
        List<GearAndWeaponJobs> items = new ArrayList<>();
        String query = "SELECT * FROM GearAndWeaponJobs";
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = connectionManager.getConnection();  // Get connection
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            
            while (resultSet.next()) {
                int itemID = resultSet.getInt("itemID");
                int jobID = resultSet.getInt("jobID");
                items.add(new GearAndWeaponJobs(itemID, jobID));  // Add to list
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
        }
        return items;  // Return the list of all items
    }


    // Update method in GearAndWeaponJobsDao
    public GearAndWeaponJobs update(GearAndWeaponJobs gearAndWeaponJobs) throws SQLException {
        String query = "UPDATE GearAndWeaponJobs SET jobID = ? WHERE itemID = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = connectionManager.getConnection();  // Get connection
            statement = connection.prepareStatement(query);
            statement.setInt(1, gearAndWeaponJobs.getJobID());  // Set the jobID to update
            statement.setInt(2, gearAndWeaponJobs.getItemID());  // Set the itemID for the condition
            
            if (statement.executeUpdate() > 0) {
                // If update is successful, return the updated object
                return getByItemID(gearAndWeaponJobs.getItemID());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
        return null;  // Return null if no rows were updated
    }


    // Delete method
    public boolean delete(int itemID) throws SQLException {
        String query = "DELETE FROM GearAndWeaponJobs WHERE itemID = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = connectionManager.getConnection();  // Get connection
            statement = connection.prepareStatement(query);
            statement.setInt(1, itemID);  // Set the itemID for deletion
            
            return statement.executeUpdate() > 0;  // Return true if deletion is successful
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
    }
}
