package entity;

public class CashPayment {

    private int paymentId;
    private double cashReceived;
    private double changeAmount;

    public CashPayment(int paymentId, double cashReceived, double changeAmount) {
        this.paymentId = paymentId;
        this.cashReceived = cashReceived;
        this.changeAmount = changeAmount;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public double getCashReceived() {
        return cashReceived;
    }

    public double getChangeAmount() {
        return changeAmount;
    }
}