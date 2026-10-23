 package entity;

public class WarehouseCapacityReport {

    private int warehouseId;
    private String location;
    private int maximumStorage;
    private int availableStorage;

    public WarehouseCapacityReport(int warehouseId, String location, int maximumStorage, int availableStorage) {
        this.warehouseId = warehouseId;
        this.location = location;
        this.maximumStorage = maximumStorage;
        this.availableStorage = availableStorage;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public String getLocation() {
        return location;
    }

    public int getMaximumStorage() {
        return maximumStorage;
    }

    public int getAvailableStorage() {
        return availableStorage;
    }
}