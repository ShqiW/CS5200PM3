package pm3.dal;

import pm3.model.Items;
import pm3.model.Weapons;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WeaponsDao {
	protected ConnectionManager connectionManager;
    private static WeaponsDao instance = null;

    protected WeaponsDao() {
        connectionManager = new ConnectionManager();
    }
    
    public static WeaponsDao getInstance() {
        if (instance == null) {
            instance = new WeaponsDao();
        }
        return instance;
    }
   

    // Create
    public Weapons create(Weapons weapon) throws SQLException {
        // First create an entry in Items, followed by EquippableItems
        Items item = ItemsDao.getInstance().create(new Items(
            weapon.getItemName(), weapon.getMaxStackSize(), 
            weapon.isMarketAllowed(), weapon.getVendorPrice()));
//
//        String insertEquippableItem = "INSERT INTO EquippableItems(ItemID, ItemLevel, SlotID, RequiredLevel) VALUES (?, ?, ?, ?)";
        String insertWeapon = "INSERT INTO Weapons(ItemID, PhysicalDamage, AutoAttack, AttackDelay) VALUES (?, ?, ?, ?)";

        Connection connection = null;
        PreparedStatement equippableStmt = null;
        PreparedStatement weaponStmt = null;

        try {
            connection = connectionManager.getConnection();
            
//            // Insert into EquippableItems
//            equippableStmt = connection.prepareStatement(insertEquippableItem);
//            equippableStmt.setInt(1, item.getItemID());
//            equippableStmt.setInt(2, weapon.getItemLevel());
//            equippableStmt.setInt(3, weapon.getSlotID());
//            equippableStmt.setInt(4, weapon.getRequiredLevel());
//            equippableStmt.executeUpdate();

            // Insert into Weapons
            weaponStmt = connection.prepareStatement(insertWeapon);
            weaponStmt.setInt(1, item.getItemID());
            weaponStmt.setInt(2, weapon.getPhysicalDamage());
            weaponStmt.setDouble(3, weapon.getAutoAttack());
            weaponStmt.setDouble(4, weapon.getAttackDelay());
            weaponStmt.executeUpdate();

            // Return the combined Weapon object
            return new Weapons(item.getItemID(), item.getItemName(), item.getMaxStackSize(), 
                               item.isMarketAllowed(), item.getVendorPrice(), weapon.getItemLevel(), 
                               weapon.getSlotID(), weapon.getRequiredLevel(), 
                               weapon.getPhysicalDamage(), weapon.getAutoAttack(), weapon.getAttackDelay());
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) connection.close();
            if (equippableStmt != null) equippableStmt.close();
            if (weaponStmt != null) weaponStmt.close();
        }
    }


 // Read by itemID
    public Weapons getWeaponByItemID(int itemID) throws SQLException {
        String query = "SELECT i.ItemID, i.ItemName, i.MaxStackSize, i.MarketAllowed, i.VendorPrice, " +
                       "e.ItemLevel, e.SlotID, e.RequiredLevel, w.PhysicalDamage, w.AutoAttack, w.AttackDelay " +
                       "FROM Items i " +
                       "JOIN EquippableItems e ON i.ItemID = e.ItemID " +
                       "JOIN Weapons w ON e.ItemID = w.ItemID " +
                       "WHERE i.ItemID = ?";
        
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();  // Get the connection from the connection manager
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, itemID);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return new Weapons(
                        resultSet.getInt("ItemID"),
                        resultSet.getString("ItemName"),
                        resultSet.getInt("MaxStackSize"),
                        resultSet.getBoolean("MarketAllowed"),
                        resultSet.getInt("VendorPrice"),
                        resultSet.getInt("ItemLevel"),
                        resultSet.getInt("SlotID"),
                        resultSet.getInt("RequiredLevel"),
                        resultSet.getInt("PhysicalDamage"),
                        resultSet.getDouble("AutoAttack"),
                        resultSet.getDouble("AttackDelay")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;  // rethrow to handle the exception at the caller level
        } finally {
            if (connection != null) {
                connection.close();  // Ensure the connection is closed
            }
        }
        return null;
    }

    // Read all
    public List<Weapons> getAllWeapons() throws SQLException {
        List<Weapons> weaponsList = new ArrayList<>();
        String query = "SELECT i.ItemID, i.ItemName, i.MaxStackSize, i.MarketAllowed, i.VendorPrice, " +
                       "e.ItemLevel, e.SlotID, e.RequiredLevel, w.PhysicalDamage, w.AutoAttack, w.AttackDelay " +
                       "FROM Items i " +
                       "JOIN EquippableItems e ON i.ItemID = e.ItemID " +
                       "JOIN Weapons w ON e.ItemID = w.ItemID";
        
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();  // Get the connection from the connection manager
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next()) {
                    Weapons weapon = new Weapons(
                        resultSet.getInt("ItemID"),
                        resultSet.getString("ItemName"),
                        resultSet.getInt("MaxStackSize"),
                        resultSet.getBoolean("MarketAllowed"),
                        resultSet.getInt("VendorPrice"),
                        resultSet.getInt("ItemLevel"),
                        resultSet.getInt("SlotID"),
                        resultSet.getInt("RequiredLevel"),
                        resultSet.getInt("PhysicalDamage"),
                        resultSet.getDouble("AutoAttack"),
                        resultSet.getDouble("AttackDelay")
                    );
                    weaponsList.add(weapon);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;  // rethrow to handle the exception at the caller level
        } finally {
            if (connection != null) {
                connection.close();  // Ensure the connection is closed
            }
        }
        return weaponsList;
    }

    // Update
    public boolean update(Weapons weapon) throws SQLException {
        String updateItems = "UPDATE Items SET ItemName = ?, MaxStackSize = ?, MarketAllowed = ?, VendorPrice = ? WHERE ItemID = ?";
        String updateEquippableItems = "UPDATE EquippableItems SET ItemLevel = ?, SlotID = ?, RequiredLevel = ? WHERE ItemID = ?";
        String updateWeapons = "UPDATE Weapons SET PhysicalDamage = ?, AutoAttack = ?, AttackDelay = ? WHERE ItemID = ?";

        Connection connection = null;
        try {
            connection = connectionManager.getConnection();  // Get the connection from the connection manager
            connection.setAutoCommit(false); // Start transaction

            try (PreparedStatement itemsStmt = connection.prepareStatement(updateItems);
                 PreparedStatement equippableStmt = connection.prepareStatement(updateEquippableItems);
                 PreparedStatement weaponStmt = connection.prepareStatement(updateWeapons)) {
                
                // Update Items
                itemsStmt.setString(1, weapon.getItemName());
                itemsStmt.setInt(2, weapon.getMaxStackSize());
                itemsStmt.setBoolean(3, weapon.isMarketAllowed());
                itemsStmt.setInt(4, weapon.getVendorPrice());
                itemsStmt.setInt(5, weapon.getItemID());
                itemsStmt.executeUpdate();

                // Update EquippableItems
                equippableStmt.setInt(1, weapon.getItemLevel());
                equippableStmt.setInt(2, weapon.getSlotID());
                equippableStmt.setInt(3, weapon.getRequiredLevel());
                equippableStmt.setInt(4, weapon.getItemID());
                equippableStmt.executeUpdate();

                // Update Weapons
                weaponStmt.setInt(1, weapon.getPhysicalDamage());
                weaponStmt.setDouble(2, weapon.getAutoAttack());
                weaponStmt.setDouble(3, weapon.getAttackDelay());
                weaponStmt.setInt(4, weapon.getItemID());
                weaponStmt.executeUpdate();

                connection.commit(); // Commit transaction
                return true;
            } catch (SQLException e) {
                connection.rollback();  // Rollback transaction on error
                e.printStackTrace();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;  // rethrow to handle the exception at the caller level
        } finally {
            if (connection != null) {
                connection.close();  // Ensure the connection is closed
            }
        }
    }

    // Delete
    public boolean delete(int itemID) throws SQLException {
        String deleteWeapons = "DELETE FROM Weapons WHERE ItemID = ?";
        String deleteEquippableItems = "DELETE FROM EquippableItems WHERE ItemID = ?";
        String deleteItems = "DELETE FROM Items WHERE ItemID = ?";

        Connection connection = null;
        try {
            connection = connectionManager.getConnection();  // Get the connection from the connection manager
            connection.setAutoCommit(false); // Start transaction

            try (PreparedStatement weaponStmt = connection.prepareStatement(deleteWeapons);
                 PreparedStatement equippableStmt = connection.prepareStatement(deleteEquippableItems);
                 PreparedStatement itemsStmt = connection.prepareStatement(deleteItems)) {
                
                // Delete from Weapons
                weaponStmt.setInt(1, itemID);
                weaponStmt.executeUpdate();

                // Delete from EquippableItems
                equippableStmt.setInt(1, itemID);
                equippableStmt.executeUpdate();

                // Delete from Items
                itemsStmt.setInt(1, itemID);
                itemsStmt.executeUpdate();

                connection.commit(); // Commit transaction
                return true;
            } catch (SQLException e) {
                connection.rollback();  // Rollback transaction on error
                e.printStackTrace();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; 
        } finally {
            if (connection != null) {
                connection.close();  // Ensure the connection is closed
            }
        }
    }
}
