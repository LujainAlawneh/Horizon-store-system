package entity;

import java.sql.Date;

public class Payment {

    private int paymentId;
    private Date paymentDate;
    private double amount;
    private String paymentStatus;
    private int orderId;

    public Payment(int paymentId, Date paymentDate,
                   double amount,
                   String paymentStatus,
                   int orderId) {

        this.paymentId = paymentId;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
        this.orderId = orderId;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public double getAmount() {
        return amount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public int getOrderId() {
        return orderId;
    }
}