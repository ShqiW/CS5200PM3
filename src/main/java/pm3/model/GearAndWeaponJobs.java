package pm3.model;

public class GearAndWeaponJobs {
    protected int itemID;
    protected int jobID;

    public GearAndWeaponJobs(int itemID, int jobID) {
        this.itemID = itemID;
        this.jobID = jobID;
    }

    public int getItemID() { return itemID; }
    public int getJobID() { return jobID; }
}