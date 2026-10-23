package entity;

import java.sql.Date;

public class TheReturns {

    private int returnId;
    private Date returnDate;
    private int quantity;
    private String reason;
    private double refundAmount;
    private int orderId;
    private int productId;
    private int inventoryId;

    public TheReturns(int returnId, Date returnDate, int quantity,
                      String reason, double refundAmount,
                      int orderId, int productId, int inventoryId) {
        this.returnId = returnId;
        this.returnDate = returnDate;
        this.quantity = quantity;
        this.reason = reason;
        this.refundAmount = refundAmount;
        this.orderId = orderId;
        this.productId = productId;
        this.inventoryId = inventoryId;
    }

    public int getReturnId() { return returnId; }
    public Date getReturnDate() { return returnDate; }
    public int getQuantity() { return quantity; }
    public String getReason() { return reason; }
    public double getRefundAmount() { return refundAmount; }
    public int getOrderId() { return orderId; }
    public int getProductId() { return productId; }
    public int getInventoryId() { return inventoryId; }
}