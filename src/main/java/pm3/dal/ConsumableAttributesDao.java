package pm3.dal;

import pm3.model.ConsumableAttributes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConsumableAttributesDao {
    protected ConnectionManager connectionManager;
    private static ConsumableAttributesDao instance = null;
    
    protected ConsumableAttributesDao() {
        connectionManager = new ConnectionManager();
    }
    
    public static ConsumableAttributesDao getInstance() {
        if (instance == null) {
            instance = new ConsumableAttributesDao();
        }
        return instance;
    }
    
  
    public ConsumableAttributes create(ConsumableAttributes consumableAttr) throws SQLException {
        String insertConsumableAttr = 
            "INSERT INTO ConsumableAttributes(ItemID, AttributeID, AttributeBonusCap, AttributeBonusPercent) " +
            "VALUES(?,?,?,?);";
        Connection connection = null;
        PreparedStatement insertStmt = null;
        
        try {
            connection = connectionManager.getConnection();
            insertStmt = connection.prepareStatement(insertConsumableAttr);
            insertStmt.setInt(1, consumableAttr.getItemID());
            insertStmt.setInt(2, consumableAttr.getAttributeID());
            insertStmt.setInt(3, consumableAttr.getAttributeBonusCap());
            insertStmt.setDouble(4, consumableAttr.getAttributeBonusPercent());
            insertStmt.executeUpdate();
            
            return new ConsumableAttributes(consumableAttr.getItemID(),consumableAttr.getAttributeID(),consumableAttr.getAttributeBonusCap(),consumableAttr.getAttributeBonusPercent());
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (insertStmt != null) {
                insertStmt.close();
            }
        }
    }
    
    /**
     * Get a ConsumableAttributes by its ItemID and AttributeID composite key.
     */
    public ConsumableAttributes getConsumableAttributeByItemIdAndAttributeId(int itemId, int attributeId) throws SQLException {
        String selectConsumableAttr = 
            "SELECT ItemID, AttributeID, AttributeBonusCap, AttributeBonusPercent " +
            "FROM ConsumableAttributes WHERE ItemID=? AND AttributeID=?;";
        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet results = null;
        
        try {
            connection = connectionManager.getConnection();
            selectStmt = connection.prepareStatement(selectConsumableAttr);
            selectStmt.setInt(1, itemId);
            selectStmt.setInt(2, attributeId);
            results = selectStmt.executeQuery();
            
            if (results.next()) {
                return new ConsumableAttributes(
                    results.getInt("ItemID"),
                    results.getInt("AttributeID"),
                    results.getInt("AttributeBonusCap"),
                    results.getDouble("AttributeBonusPercent")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (selectStmt != null) {
                selectStmt.close();
            }
            if (results != null) {
                results.close();
            }
        }
        return null;
    }
    
    /**
     * Get all attributes for a specific consumable item.
     */
    public List<ConsumableAttributes> getAttributesByItemId(int itemId) throws SQLException {
        List<ConsumableAttributes> attributes = new ArrayList<>();
        String selectAttributes = 
            "SELECT ItemID, AttributeID, AttributeBonusCap, AttributeBonusPercent " +
            "FROM ConsumableAttributes WHERE ItemID=?;";
        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet results = null;
        
        try {
            connection = connectionManager.getConnection();
            selectStmt = connection.prepareStatement(selectAttributes);
            selectStmt.setInt(1, itemId);
            results = selectStmt.executeQuery();
            
            while (results.next()) {
                attributes.add(new ConsumableAttributes(
                    results.getInt("ItemID"),
                    results.getInt("AttributeID"),
                    results.getInt("AttributeBonusCap"),
                    results.getDouble("AttributeBonusPercent")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (selectStmt != null) {
                selectStmt.close();
            }
            if (results != null) {
                results.close();
            }
        }
        return attributes;
    }
    
    /**
     * Update a ConsumableAttributes record.
     */
    public ConsumableAttributes updateConsumableAttribute(ConsumableAttributes consumableAttr) throws SQLException {
        String updateConsumableAttr = 
            "UPDATE ConsumableAttributes SET AttributeBonusCap=?, AttributeBonusPercent=? " +
            "WHERE ItemID=? AND AttributeID=?;";
        Connection connection = null;
        PreparedStatement updateStmt = null;
        
        try {
            connection = connectionManager.getConnection();
            updateStmt = connection.prepareStatement(updateConsumableAttr);
            updateStmt.setInt(1, consumableAttr.getAttributeBonusCap());
            updateStmt.setDouble(2, consumableAttr.getAttributeBonusPercent());
            updateStmt.setInt(3, consumableAttr.getItemID());
            updateStmt.setInt(4, consumableAttr.getAttributeID());
            updateStmt.executeUpdate();
            
            return consumableAttr;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (updateStmt != null) {
                updateStmt.close();
            }
        }
    }
    
    /**
     * Delete a ConsumableAttributes record.
     */
    public void delete(ConsumableAttributes consumableAttr) throws SQLException {
        String deleteConsumableAttr = 
            "DELETE FROM ConsumableAttributes WHERE ItemID=? AND AttributeID=?;";
        Connection connection = null;
        PreparedStatement deleteStmt = null;
        
        try {
            connection = connectionManager.getConnection();
            deleteStmt = connection.prepareStatement(deleteConsumableAttr);
            deleteStmt.setInt(1, consumableAttr.getItemID());
            deleteStmt.setInt(2, consumableAttr.getAttributeID());
            deleteStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (deleteStmt != null) {
                deleteStmt.close();
            }
        }
    }
}