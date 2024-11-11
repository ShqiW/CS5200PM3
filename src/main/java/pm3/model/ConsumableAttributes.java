package pm3.model;

public class ConsumableAttributes {
    protected int itemID;
    protected int attributeID;
    protected int attributeBonusCap;
    protected double attributeBonusPercent;

    public ConsumableAttributes(int itemID, int attributeID, int attributeBonusCap, double attributeBonusPercent) {
        this.itemID = itemID;
        this.attributeID = attributeID;
        this.attributeBonusCap = attributeBonusCap;
        this.attributeBonusPercent = attributeBonusPercent;
    }

    public int getItemID() { return itemID; }


    public int getAttributeID() { return attributeID; }


    public int getAttributeBonusCap() { return attributeBonusCap; }
    public void setAttributeBonusCap(int attributeBonusCap) { this.attributeBonusCap = attributeBonusCap; }

    public double getAttributeBonusPercent() { return attributeBonusPercent; }
    public void setAttributeBonusPercent(double attributeBonusPercent) { this.attributeBonusPercent = attributeBonusPercent; }
}