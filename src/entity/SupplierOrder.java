package entity;

import java.sql.Date;

public class SupplierOrder {

    private int supplierOrderId;
    private Date purchaseDate;
    private double totalCost;
    private int supplierId;
    private int employeeId;

    public SupplierOrder(int supplierOrderId,
                         Date purchaseDate,
                         double totalCost,
                         int supplierId,
                         int employeeId) {

        this.supplierOrderId = supplierOrderId;
        this.purchaseDate = purchaseDate;
        this.totalCost = totalCost;
        this.supplierId = supplierId;
        this.employeeId = employeeId;
    }

    public int getSupplierOrderId() {
        return supplierOrderId;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public int getEmployeeId() {
        return employeeId;
    }
}