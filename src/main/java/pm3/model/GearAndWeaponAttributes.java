package pm3.model;

public class GearAndWeaponAttributes {
    protected int itemID;
    protected int attributeID;
    protected int attributeBonus;

    public GearAndWeaponAttributes(int itemID, int attributeID, int attributeBonus) {
        this.itemID = itemID;
        this.attributeID = attributeID;
        this.attributeBonus = attributeBonus;
    }

    public int getItemID() { return itemID; }


    public int getAttributeID() { return attributeID; }


    public int getAttributeBonus() { return attributeBonus; }
    public void setAttributeBonus(int attributeBonus) { this.attributeBonus = attributeBonus; }
}
