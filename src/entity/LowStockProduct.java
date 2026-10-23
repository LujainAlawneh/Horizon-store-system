 package entity;

public class LowStockProduct {

    private String productName;
    private String warehouseLocation;
    private int quantityAvailable;
    private int minimumStockLevel;

    public LowStockProduct(String productName,
                           String warehouseLocation,
                           int quantityAvailable,
                           int minimumStockLevel) {

        this.productName = productName;
        this.warehouseLocation = warehouseLocation;
        this.quantityAvailable = quantityAvailable;
        this.minimumStockLevel = minimumStockLevel;
    }

    public String getProductName() {
        return productName;
    }

    public String getWarehouseLocation() {
        return warehouseLocation;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public int getMinimumStockLevel() {
        return minimumStockLevel;
    }
}