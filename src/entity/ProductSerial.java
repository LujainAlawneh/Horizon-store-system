package entity;

import java.sql.Date;

public class ProductSerial {

    private int productSerialId;
    private String serialNumber;
    private Date warrantyStartDate;
    private Date warrantyEndDate;
    private String status;
    private int orderId;
    private int productId;

    public ProductSerial(int productSerialId, String serialNumber,
                         Date warrantyStartDate, Date warrantyEndDate,
                         String status, int orderId, int productId) {
        this.productSerialId = productSerialId;
        this.serialNumber = serialNumber;
        this.warrantyStartDate = warrantyStartDate;
        this.warrantyEndDate = warrantyEndDate;
        this.status = status;
        this.orderId = orderId;
        this.productId = productId;
    }

    public int getProductSerialId() { return productSerialId; }
    public String getSerialNumber() { return serialNumber; }
    public Date getWarrantyStartDate() { return warrantyStartDate; }
    public Date getWarrantyEndDate() { return warrantyEndDate; }
    public String getStatus() { return status; }
    public int getOrderId() { return orderId; }
    public int getProductId() { return productId; }
}