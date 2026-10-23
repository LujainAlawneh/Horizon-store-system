package entity;

public class SupplierOrderDetails {

    private int quantity;
    private double unitCost;
    private int supplierOrderId;
    private int productId;

    public SupplierOrderDetails(int quantity, double unitCost,
                                int supplierOrderId, int productId) {
        this.quantity = quantity;
        this.unitCost = unitCost;
        this.supplierOrderId = supplierOrderId;
        this.productId = productId;
    }

    public int getQuantity() { return quantity; }
    public double getUnitCost() { return unitCost; }
    public int getSupplierOrderId() { return supplierOrderId; }
    public int getProductId() { return productId; }
}