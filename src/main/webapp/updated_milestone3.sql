-- Drop schema if it exists and recreate it
DROP SCHEMA IF EXISTS CS5200Project;
CREATE SCHEMA CS5200Project;
USE CS5200Project;

-- Create Players table
CREATE TABLE Players (
    playerID INT AUTO_INCREMENT,
    userName VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    CONSTRAINT PK_Player PRIMARY KEY (playerID)
);

-- Create CharacterInfo table
CREATE TABLE CharacterInfo (
    characterID INT AUTO_INCREMENT,
    playerID INT,
    firstName VARCHAR(50) UNIQUE NOT NULL,
    lastName VARCHAR(50) UNIQUE NOT NULL,
    maxHP INT NOT NULL,
    CONSTRAINT PK_Character PRIMARY KEY (characterID),
    CONSTRAINT FK_Character_Player FOREIGN KEY (playerID) 
        REFERENCES Players(playerID) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Create CharacterJobs table
CREATE TABLE Jobs (
    jobID INT AUTO_INCREMENT,
    jobName VARCHAR(50) NOT NULL,
    CONSTRAINT PK_Jobs PRIMARY KEY (jobID)
);

-- Create CharacterJobs table
CREATE TABLE CharacterJobs (
    characterID INT,
    jobID INT,
    level INT NOT NULL,
    experiencePoints INT NOT NULL,
    isCurrentJob BOOLEAN NOT NULL DEFAULT 0,
    CONSTRAINT PK_CharacterJobs PRIMARY KEY (characterID, jobID),
    CONSTRAINT FK_CharacterJobs_Character FOREIGN KEY (characterID) 
        REFERENCES CharacterInfo(characterID) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FK_CharacterJobs_Job FOREIGN KEY (jobID) 
        REFERENCES Jobs(jobID) ON DELETE CASCADE ON UPDATE CASCADE
);

-- create currency tables
CREATE TABLE Currencies (
	currencyID INT AUTO_INCREMENT,
	currencyName VARCHAR(50) NOT NULL, 
    max_amount INT NOT NULL,
    weeklycap INT NOT NULL,
    CONSTRAINT pk_Currencies_currencyID PRIMARY KEY (currencyID)
);

CREATE TABLE CharacterCurrencies (
    characterID INT,
    currencyID INT,
    totalAmountOwned INT NOT NULL,
    amountThisWeek INT NOT NULL,
    CONSTRAINT pk_CharacterCurrencies_characterID_currencyID PRIMARY KEY (characterID,currencyID),
    CONSTRAINT fk_CharacterCurrencies_characterID 
		FOREIGN KEY (characterID) REFERENCES CharacterInfo(characterID)
		ON DELETE CASCADE 
		ON UPDATE CASCADE,
	CONSTRAINT fk_CharacterCurrencies_currencyID 
		FOREIGN KEY (currencyID) REFERENCES Currencies(currencyID)
	ON DELETE CASCADE 
    ON UPDATE CASCADE
);

-- Create EquipmentSlots table (required by CharacterEquipments)
CREATE TABLE EquipmentSlots (
    slotID INT AUTO_INCREMENT,
    slotName VARCHAR(50) NOT NULL,
    CONSTRAINT PK_EquipmentSlot PRIMARY KEY (slotID)
);

CREATE TABLE Items (
    itemID INT AUTO_INCREMENT,
    itemName VARCHAR(200) NOT NULL,
    maxStackSize INT NOT NULL,
    marketAllowed BOOLEAN NOT NULL,
    vendorPrice INT NOT NULL,
    CONSTRAINT pk_Items_itemID PRIMARY KEY (itemID)
);

CREATE TABLE EquippableItems(
	itemID INT,
    itemLevel INT NOT NULL, 
    slotID INT,
    requiredLevel INT NOT NULL,
    CONSTRAINT pk_EquippableItems_itemID PRIMARY KEY(itemID), 
    CONSTRAINT fk_EquippableItems_slotID FOREIGN KEY (slotID) REFERENCES EquipmentSlots(slotID)
		ON DELETE CASCADE 
		ON UPDATE CASCADE,
	CONSTRAINT fk_EquippableItems_itemID FOREIGN KEY (itemID) REFERENCES Items(itemID)
		ON DELETE CASCADE 
		ON UPDATE CASCADE
);

CREATE TABLE Gears (
    itemID INT,
    defenseRating INT NOT NULL,
    magicDefenseRating INT NOT NULL,
    CONSTRAINT pk_Gears_itemID PRIMARY KEY (itemID),
    CONSTRAINT fk_Gears_itemID FOREIGN KEY (itemID) 
        REFERENCES EquippableItems(itemID) 
        ON DELETE CASCADE 
        ON UPDATE CASCADE
);

CREATE TABLE Weapons (
    itemID INT,
    physicalDamage INT NOT NULL,
	autoAttack DECIMAL(10, 2) NOT NULL,
    attackDelay DECIMAL(10, 2) NOT NULL,
    CONSTRAINT pk_Weapons_itemID PRIMARY KEY (itemID),
    CONSTRAINT fk_Weapons_itemID FOREIGN KEY (itemID) 
        REFERENCES EquippableItems(itemID) 
        ON DELETE CASCADE 
        ON UPDATE CASCADE
);

CREATE TABLE Consumables (
    itemID INT,
    itemLevel INT NOT NULL,
    description VARCHAR(500) NOT NULL,
    CONSTRAINT pk_Consumables_itemID PRIMARY KEY (itemID),
    CONSTRAINT fk_Consumables_itemID FOREIGN KEY (itemID) 
        REFERENCES Items(itemID) 
        ON DELETE CASCADE 
        ON UPDATE CASCADE
);

CREATE TABLE GearAndWeaponJobs(
	itemID INT,
    jobID INT,
    CONSTRAINT pk_GearAndWeaponJobs_itemID_jobID PRIMARY KEY (itemID, jobID),
    CONSTRAINT fk_GearAndWeaponJobs_itemID FOREIGN KEY (itemID)
		REFERENCES EquippableItems(itemID)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
	CONSTRAINT fk_GearAndWeaponJobs_jobID FOREIGN KEY (jobID)
		REFERENCES Jobs(jobID)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE InventoryPositions(
	characterID INT,
    stackPosition INT,
    itemID INT NOT NULL,
    stackSize INT NOT NULL,
    CONSTRAINT pk_InventoryPositions_characterID_stackPosition PRIMARY KEY(characterID, stackPosition),
    CONSTRAINT fk_InventoryPositions_itemID FOREIGN KEY (itemID) REFERENCES Items(itemID) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_InventoryPositions_characterID FOREIGN KEY (characterID) REFERENCES CharacterInfo(characterID) ON UPDATE CASCADE ON DELETE CASCADE
    );
    
-- Create CharacterEquipments table
CREATE TABLE CharacterEquipments (
    characterID INT,
    slotID INT,
    itemID INT,
	CONSTRAINT pk_character_slot PRIMARY KEY (characterID, slotID),
    CONSTRAINT fk_CharacterEquipments_slotID FOREIGN KEY (slotID) REFERENCES EquipmentSlots(slotID)
		ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_CharacterEquipments_itemID FOREIGN KEY (itemID)
        REFERENCES EquippableItems(itemID)
		ON DELETE CASCADE
        ON UPDATE CASCADE,
	CONSTRAINT fk_CharacterEquipments_characterId FOREIGN KEY(characterID) REFERENCES CharacterInfo(characterID)
    	ON DELETE CASCADE
        ON UPDATE CASCADE
);


CREATE TABLE Attributes (
    attributeID INT AUTO_INCREMENT PRIMARY KEY,
    attributeName VARCHAR(200) NOT NULL
);

CREATE TABLE CharacterAttributes(
	attributeID INT,
    characterID INT,
    attributeValue INT NOT NULL,
    CONSTRAINT pk_CharacterAttributes_attributeID_characterID PRIMARY KEY(attributeID, characterID),
    CONSTRAINT fk_CharacterAttributes_attributeID FOREIGN KEY (attributeID) REFERENCES Attributes(attributeID) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_CharacterAttributes_characterID FOREIGN KEY (characterID) REFERENCES CharacterInfo(characterID) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE GearAndWeaponAttributes (
    itemID INT,
    attributeID INT,
    attributeBonus INT NOT NULL,
    CONSTRAINT pk_GearAndWeaponAttributes_itemID_attributeID PRIMARY KEY (itemID, attributeID),
    CONSTRAINT fk_GearAndWeaponAttributes_itemID FOREIGN KEY (itemID) 
        REFERENCES EquippableItems(itemID) 
        ON DELETE CASCADE 
        ON UPDATE CASCADE,
    CONSTRAINT fk_GearAndWeaponAttributes_attributeID FOREIGN KEY (attributeID) 
        REFERENCES Attributes(attributeID) 
        ON DELETE CASCADE 
        ON UPDATE CASCADE
);

CREATE TABLE ConsumableAttributes (
    itemID INT,
    attributeID INT,
    attributeBonusCap INT NOT NULL,
    attributeBonusPercent DECIMAL(3, 2) NOT NULL,
    CONSTRAINT pk_ConsumableAttributes_itemID_attributeID PRIMARY KEY (itemID, attributeID),
    CONSTRAINT fk_ConsumableAttributes_itemID FOREIGN KEY (itemID) 
        REFERENCES Items(itemID) 
        ON DELETE CASCADE 
        ON UPDATE CASCADE,
    CONSTRAINT fk_ConsumableAttributes_attributeID FOREIGN KEY (attributeID) 
        REFERENCES Attributes(attributeID) 
        ON DELETE CASCADE 
        ON UPDATE CASCADE
);

-- PM3 sample data for test SQ
-- Insert into Players table
INSERT INTO Players (userName, email)
VALUES ('player1', 'player1@example.com');

-- Insert into CharacterInfo table
INSERT INTO CharacterInfo (playerID, firstName, lastName, maxHP)
VALUES (1, 'John', 'Doe', 500);

-- Insert into Jobs table
INSERT INTO Jobs (jobName)
VALUES ('Warrior');

-- Insert into CharacterJobs table
INSERT INTO CharacterJobs (characterID, jobID, level, experiencePoints, isCurrentJob)
VALUES (1, 1, 10, 5000, true);

-- Insert into Currencies table
INSERT INTO Currencies (currencyName, max_amount, weeklycap)
VALUES ('Gold', 100000, 10000);

-- Insert into CharacterCurrencies table
INSERT INTO CharacterCurrencies (characterID, currencyID, totalAmountOwned, amountThisWeek)
VALUES (1, 1, 500, 100);

-- Insert into EquipmentSlots table
INSERT INTO EquipmentSlots (slotName)
VALUES ('Head');

-- Insert into Items table
INSERT INTO Items (itemName, maxStackSize, marketAllowed, vendorPrice)
VALUES ('Health Potion', 99, true, 100);

-- Insert into EquippableItems table
INSERT INTO EquippableItems (itemID, itemLevel, slotID, requiredLevel)
VALUES (1, 10, 1, 5);

-- Insert into Gears table
INSERT INTO Gears (itemID, defenseRating, magicDefenseRating)
VALUES (1, 50, 30);

-- Insert into Weapons table
INSERT INTO Weapons (itemID, physicalDamage, autoAttack, attackDelay)
VALUES (1, 75, 1.5, 2.0);

-- Insert into Consumables table
INSERT INTO Consumables (itemID, itemLevel, description)
VALUES (1, 1, 'Restores 100 HP');

-- Insert into GearAndWeaponJobs table
INSERT INTO GearAndWeaponJobs (itemID, jobID)
VALUES (1, 1);

-- Insert into InventoryPositions table
INSERT INTO InventoryPositions (characterID, stackPosition, itemID, stackSize)
VALUES (1, 1, 1, 5);

-- Insert into CharacterEquipments table
INSERT INTO CharacterEquipments (characterID, slotID, itemID)
VALUES (1, 1, 1);

-- Insert into Attributes table
INSERT INTO Attributes (attributeName)
VALUES ('Strength');

-- Insert into CharacterAttributes table
INSERT INTO CharacterAttributes (attributeID, characterID, attributeValue)
VALUES (1, 1, 10);

-- Insert into GearAndWeaponAttributes table
INSERT INTO GearAndWeaponAttributes (itemID, attributeID, attributeBonus)
VALUES (1, 1, 5);

-- Insert into ConsumableAttributes table
INSERT INTO ConsumableAttributes (itemID, attributeID, attributeBonusCap, attributeBonusPercent)
VALUES (1, 1, 50, 0.25);




-- PM2
-- Insert sample data into Players
-- INSERT INTO Players (userName, email) VALUES 
-- ('PlayerOne', 'player1@example.com'),
-- ('PlayerTwo', 'player2@example.com'),
-- ('PlayerThree', 'player3@example.com'),
-- ('PlayerFour', 'player4@example.com'),
-- ('PlayerFive', 'player5@example.com'),
-- ('PlayerSix', 'player6@example.com'),
-- ('PlayerSeven', 'player7@example.com'),
-- ('PlayerEight', 'player8@example.com'),
-- ('PlayerNine', 'player9@example.com'),
-- ('PlayerTen', 'player10@example.com');

-- Insert sample data into CharacterInfo
-- INSERT INTO CharacterInfo (playerID, firstName, lastName, maxHP) VALUES 
-- (1, 'John', 'Doe', 100),
-- (2, 'Jane', 'Smith', 120),
-- (3, 'Bob', 'Brown', 110),
-- (4, 'Alice', 'White', 105),
-- (5, 'Charlie', 'Black', 115),
-- (6, 'Dave', 'Green', 130),
-- (7, 'Eva', 'Blue', 140),
-- (8, 'Frank', 'Yellow', 125),
-- (9, 'Grace', 'Red', 135),
-- (10, 'Hank', 'Grey', 150);

-- Insert sample data into Jobs
-- INSERT INTO Jobs (jobName) VALUES 
-- ('Warrior'),
-- ('Mage'),
-- ('Rogue'),
-- ('Paladin'),
-- ('Hunter'),
-- ('Druid'),
-- ('Bard'),
-- ('Sorcerer'),
-- ('Assassin'),
-- ('Monk');

-- Insert sample data into CharacterJobs
-- INSERT INTO CharacterJobs (characterID, jobID, level, experiencePoints, isCurrentJob) VALUES 
-- (1, 1, 10, 5000, TRUE),
-- (2, 2, 8, 4000, TRUE),
-- (3, 3, 6, 3000, FALSE),
-- (4, 4, 7, 3500, TRUE),
-- (5, 5, 5, 2500, FALSE),
-- (6, 6, 12, 6000, TRUE),
-- (7, 7, 9, 4500, FALSE),
-- (8, 8, 11, 5500, TRUE),
-- (9, 9, 4, 2000, FALSE),
-- (10, 10, 13, 6500, TRUE);

-- Insert sample data into Currencies
-- INSERT INTO Currencies (currencyName, max_amount, weeklycap) VALUES
-- ('Gold', 10000, 1000),
-- ('Silver', 50000, 5000),
-- ('Copper', 100000, 10000),
-- ('Gem', 1000, 100),
-- ('Crystal', 2000, 200),
-- ('Ruby', 500, 50),
-- ('Emerald', 500, 50),
-- ('Sapphire', 500, 50),
-- ('Topaz', 500, 50),
-- ('Diamond', 100, 10);

-- Insert sample data into CharacterCurrencies
-- INSERT INTO CharacterCurrencies (characterID, currencyID, totalAmountOwned, amountThisWeek) VALUES
-- (1, 1, 1000, 100),  
-- (2, 2, 8000, 800),  
-- (3, 3, 5000, 500),  
-- (4, 4, 150, 15),    
-- (5, 5, 300, 30),    
-- (6, 6, 100, 10),    
-- (7, 7, 200, 20),    
-- (8, 8, 250, 25),    
-- (9, 9, 300, 30),    
-- (10, 10, 50, 5);    

-- Insert sample data into EquipmentSlots
-- INSERT INTO EquipmentSlots (slotName) VALUES 
-- ('Headgear'),
-- ('Chest Armor'),
-- ('Leggings'),
-- ('Boots'),
-- ('Main Hand'),
-- ('Off Hand'),
-- ('Ring'),
-- ('Necklace'),
-- ('Belt'),
-- ('Gloves');

-- Insert sample data into Items
-- INSERT INTO Items (itemName, maxStackSize, marketAllowed, vendorPrice) VALUES 
-- ('Iron Helmet', 1, TRUE, 100),
-- ('Steel Armor', 1, TRUE, 200),
-- ('Leather Boots', 1, TRUE, 50),
-- ('Sword of Dawn', 1, TRUE, 150),
-- ('Shield of Aegis', 1, TRUE, 120),
-- ('Mystic Wand', 1, TRUE, 300),
-- ('Healing Potion', 99, TRUE, 10),
-- ('Mana Elixir', 99, TRUE, 15),
-- ('Dragon Scale', 10, FALSE, 500),
-- ('Enchanted Cloak', 1, TRUE, 250);

-- Insert sample data into EquippableItems
-- INSERT INTO EquippableItems (itemID, itemLevel, slotID, requiredLevel) VALUES 
-- (1, 1, 1, 1),
-- (2, 2, 2, 2),
-- (3, 1, 3, 1),
-- (4, 2, 4, 2),
-- (5, 1, 5, 1),
-- (6, 2, 6, 2),
-- (7, 1, 7, 1),
-- (8, 2, 8, 2),
-- (9, 1, 9, 1),
-- (10, 2, 10, 2);

-- Insert sample data into Gears
-- INSERT INTO Gears (itemID, defenseRating, magicDefenseRating) VALUES 
-- (1, 10, 5),
-- (2, 12, 6),
-- (3, 8, 4),
-- (4, 15, 7),
-- (5, 11, 3),
-- (6, 9, 2),
-- (7, 14, 8),
-- (8, 10, 5),
-- (9, 13, 4),
-- (10, 12, 6);

-- Insert sample data into Weapons
-- INSERT INTO Weapons (itemID, physicalDamage, autoAttack, attackDelay) VALUES 
-- (1, 5, 1.25, 2.5),
-- (2, 6, 1.50, 2.3),
-- (3, 4, 1.70, 2.2),
-- (4, 12, 1.90, 2.0),
-- (5, 3, 1.30, 2.4),
-- (6, 7, 1.80, 2.1),
-- (7, 8, 1.40, 2.3),
-- (8, 5, 1.60, 2.2),
-- (9, 10, 1.90, 2.0),
-- (10, 6, 1.50, 2.4);

-- Insert sample data into Consumables
-- INSERT INTO Consumables (itemID, itemLevel, description) VALUES 
-- (1, 1, 'Restores 50 HP'),
-- (2, 1, 'Restores 30 MP'),
-- (3, 1, 'Restores 40 Stamina'),
-- (4, 1, 'Restores 20% Health over 5 seconds'),
-- (5, 1, 'Restores 25% Mana over 5 seconds'),
-- (6, 1, 'Restores 30% Stamina over 5 seconds'),
-- (7, 1, 'Boosts attack power by 5% for 10 minutes'),
-- (8, 1, 'Increases defense by 10% for 5 minutes'),
-- (9, 1, 'Improves critical strike chance by 5% for 15 minutes'),
-- (10, 1, 'Grants invisibility for 2 minutes');

-- Insert sample data into GearAndWeaponJobs
-- INSERT INTO GearAndWeaponJobs (itemID, jobID) VALUES 
-- (1, 1),
-- (2, 2),
-- (3, 3),
-- (4, 4),
-- (5, 5),
-- (6, 6),
-- (7, 7),
-- (8, 8),
-- (9, 9),
-- (10, 10);


-- Insert sample data into Attributes
-- INSERT INTO Attributes (attributeName) VALUES 
-- ('Strength'),
-- ('Agility'),
-- ('Intelligence'),
-- ('Stamina'),
-- ('Wisdom'),
-- ('Charisma'),
-- ('Endurance'),
-- ('Luck'),
-- ('Perception'),
-- ('Magic Power');

-- Insert sample data into GearAndWeaponAttributes
-- INSERT INTO GearAndWeaponAttributes (itemID, attributeID, attributeBonus) VALUES 
-- (1, 1, 5),
-- (2, 2, 6),
-- (3, 3, 4),
-- (4, 4, 7),
-- (5, 5, 8),
-- (6, 6, 3),
-- (7, 7, 5),
-- (8, 8, 2),
-- (9, 9, 1),
-- (10, 10, 4);


-- Insert sample data into ConsumableAttributes
-- INSERT INTO ConsumableAttributes (itemID, attributeID, attributeBonusCap, attributeBonusPercent) VALUES 
-- (1, 1, 50, 0.05),
-- (2, 2, 30, 0.04),
-- (3, 3, 40, 0.06),
-- (4, 4, 20, 0.10),
-- (5, 5, 25, 0.08),
-- (6, 6, 30, 0.07),
-- (7, 1, 0, 0.05),
-- (8, 2, 0, 0.10),
-- (9, 3, 0, 0.08),
-- (10, 4, 0, 0.15);


-- Insert sample data into CharacterAttributes
-- INSERT INTO CharacterAttributes (attributeID, characterID, attributeValue) VALUES 
-- (1, 1, 10),  
-- (2, 1, 15),  
-- (3, 1, 8),   
-- (4, 1, 12),  
-- (5, 1, 7),   
-- (6, 1, 5),   
-- (1, 2, 14),  
-- (2, 2, 18),  
-- (3, 2, 11), 
-- (4, 2, 13),
-- (5, 2, 9), 
-- (6, 2, 8); 

-- Insert sample data into InventoryPositions
-- INSERT INTO InventoryPositions (characterID, stackPosition, itemID, stackSize) VALUES
-- (1, 1, 7, 10), 
-- (1, 2, 8, 5), 
-- (2, 1, 9, 2), 
-- (3, 1, 10, 1),
-- (4, 1, 1, 1), 
-- (5, 1, 2, 1), 
-- (6, 1, 3, 1), 
-- (7, 1, 4, 1), 
-- (8, 1, 5, 1), 
-- (9, 1, 6, 1), 
-- (10, 1, 7, 20);

-- Insert sample data into CharacterEquipments
-- INSERT INTO CharacterEquipments (characterID, slotID, itemID) VALUES 
-- (1, 1, 1),
-- (1, 2, 2),
-- (1, 3, 3),
-- (2, 1, 4),
-- (2, 2, 5),
-- (2, 3, 6),
-- (3, 1, 7),
-- (3, 2, 8),
-- (4, 1, 9),
-- (4, 2, 10);
