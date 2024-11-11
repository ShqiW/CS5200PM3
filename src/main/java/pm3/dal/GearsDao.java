package pm3.dal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import pm3.model.Gears;
import pm3.model.Items;

public class GearsDao {
    protected ConnectionManager connectionManager;
    private static GearsDao instance = null;
    
    protected GearsDao() {
        connectionManager = new ConnectionManager();
    }
    
    public static GearsDao getInstance() {
        if (instance == null) {
            instance = new GearsDao();
        }
        return instance;
    }
    
    public Gears create(Gears gear) throws SQLException {
        // First create an entry in Items, followed by EquippableItems
        Items item = ItemsDao.getInstance().create(new Items(
            gear.getItemName(), gear.getMaxStackSize(), 
            gear.isMarketAllowed(), gear.getVendorPrice()));

//        String insertEquippableItem = "INSERT INTO EquippableItems(ItemID, ItemLevel, SlotID, RequiredLevel) VALUES (?, ?, ?, ?)";
        String insertGear = "INSERT INTO Gears(ItemID, DefenseRating, MagicDefenseRating) VALUES (?, ?, ?)";

        Connection connection = null;
        PreparedStatement equippableStmt = null;
        PreparedStatement gearStmt = null;

        try {
            connection = connectionManager.getConnection();
            
//            // Insert into EquippableItems
//            equippableStmt = connection.prepareStatement(insertEquippableItem);
//            equippableStmt.setInt(1, item.getItemID());
//            equippableStmt.setInt(2, gear.getItemLevel());
//            equippableStmt.setInt(3, gear.getSlotID());
//            equippableStmt.setInt(4, gear.getRequiredLevel());
//            equippableStmt.executeUpdate();

            // Insert into Gears
            gearStmt = connection.prepareStatement(insertGear);
            gearStmt.setInt(1, item.getItemID());
            gearStmt.setInt(2, gear.getDefenseRating());
            gearStmt.setInt(3, gear.getMagicDefenseRating());
            gearStmt.executeUpdate();

            // Return the combined Gear object
            return new Gears(item.getItemID(), item.getItemName(), item.getMaxStackSize(), 
                             item.isMarketAllowed(), item.getVendorPrice(), gear.getItemLevel(), 
                             gear.getSlotID(), gear.getRequiredLevel(), 
                             gear.getDefenseRating(), gear.getMagicDefenseRating());
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) connection.close();
            if (equippableStmt != null) equippableStmt.close();
            if (gearStmt != null) gearStmt.close();
        }
    }

    // Read by partial name
    public List<Gears> getGearByPartialName(String name) throws SQLException {
        List<Gears> gearsList = new ArrayList<>();
        String selectGears = "SELECT * FROM Gears INNER JOIN Items ON Gears.ItemID = Items.ItemID " +
                             "WHERE Items.ItemName LIKE ?";
        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet results = null;
        
        try {
            connection = connectionManager.getConnection();
            selectStmt = connection.prepareStatement(selectGears);
            selectStmt.setString(1, "%" + name + "%");
            results = selectStmt.executeQuery();
            
            while (results.next()) {
                Gears gear = new Gears(
                    results.getInt("ItemID"),
                    results.getString("ItemName"),
                    results.getInt("MaxStackSize"),
                    results.getBoolean("MarketAllowed"),
                    results.getInt("VendorPrice"),
                    results.getInt("ItemLevel"),
                    results.getInt("SlotID"),
                    results.getInt("RequiredLevel"),
                    results.getInt("DefenseRating"),
                    results.getInt("MagicDefenseRating")
                );
                gearsList.add(gear);
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
        return gearsList;
    }

    
    // Delete
    public void delete(Gears gearItem) throws SQLException {
        String deleteGear = "DELETE FROM Gears WHERE ItemID = ?";
        Connection connection = null;
        PreparedStatement deleteStmt = null;
        
        try {
            connection = connectionManager.getConnection();
            deleteStmt = connection.prepareStatement(deleteGear);
            deleteStmt.setInt(1, gearItem.getItemID());
            deleteStmt.executeUpdate();
            
            // Also delete from Items table if needed
            ItemsDao.getInstance().delete(gearItem.getItemID());
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
