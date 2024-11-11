package pm3.model;

public class Weapons extends EquippableItems {
    protected int physicalDamage;
    protected double autoAttack;
    protected double attackDelay;
    
    public Weapons(String itemName, int maxStackSize, boolean marketAllowed, int vendorPrice,
                  int itemLevel, int slotID, int requiredLevel,
                  int physicalDamage, double autoAttack, double attackDelay) {
        super(itemName, maxStackSize, marketAllowed, vendorPrice, itemLevel, slotID, requiredLevel);
        this.physicalDamage = physicalDamage;
        this.autoAttack = autoAttack;
        this.attackDelay = attackDelay;
    }
    
    public Weapons(int itemID, String itemName, int maxStackSize, boolean marketAllowed, int vendorPrice,
                  int itemLevel, int slotID, int requiredLevel,
                  int physicalDamage, double autoAttack, double attackDelay) {
        super(itemID, itemName, maxStackSize, marketAllowed, vendorPrice, itemLevel, slotID, requiredLevel);
        this.physicalDamage = physicalDamage;
        this.autoAttack = autoAttack;
        this.attackDelay = attackDelay;
    }
    
    public int getPhysicalDamage() { return physicalDamage; }
    public void setPhysicalDamage(int physicalDamage) { this.physicalDamage = physicalDamage; }
    
    public double getAutoAttack() { return autoAttack; }
    public void setAutoAttack(double autoAttack) { this.autoAttack = autoAttack; }
    
    public double getAttackDelay() { return attackDelay; }
    public void setAttackDelay(double attackDelay) { this.attackDelay = attackDelay; }
}