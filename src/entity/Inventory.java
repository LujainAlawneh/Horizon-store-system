package entity;

import java.sql.Date;

public class Inventory {

    private int inventoryId;
    private Date inventoryDate;
    private int quantityAvailable;
    private int minimumStockLevel;
    private int productId;
    private int warehouseId;

    public Inventory(int inventoryId, Date inventoryDate,
                     int quantityAvailable,
                     int minimumStockLevel,
                     int productId,
                     int warehouseId) {

        this.inventoryId = inventoryId;
        this.inventoryDate = inventoryDate;
        this.quantityAvailable = quantityAvailable;
        this.minimumStockLevel = minimumStockLevel;
        this.productId = productId;
        this.warehouseId = warehouseId;
    }

    public int getInventoryId() {
        return inventoryId;
    }

    public Date getInventoryDate() {
        return inventoryDate;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public int getMinimumStockLevel() {
        return minimumStockLevel;
    }

    public int getProductId() {
        return productId;
    }

    public int getWarehouseId() {
        return warehouseId;
    }
}