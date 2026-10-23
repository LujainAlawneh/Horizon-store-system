package entity;

public class OrderDetails {

    private int quantity;
    private double unitPrice;
    private double lineTotal;
    private int orderId;
    private int productId;

    public OrderDetails(int quantity, double unitPrice, double lineTotal,
                        int orderId, int productId) {
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.lineTotal = lineTotal;
        this.orderId = orderId;
        this.productId = productId;
    }

    public int getQuantity() { return quantity; }
    public double getUnitPrice() { return unitPrice; }
    public double getLineTotal() { return lineTotal; }
    public int getOrderId() { return orderId; }
    public int getProductId() { return productId; }
}