package pm3.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import pm3.model.GearAndWeaponAttributes;

public class GearAndWeaponAttributesDao {
    protected ConnectionManager connectionManager;
    private static GearAndWeaponAttributesDao instance = null;
    protected GearAndWeaponAttributesDao() {
        connectionManager = new ConnectionManager();
    }
    
    public static GearAndWeaponAttributesDao getInstance() {
        if (instance == null) {
            instance = new GearAndWeaponAttributesDao();
        }
        return instance;
    }
    
    public GearAndWeaponAttributes create(GearAndWeaponAttributes gearAttr) throws SQLException {
        String insertGearAttr = "INSERT INTO GearAndWeaponAttributes(ItemID, AttributeID, AttributeBonus) VALUES(?,?,?);";
        Connection connection = null;
        PreparedStatement insertStmt = null;
        
        try {
            connection = connectionManager.getConnection();
            insertStmt = connection.prepareStatement(insertGearAttr);
            insertStmt.setInt(1, gearAttr.getItemID());
            insertStmt.setInt(2, gearAttr.getAttributeID());
            insertStmt.setInt(3, gearAttr.getAttributeBonus());
            insertStmt.executeUpdate();
            
            return new GearAndWeaponAttributes(
                gearAttr.getItemID(),
                gearAttr.getAttributeID(),
                gearAttr.getAttributeBonus()
            );
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
}