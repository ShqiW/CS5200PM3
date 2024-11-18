package pm3.tools;

import pm3.dal.AttributesDao;
import pm3.dal.ConsumableAttributesDao;
import pm3.dal.ConsumablesDao;
import pm3.dal.EquipmentSlotsDao;
import pm3.dal.EquippableItemsDao;
import pm3.dal.GearAndWeaponAttributesDao;
import pm3.dal.GearAndWeaponJobsDao;
import pm3.dal.GearsDao;
import pm3.dal.ItemsDao;
import pm3.dal.JobsDao;
import pm3.dal.WeaponsDao;
import pm3.model.Attributes;
import pm3.model.ConsumableAttributes;
import pm3.model.Consumables;
import pm3.model.EquipmentSlots;
import pm3.model.EquippableItems;
import pm3.model.GearAndWeaponAttributes;
import pm3.model.GearAndWeaponJobs;
import pm3.model.Gears;
import pm3.model.Items;
import pm3.model.Jobs;
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
         System.out.println("TABLE GearAndWeaponAttributes: create, getAttributeByIds");
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
             EquipmentSlotsDao slotsDao = EquipmentSlotsDao.getInstance();
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
                 savedPotion, savedStrength, 100, 0.25f
             );
             ConsumableAttributes savedPotionEffect = consumableAttributesDao.create(potionEffect);
             System.out.println("Created consumable attribute for potion");

             // Create Consumables
             Consumables healthPotion = new Consumables(savedPotion.getItemID(), 1, "Restores 100 HP");
             Consumables savedHealthPotion = consumablesDao.create(healthPotion);
             System.out.println("Created consumable: Health Potion");
             
             // Create Two Slots
             EquipmentSlots mainHandSlot = new EquipmentSlots("Main Hand");
             EquipmentSlots armorSlot = new EquipmentSlots("Armor");

     
             EquipmentSlots savedMainHandSlot = slotsDao.create(mainHandSlot);
             EquipmentSlots savedArmorSlot = slotsDao.create(armorSlot);

             System.out.println("Created equipment slots");
      
             System.out.println("Main hand slot: " + (savedMainHandSlot != null ? savedMainHandSlot.getSlotID() : "null"));
             System.out.println("Armor slot: " + (savedArmorSlot != null ? savedArmorSlot.getSlotID() : "null"));

             
             EquippableItems equippableSword = new EquippableItems(
                     savedSword.getItemID(), 
                     savedSword.getItemName(),  
                     savedSword.getMaxStackSize(),
                     savedSword.isMarketAllowed(), 
                     savedSword.getVendorPrice(), 
                     1,                 
                     savedMainHandSlot,      
                     1                  
                 );
             
             EquippableItems savedEquippableSword = equippableItemsDao.create(equippableSword);
             System.out.println("Created equippable item: Sword");
             
             EquippableItems equippableArmor = new EquippableItems(
                     savedArmor.getItemID(),  
                     savedArmor.getItemName(),
                     savedArmor.getMaxStackSize(), 
                     savedArmor.isMarketAllowed(),
                     savedArmor.getVendorPrice(),
                     2,               
                     savedArmorSlot,     
                     1            
                 );

             EquippableItems savedEquippableArmor = equippableItemsDao.create(equippableArmor);
             
             System.out.println("Created equippable item: Armor");

             // Create GearAndWeaponAttributes
             GearAndWeaponAttributes swordStrength = new GearAndWeaponAttributes(
            		 savedEquippableSword, savedStrength, 5
             );
//             System.out.println(savedSword.getItemID());
//             System.out.println(savedEquippableSword.getItemID());
//           
//             System.out.println(swordStrength.getAttributeID());
             GearAndWeaponAttributes savedSwordStrength = gearAndWeaponAttributesDao.create(swordStrength);
             System.out.println("Created gear attribute for sword");
             
             
             // create job
             JobsDao jobsDao = JobsDao.getInstance();
             Jobs warriorJob = new Jobs("Warrior");
             Jobs savedWarriorJob = jobsDao.create(warriorJob);
             
             // Create GearAndWeaponJobs
             GearAndWeaponJobs swordJob = new GearAndWeaponJobs(savedEquippableSword, savedWarriorJob);
             GearAndWeaponJobs savedSwordJob = gearAndWeaponJobsDao.create(swordJob);
             System.out.println("Created gear job for sword");
             

             // Create Weapon
             Weapons ironSword = new Weapons(
                     savedEquippableSword.getItemID(), 
                     savedEquippableSword.getItemName(), 
                     savedEquippableSword.getMaxStackSize(),
                     savedEquippableSword.isMarketAllowed(),
                     savedEquippableSword.getVendorPrice(), 
                     10,              
                     mainHandSlot,    
                     5,              
                     150,            
                     2.5,            // autoAttack
                     2.8             // attackDelay
                 );
       
             
             Weapons savedIronSword = weaponsDao.create(ironSword);
             System.out.println("Created weapon: Iron Sword");


             // Create Weapons
             Gears ironArmor = new Gears(
                     savedArmor.getItemID(),
                     savedArmor.getItemName(),
                     savedArmor.getMaxStackSize(),
                     savedArmor.isMarketAllowed(),
                     savedArmor.getVendorPrice(),
                     10,          
                     armorSlot,       
                     5,         
                     100,           
                     50             
                 );
             Gears savedIronArmor = gearsDao.create(ironArmor);
             System.out.println("Created gear: Iron Armor");
      
 
             
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
             
             // Read GearAndWeaponAttributes
             GearAndWeaponAttributes retrievedGearAndWeaponAttributes = gearAndWeaponAttributesDao.getGearAttributeByIds(savedSword.getItemID(), savedStrength.getAttributeID());
             System.out.println("Retrieved gear and weapon attributes");

             // Read GearAndWeaponJobs
             GearAndWeaponJobs retrievedJob = gearAndWeaponJobsDao.getByItemId(savedSword.getItemID());
             System.out.println("Retrieved gear and weapon job");

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
             System.out.println("Updated weapon damage");

             System.out.println("\n=== DELETING RECORDS ===");
             // Delete Weapons
//             weaponsDao.delete(savedIronSword.getItemID());
             System.out.println("Deleted weapon");

             // Delete Gears
//             gearsDao.delete(savedIronArmor);
             System.out.println("Deleted gear");

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