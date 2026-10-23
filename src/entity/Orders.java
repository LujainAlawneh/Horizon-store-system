package entity;

import java.sql.Date;

public class Orders {

    private int orderId;
    private Date orderDate;
    private double totalCost;
    private int customerId;
    private int employeeId;

    // Constructor للتحميل من الداتابيس
    public Orders(int orderId, Date orderDate,
                  double totalCost,
                  int customerId,
                  int employeeId) {

        this.orderId = orderId;
        this.orderDate = orderDate;
        this.totalCost = totalCost;
        this.customerId = customerId;
        this.employeeId = employeeId;
    }

    // Constructor للإضافة (بدون ID)
    public Orders(Date orderDate,
                  double totalCost,
                  int customerId,
                  int employeeId) {

        this.orderDate = orderDate;
        this.totalCost = totalCost;
        this.customerId = customerId;
        this.employeeId = employeeId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
}