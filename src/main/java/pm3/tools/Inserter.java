package pm3.tools;

import pm3.dal.AttributesDao;
import pm3.dal.ConsumableAttributesDao;
import pm3.dal.ConsumablesDao;
import pm3.dal.EquippableItemsDao;
import pm3.dal.GearAndWeaponAttributesDao;
import pm3.dal.GearAndWeaponJobsDao;
import pm3.dal.GearsDao;
import pm3.dal.ItemsDao;
import pm3.dal.WeaponsDao;
import pm3.model.Attributes;
import pm3.model.ConsumableAttributes;
import pm3.model.Consumables;
import pm3.model.EquippableItems;
import pm3.model.GearAndWeaponAttributes;
import pm3.model.GearAndWeaponJobs;
import pm3.model.Gears;
import pm3.model.Items;
import pm3.model.Weapons;

import java.sql.SQLException;
import java.util.List;

public class Inserter {
    public static void main(String[] args) {
    	 System.out.println("--------------------------------------------------------------------");
         System.out.println("TABLE Items: create, getItemById, delete");
         System.out.println("TABLE Attributes: create, getAttributeByAttributeId");
         System.out.println("TABLE ConsumableAttributes: create, getConsumableAttributeByItemIdAndAttributeId, getAttributesByItemId, updateConsumableAttribute");
         System.out.println("TABLE Consumables: create, getConsumableByItemID, updateConsumable, updateDescription, getConsumablesByItemLevel, delete");
         System.out.println("TABLE EquippablesItems: create");
         System.out.println("TABLE GearAndWeaponAttributes: create");
         System.out.println("TABLE GearAndWeaponJobs: create, getItemByID");
         System.out.println("TABLE Gears: create, getGearByPartialNAme, delete");
     	 System.out.println("TABLE Weapons: create, getWeaponByItemID, getAllWeapons, update, delete");
     	 System.out.println("--------------------------------------------------------------------");
        try {
       
             // Initialize DAOs once
        	 ItemsDao itemsDao = ItemsDao.getInstance();
             AttributesDao attributesDao = AttributesDao.getInstance();
             ConsumableAttributesDao consumableAttributesDao = ConsumableAttributesDao.getInstance();
             ConsumablesDao consumablesDao = ConsumablesDao.getInstance();
             EquippableItemsDao equippableItemsDao = EquippableItemsDao.getInstance();
             GearAndWeaponAttributesDao gearAndWeaponAttributesDao = GearAndWeaponAttributesDao.getInstance();
             GearAndWeaponJobsDao gearAndWeaponJobsDao = GearAndWeaponJobsDao.getInstance();
             GearsDao gearsDao = GearsDao.getInstance();
             WeaponsDao weaponsDao = WeaponsDao.getInstance();
             
             
             System.out.println("=== CREATING RECORDS ===");
             // Create Items
             Items potion = new Items("Health Potion", 99, true, 100);
             Items savedPotion = itemsDao.create(potion);
             System.out.println("Created item: Health Potion");

             Items sword = new Items("Iron Sword", 1, true, 500);
             Items savedSword = itemsDao.create(sword);
             System.out.println("Created item: Iron Sword");

             Items armor = new Items("Iron Armor", 1, true, 450);
             Items savedArmor = itemsDao.create(armor);
             System.out.println("Created item: Iron Armor");

             // Create Attributes
             Attributes strength = new Attributes("Strength");
             Attributes savedStrength = attributesDao.create(strength);
             System.out.println("Created attribute: Strength");

             // Create ConsumableAttributes
             ConsumableAttributes potionEffect = new ConsumableAttributes(
                 savedPotion.getItemID(), savedStrength.getAttributeID(), 100, 0.25f
             );
             ConsumableAttributes savedPotionEffect = consumableAttributesDao.create(potionEffect);
             System.out.println("Created consumable attribute for potion");

             // Create Consumables
             Consumables healthPotion = new Consumables(savedPotion.getItemID(), 1, "Restores 100 HP");
             Consumables savedHealthPotion = consumablesDao.create(healthPotion);
             System.out.println("Created consumable: Health Potion");

             // Create EquippableItems
             EquippableItems equippableSword = new EquippableItems(savedSword.getItemID(), 1, 1, 1);
             equippableSword = equippableItemsDao.create(equippableSword);
             System.out.println("Created equippable item: Sword(waiting for equipment slot...)");

             EquippableItems equippableArmor = new EquippableItems(savedArmor.getItemID(), 2, 1, 1);
             equippableArmor = equippableItemsDao.create(equippableArmor);
             System.out.println("Created equippable item: Armor(waiting for equipment slot...)");

             // Create GearAndWeaponAttributes
             GearAndWeaponAttributes swordStrength = new GearAndWeaponAttributes(
                 savedSword.getItemID(), savedStrength.getAttributeID(), 5
             );
             System.out.println(savedSword.getItemID());
             System.out.println(savedStrength.getAttributeID());
             GearAndWeaponAttributes savedSwordStrength = gearAndWeaponAttributesDao.create(swordStrength);
             System.out.println("Created gear attribute for sword(waiting for equippableItems)");

             // Create GearAndWeaponJobs
             GearAndWeaponJobs swordJob = new GearAndWeaponJobs(savedSword.getItemID(), 1);
             GearAndWeaponJobs savedSwordJob = gearAndWeaponJobsDao.create(swordJob);
             System.out.println("Created gear job for sword (waiting for equippableItems)");

             // Create Gears
             Gears ironArmor = new Gears(savedArmor.getItemID()), 10, 5);
             Gears savedIronArmor = gearsDao.create(ironArmor);
             System.out.println("Created gear: Iron Armor(waiting for equipment slot...)");

             // Create Weapons
             Weapons ironSword = new Weapons(savedSword.getItemID(), 15, 2.5f, 2.8f);
             Weapons savedIronSword = weaponsDao.create(ironSword);
             System.out.println("Created weapon: Iron Sword(waiting for equipment slot...)");

             
             System.out.println("\n=== READING RECORDS ===");
             // Read Items
             Items retrievedItem = itemsDao.getItemById(savedPotion.getItemID());
             System.out.println("Retrieved item: " + retrievedItem.getItemName());

             // Read Attributes
             Attributes retrievedAttribute = attributesDao.getAttributeByAttributesID(savedStrength.getAttributeID());
             System.out.println("Retrieved attribute: " + retrievedAttribute.getAttributeName());

             // Read ConsumableAttributes
             ConsumableAttributes retrievedEffect = consumableAttributesDao
                 .getConsumableAttributeByItemIdAndAttributeId(savedPotion.getItemID(), savedStrength.getAttributeID());
             System.out.println("Retrieved consumable attribute");

             List<ConsumableAttributes> attributesList = consumableAttributesDao.getAttributesByItemId(savedPotion.getItemID());
             System.out.println("Retrieved consumable attributes list, count: " + attributesList.size());

             // Read Consumables
             Consumables retrievedConsumable = consumablesDao.getConsumableByItemId(savedPotion.getItemID());
             System.out.println("Retrieved consumable: " + retrievedConsumable.getDescription());

             List<Consumables> levelConsumables = consumablesDao.getConsumablesByItemLevel(1);
             System.out.println("Retrieved consumables by level, count: " + levelConsumables.size());

             // Read GearAndWeaponJobs
//             GearAndWeaponJobs retrievedJob = gearAndWeaponJobsDao.getItemByID(savedSword.getItemID());
             System.out.println("Retrieved gear and weapon job(need UPDATE!)");

             // Read Gears
             List<Gears> armorList = gearsDao.getGearByPartialName("Iron");
             System.out.println("Retrieved gear by partial name, count: " + armorList.size());

             // Read Weapons
             Weapons retrievedWeapon = weaponsDao.getWeaponByItemID(savedSword.getItemID());
             System.out.println("Retrieved weapon by ID");

             List<Weapons> allWeapons = weaponsDao.getAllWeapons();
             System.out.println("Retrieved all weapons, count: " + allWeapons.size());

             System.out.println("\n=== UPDATING RECORDS ===");
             // Update ConsumableAttributes
             savedPotionEffect.setAttributeBonusCap(150);
             consumableAttributesDao.updateConsumableAttribute(savedPotionEffect);
             System.out.println("Updated consumable attribute");

             // Update Consumables
             savedHealthPotion.setItemLevel(2);
             consumablesDao.updateConsumable(savedHealthPotion);
             System.out.println("Updated consumable level");

             consumablesDao.updateDescription(savedHealthPotion, "Restores 150 HP");
             System.out.println("Updated consumable description");

             // Update Weapons
//             savedIronSword.setPhysicalDamage(20);
//             weaponsDao.update(savedIronSword);
             System.out.println("Updated weapon damage(waiting for equipment slot...)");

             System.out.println("\n=== DELETING RECORDS ===");
             // Delete Weapons
//             weaponsDao.delete(savedIronSword.getItemID());
             System.out.println("Deleted weapon(waiting for equipment slot...)");

             // Delete Gears
//             gearsDao.delete(savedIronArmor);
             System.out.println("Deleted gear(waiting for equipment slot...)");

             // Delete Consumables
             consumablesDao.delete(savedHealthPotion);
             System.out.println("Deleted consumable");

             // Delete Items (should cascade)
             itemsDao.delete(savedPotion.getItemID());
             itemsDao.delete(savedSword.getItemID());
             itemsDao.delete(savedArmor.getItemID());
             System.out.println("Deleted items");

             System.out.println("\nAll operations completed successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}






//public class Inserter {
//
//	public static void main(String[] args) throws SQLException {
//		// TABLE Items
//		System.out.println("--------------------------------------------------------------------");
//		System.out.println("TABLE Items");
//		System.out.println("create, getItemById, delete");
//		
//		// Create an instance of ItemsDao
//		ItemsDao itemsDao = ItemsDao.getInstance();
//
//		// Create a new Item
//		Items newItem = new Items("Sword of Power", 10, true, 500);
//		Items createdItem = itemsDao.create(newItem);
//		System.out.println("Created Item: " + createdItem.getItemID() + " " + createdItem.getItemName());
//
//		// Test the delete method
//		boolean isDeleted = itemsDao.delete(createdItem.getItemID());
//		System.out.println("Item Deleted: " + isDeleted);
//
//		// Try to fetch the deleted item again
//		Items fetchedItem = itemsDao.getItemById(createdItem.getItemID());
//		if (fetchedItem == null) {
//			System.out.println("Item no longer exists in the database.");
//		} else {
//			System.out.println("Fetched Item after deletion: " + fetchedItem);
//		}
//
//		// TABLE Attributes
//		System.out.println("--------------------------------------------------------------------");
//		System.out.println("TABLE Attributes");
//		System.out.println("create, getAttributeByAttributeId");
//				
//		// DAO instance
//		AttributesDao attributesDao1 = AttributesDao.getInstance();
//
//		// INSERT an Attribute
//		Attributes attribute1 = new Attributes("Strength");
//		Attributes saved_attribute1 = attributesDao1.create(attribute1);
//		System.out.println("Inserted Attribute: id=" + saved_attribute1.getAttributeID() + ", name="
//				+ saved_attribute1.getAttributeName());
//
//		// INSERT another Attribute
//		Attributes attribute2 = new Attributes("Agility");
//		Attributes saved_attribute2 = attributesDao1.create(attribute2);
//		System.out.println("Inserted Attribute: id=" + saved_attribute2.getAttributeID() + ", name="
//				+ saved_attribute2.getAttributeName());
//
//		// READ Attribute by attributeID
//		Attributes fetched_attribute1 = attributesDao1.getAttributeByAttributesID(saved_attribute1.getAttributeID());
//		if (fetched_attribute1 != null) {
//			System.out.println("Read Attribute: id=" + fetched_attribute1.getAttributeID() + ", name="
//					+ fetched_attribute1.getAttributeName());
//		}
//		
//		// TABLE ConsumableAttributes
//		System.out.println("--------------------------------------------------------------------");
//		System.out.println("TABLE ConsumableAttributes");
//		System.out.println("create, getConsumableAttributeByItemIdAndAttributeId, getAttributesByItemId, updateConsumableAttribute");
//
//		// DAO Instances
//		AttributesDao attributesDao2 = AttributesDao.getInstance();
//		ConsumableAttributesDao consumableAttributesDao = ConsumableAttributesDao.getInstance();
//
//		// Insert a ConsumableAttribute entry
//		ItemsDao itemsDao2 = ItemsDao.getInstance();
//		ConsumableAttributesDao consumableAttributesDao2 = ConsumableAttributesDao.getInstance();
//		ConsumableAttributes savedConsumableAttribute = null;
//
//		try {
//			// Step 1: Insert a new Item into the Items table
//			Items newItem2 = new Items("Sword of Power", 10, true, 500);
//			Items createdItem2 = itemsDao2.create(newItem2);
//			System.out.println("Created Item: " + createdItem.getItemID() + ", " + createdItem.getItemName());
//
//			// Step 2: Insert a ConsumableAttribute for the created item
//			ConsumableAttributes consumableAttribute = new ConsumableAttributes(createdItem2.getItemID(), 1, 50, 0.15); 
//																								
//			savedConsumableAttribute = consumableAttributesDao2.create(consumableAttribute);
//			System.out.println("Inserted ConsumableAttribute: ItemID=" + savedConsumableAttribute.getItemID()
//					+ ", AttributeID=" + savedConsumableAttribute.getAttributeID() + ", BonusCap="
//					+ savedConsumableAttribute.getAttributeBonusCap() + ", BonusPercent="
//					+ savedConsumableAttribute.getAttributeBonusPercent());
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//
//		// Retrieve ConsumableAttribute by ItemID and Attribute
//		int itemId3 = 1; // Replace with actual itemId you want to work with
//		int attributeId3 = 5;
//		ConsumableAttributes retrievedConsumableAttribute = consumableAttributesDao
//				.getConsumableAttributeByItemIdAndAttributeId(itemId3, attributeId3);
//		if (retrievedConsumableAttribute == null) {
//			System.out.println(
//					"ConsumableAttribute not found for ItemID=" + itemId3 + ", AttributeID=" + attributeId3 + ".");
//		} else {
//			System.out.println("Retrieved ConsumableAttribute: ItemID=" + retrievedConsumableAttribute.getItemID()
//					+ ", AttributeID=" + retrievedConsumableAttribute.getAttributeID() + ", BonusCap="
//					+ retrievedConsumableAttribute.getAttributeBonusCap() + ", BonusPercent="
//					+ retrievedConsumableAttribute.getAttributeBonusPercent());
//		}
//
//		// Retrieve all ConsumableAttributes by ItemID
//		List<ConsumableAttributes> consumableAttributesList = consumableAttributesDao.getAttributesByItemId(1);
//		for (ConsumableAttributes ca : consumableAttributesList) {
//			System.out.println("Looping ConsumableAttributes: ItemID=" + ca.getItemID() + ", AttributeID="
//					+ ca.getAttributeID() + ", BonusCap=" + ca.getAttributeBonusCap() + ", BonusPercent="
//					+ ca.getAttributeBonusPercent());
//		}
//
//		// Update ConsumableAttribute
//		savedConsumableAttribute.setAttributeBonusCap(75);
//		savedConsumableAttribute.setAttributeBonusPercent(0.20);
//		savedConsumableAttribute = consumableAttributesDao.updateConsumableAttribute(savedConsumableAttribute);
//		System.out.println("Updated ConsumableAttribute: ItemID=" + savedConsumableAttribute.getItemID()
//				+ ", AttributeID=" + savedConsumableAttribute.getAttributeID() + ", NewBonusCap="
//				+ savedConsumableAttribute.getAttributeBonusCap() + ", NewBonusPercent="
//				+ savedConsumableAttribute.getAttributeBonusPercent());
//
//		// Delete ConsumableAttribute
//		consumableAttributesDao.delete(savedConsumableAttribute);
//		System.out.println("Deleted ConsumableAttribute with ItemID=" + savedConsumableAttribute.getItemID()
//				+ " and AttributeID=" + savedConsumableAttribute.getAttributeID());
//		
//		// TABLE Consumables
//		System.out.println("--------------------------------------------------------------------");
//		System.out.println("TABLE Consumables");
//		System.out.println("create, getConsumableByItemID, updateConsumable, updateDescription, getConsumablesByItemLevel, delete");
//		
//
//		// DAO Instances
//		ItemsDao itemsDao3 = ItemsDao.getInstance();
//		ConsumablesDao consumablesDao = ConsumablesDao.getInstance();
//		Consumables savedConsumableItem = null;
//
//		try {
//			// Step 1: Insert an item in the Items table if it doesn’t exist already
//			Items item = new Items("Basic Health Potion", 10, true, 50); // Example item
//			Items savedItem = itemsDao3.create(item);
//			System.out.println("Inserted Item: ItemID=" + savedItem.getItemID());
//
//			// Step 2: Create a new consumable item using the existing item's ID
//			Consumables consumableItem = new Consumables(savedItem.getItemID(), 10, "Health Potion");
//			savedConsumableItem = consumablesDao.create(consumableItem);
//			System.out.println("Inserted Consumable: ItemID=" + savedConsumableItem.getItemID() + ", ItemLevel="
//					+ savedConsumableItem.getItemLevel() + ", Description=" + savedConsumableItem.getDescription());
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//
//		// Retrieve consumable item by ItemID
//		Consumables retrievedItem = consumablesDao.getConsumableByItemId(savedConsumableItem.getItemID());
//		System.out.println("Retrieved Consumable: ItemID=" + retrievedItem.getItemID() + ", ItemLevel="
//				+ retrievedItem.getItemLevel() + ", Description=" + retrievedItem.getDescription());
//
//		// Update consumable item information
//		savedConsumableItem.setItemLevel(15);
//		savedConsumableItem.setDescription("Advanced Health Potion");
//		savedConsumableItem = consumablesDao.updateConsumable(savedConsumableItem);
//		System.out.println("Updated Consumable: ItemID=" + savedConsumableItem.getItemID() + ", NewItemLevel="
//				+ savedConsumableItem.getItemLevel() + ", NewDescription=" + savedConsumableItem.getDescription());
//
//		// Update only the description of the consumable item
//		Consumables updatedConsumableItem = consumablesDao.updateDescription(savedConsumableItem,
//				"Ultimate Health Potion");
//		System.out.println("Updated Description: ItemID=" + updatedConsumableItem.getItemID() + ", NewDescription="
//				+ updatedConsumableItem.getDescription());
//
//		// Retrieve all consumable items with a specific item level
//		int itemLevelToRetrieve = 10;
//		List<Consumables> consumablesByLevel = consumablesDao.getConsumablesByItemLevel(itemLevelToRetrieve);
//		for (Consumables item : consumablesByLevel) {
//			System.out.println("Consumable Item: ItemID=" + item.getItemID() + ", ItemLevel=" + item.getItemLevel()
//					+ ", Description=" + item.getDescription());
//		}
//
//		// Delete consumable item
//		consumablesDao.delete(savedConsumableItem);
//		System.out.println("Deleted Consumable: ItemID=" + savedConsumableItem.getItemID());
//		
//		// TABLE EquippableItems
//		System.out.println("--------------------------------------------------------------------");
//		System.out.println("TABLE EquippablesItems");
//		System.out.println("create");
//		
//		// DAO Instances
//		ItemsDao itemsDao4 = ItemsDao.getInstance();
//		EquippableItemsDao equippableItemsDao = EquippableItemsDao.getInstance();
//		GearAndWeaponAttributesDao gearAndWeaponAttributesDao = GearAndWeaponAttributesDao.getInstance();
//
//		try {
//		    // Step 1: First create the parent Items record
//		    Items item = new Items("Epic Sword",20,true,500);
//		    Items savedItem = itemsDao4.create(item);
//		    System.out.println("Created parent Item: ItemID=" + savedItem.getItemID());
//
//		    // Step 2: Now create the EquippableItems record using the ItemID from the parent
//		    int validSlotID = 1; // Ensure this ID exists in equipmentslots
//	
//		    EquippableItems equippableItem = new EquippableItems(
//		    	    "Epic Sword",             // itemName
//		    	    1,                        // maxStackSize
//		    	    true,                     // marketAllowed
//		    	    500,                      // vendorPrice
//		    	    10,                       // itemLevel
//		    	    validSlotID,              // slotID
//		    	    20                        // requiredLevel
//		    	);
//		    EquippableItems savedEquippableItem = equippableItemsDao.create(equippableItem);
//		    System.out.println("Created EquippableItem: ItemID=" + savedEquippableItem.getItemID());
//
//		    // Step 3: Create the GearAndWeaponAttributes record
//		    GearAndWeaponAttributes gearAttribute = new GearAndWeaponAttributes(
//		        savedEquippableItem.getItemID(), 
//		        5,      // attributeID
//		        100     // attributeBonus
//		    );
//		    GearAndWeaponAttributes savedGearAttribute = gearAndWeaponAttributesDao.create(gearAttribute);
//		    System.out.println("Created GearAndWeaponAttributes: ItemID=" + savedGearAttribute.getItemID() +
//		        ", AttributeID=" + savedGearAttribute.getAttributeID() +
//		        ", AttributeBonus=" + savedGearAttribute.getAttributeBonus());
//
//		} catch (SQLException e) {
//		    e.printStackTrace();
//		    // Consider rolling back the transaction if any step fails
//		}
//		
//
//		// TABLE GearAndWeaponJobs
//		System.out.println("--------------------------------------------------------------------");
//		System.out.println("TABLE GearAndWeaponJobs");
//		System.out.println("create, getItemByID");
//		// DAO Instances
//        ItemsDao itemsDao7 = ItemsDao.getInstance();
//        GearAndWeaponJobsDao gearAndWeaponJobsDao = GearAndWeaponJobsDao.getInstance();
//
//        // Step 1: Insert a new Item into the Items table
//        Items newItem4 = new Items("Sword of Power", 10, true, 500);  // Example item
//        Items savedItem4 = itemsDao7.create(newItem);
//        int itemID = savedItem4.getItemID();
//        System.out.println("Created Item: ID=" + itemID + ", Name=" + newItem4.getItemName());
//
//        // Step 2: Now insert into GearAndWeaponJobs with the valid itemID
//        GearAndWeaponJobs gearAndWeaponJob = new GearAndWeaponJobs(itemID, 10);  // Using the valid itemID
//        try {
//            GearAndWeaponJobs savedGearAndWeaponJob = gearAndWeaponJobsDao.create(gearAndWeaponJob);
//            System.out.println("Inserted GearAndWeaponJobs: ItemID=" + savedGearAndWeaponJob.getItemID() + ", JobID="
//                    + savedGearAndWeaponJob.getJobID());
//
//            // Retrieve GearAndWeaponJobs by ItemID
//            GearAndWeaponJobs retrievedGearAndWeaponJob = gearAndWeaponJobsDao.getByItemID(savedGearAndWeaponJob.getItemID());
//            System.out.println("Retrieved GearAndWeaponJobs: ItemID=" + retrievedGearAndWeaponJob.getItemID() + ", JobID="
//                    + retrievedGearAndWeaponJob.getJobID());
//
//            // Step 3: Update GearAndWeaponJobs with a new JobID
//            GearAndWeaponJobs updatedJob = new GearAndWeaponJobs(itemID, 15); // new jobID for the same itemID
//            updatedJob = gearAndWeaponJobsDao.update(updatedJob);  // This will update the jobID in the database.
//            System.out.println("Updated GearAndWeaponJob: " + updatedJob.getItemID() + " -> JobID: " + updatedJob.getJobID());
//
//            // Step 4: Delete GearAndWeaponJobs record by ItemID
//            gearAndWeaponJobsDao.delete(savedGearAndWeaponJob.getItemID());
//            System.out.println("Deleted GearAndWeaponJobs with ItemID=" + savedGearAndWeaponJob.getItemID());
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//     
//    
//		
//		// DAO Instance for GearAndWeaponJobs
//		GearAndWeaponJobsDao gearAndWeaponJobsDao2 = GearAndWeaponJobsDao.getInstance();
//
//		// Create a new GearAndWeaponJobs item
//		GearAndWeaponJobs gearAndWeaponJob2 = new GearAndWeaponJobs(1, 10); // Example itemID and jobID
//		GearAndWeaponJobs savedGearAndWeaponJob = gearAndWeaponJobsDao.create(gearAndWeaponJob2);
//		System.out.println("Inserted GearAndWeaponJobs: ItemID=" + savedGearAndWeaponJob.getItemID() + ", JobID="
//				+ savedGearAndWeaponJob.getJobID());
//
//		// Retrieve GearAndWeaponJobs by ItemID
//		GearAndWeaponJobs retrievedGearAndWeaponJob = gearAndWeaponJobsDao.getByItemID(gearAndWeaponJob.getItemID());
//		System.out.println("Retrieved GearAndWeaponJobs: ItemID=" + retrievedGearAndWeaponJob.getItemID() + ", JobID="
//				+ retrievedGearAndWeaponJob.getJobID());
//
//		// Update GearAndWeaponJobs
//
//		int existingItemID = 10;
//		GearAndWeaponJobs updatedJob = new GearAndWeaponJobs(existingItemID, 15); // new jobID for the same itemID
//		updatedJob = gearAndWeaponJobsDao.update(updatedJob); // This will update the jobID in the database.
//		System.out.println("Updated GearAndWeaponJob: " + updatedJob.getItemID() + " -> JobID: " + updatedJob.getJobID());
//
//		// Delete GearAndWeaponJobs
//		gearAndWeaponJobsDao.delete(gearAndWeaponJob.getItemID());
//		System.out.println("Deleted GearAndWeaponJobs with ItemID=" + gearAndWeaponJob.getItemID());
//
//		// Clean up by deleting the Attributes entries
//		attributesDao1.delete(saved_attribute1);
//		attributesDao1.delete(saved_attribute2);
//		System.out.println("Deleted Attributes: ID=" + saved_attribute1.getAttributeID() + " and ID="
//				+ saved_attribute2.getAttributeID());
//		
//		// TABLE Gears
//		System.out.println("--------------------------------------------------------------------");
//		System.out.println("TABLE Gears");
//		System.out.println("create, getGearByPartialNAme, delete");
//		GearsDao gearsDao = GearsDao.getInstance();
//        
//        try {
//        	// Test Create with all fields required by the schema
//            Gears newGear = new Gears(
//                "Steel Armor", 20, true, 1500, 5, 2, 10, 75, 40); // Create a Gears instance
//            Gears savedGear = gearsDao.create(newGear); // Save it to the database
//
//            // Output the created Gears details
//            System.out.println("Inserted Gear: " +
//                    "ItemID=" + savedGear.getItemID() +
//                    ", ItemName=" + savedGear.getItemName() +
//                    ", MaxStackSize=" + savedGear.getMaxStackSize() +
//                    ", MarketAllowed=" + savedGear.isMarketAllowed() +
//                    ", VendorPrice=" + savedGear.getVendorPrice() +
//                    ", ItemLevel=" + savedGear.getItemLevel() +
//                    ", SlotID=" + savedGear.getSlotID() +
//                    ", RequiredLevel=" + savedGear.getRequiredLevel() +
//                    ", DefenseRating=" + savedGear.getDefenseRating() +
//                    ", MagicDefenseRating=" + savedGear.getMagicDefenseRating());
////            
//            // Test Read by Partial Name
//            String partialName = "Steel";
//            List<Gears> gearsList = gearsDao.getGearByPartialName(partialName);
//            System.out.println("Gears containing '" + partialName + "':");
//            for (Gears gear : gearsList) {
//                System.out.println(" - ItemID=" + gear.getItemID() +
//                        ", ItemName=" + gear.getItemName() +
//                        ", DefenseRating=" + gear.getDefenseRating() +
//                        ", MagicDefenseRating=" + gear.getMagicDefenseRating());
//            }
//            
//            // Test Delete
//            gearsDao.delete(savedGear);
//            System.out.println("Deleted Gear with ItemID=" + savedGear.getItemID());
//
//            // Verify Delete by attempting to fetch the deleted item
//            List<Gears> deletedGearsList = gearsDao.getGearByPartialName(savedGear.getItemName());
//            if (deletedGearsList.isEmpty()) {
//                System.out.println("Verified: Gear with ItemID=" + savedGear.getItemID() + " successfully deleted.");
//            } else {
//                System.out.println("Warning: Gear with ItemID=" + savedGear.getItemID() + " was not deleted.");
//            }
//            
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        
//     // TABLE Weapons
//     System.out.println("--------------------------------------------------------------------");
//     System.out.println("TABLE Weapons");
//     System.out.println("create, getWeaponByItemID, getAllWeapons, update, delete");
//     
//  // DAO Instances
//     WeaponsDao weaponsDao = WeaponsDao.getInstance();
//     ItemsDao itemsDao5 = ItemsDao.getInstance();
//     GearsDao gearsDao5 = GearsDao.getInstance();
//
//   
//     // created...need equippableSlot
//     // Insert a new Weapon into the Items and EquippableItems tables
//     Items newItem5 = new Items("Sword of Power", 1, true, 1000);
//     Items createdItem4 = itemsDao5.create(newItem5);
//     System.out.println("Created Item: ID=" + createdItem4.getItemID() + ", " + createdItem4.getItemName());
//
//     // Create the Weapon associated with the Item
//     Weapons weapon = new Weapons(createdItem4.getItemID(), "Sword of Power", 1, true, 1000, 
//                                  10, 2, 5, 100, 1.5, 1.2);
//     Weapons createdWeapon = weaponsDao.create(weapon);
//     System.out.println("Inserted Weapon: ID=" + createdWeapon.getItemID() + ", Name=" + createdWeapon.getItemName() +
//                        ", PhysicalDamage=" + createdWeapon.getPhysicalDamage() + ", AttackDelay=" + createdWeapon.getAttackDelay());
//
//     // Retrieve Weapon by ItemID
//     int weaponId = createdWeapon.getItemID(); // Retrieve using the weapon's ItemID
//     Weapons retrievedWeapon = weaponsDao.getWeaponByItemID(weaponId);
//     if (retrievedWeapon == null) {
//         System.out.println("Weapon not found for ItemID=" + weaponId + ".");
//     } else {
//         System.out.println("Retrieved Weapon: ID=" + retrievedWeapon.getItemID() + ", Name=" + retrievedWeapon.getItemName() +
//                            ", PhysicalDamage=" + retrievedWeapon.getPhysicalDamage() + ", AttackDelay=" + retrievedWeapon.getAttackDelay());
//     }
//
//     // Retrieve all Weapons
//     List<Weapons> weaponsList = weaponsDao.getAllWeapons();
//     for (Weapons w : weaponsList) {
//         System.out.println("Weapon in List: ID=" + w.getItemID() + ", Name=" + w.getItemName() +
//                            ", PhysicalDamage=" + w.getPhysicalDamage() + ", AttackDelay=" + w.getAttackDelay());
//     }
//
//     // Update Weapon
//     retrievedWeapon.setPhysicalDamage(120);
//     retrievedWeapon.setAttackDelay(1.4);
//     boolean updateSuccess = weaponsDao.update(retrievedWeapon);
//     if (updateSuccess) {
//         System.out.println("Updated Weapon: ID=" + retrievedWeapon.getItemID() + ", New PhysicalDamage=" +
//                            retrievedWeapon.getPhysicalDamage() + ", New AttackDelay=" + retrievedWeapon.getAttackDelay());
//     } else {
//         System.out.println("Weapon update failed.");
//     }
//
//     // Delete Weapon
//     boolean deleteSuccess = weaponsDao.delete(retrievedWeapon.getItemID());
//     if (deleteSuccess) {
//         System.out.println("Deleted Weapon: ID=" + retrievedWeapon.getItemID());
//     } else {
//         System.out.println("Weapon deletion failed.");
//     }
//
//
//	}
//
//}
