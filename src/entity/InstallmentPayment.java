package entity;

public class InstallmentPayment {

    private int paymentId;
    private double downPayment;
    private int numberOfInstallments;
    private double monthlyAmount;
    private double remainingBalance;

    public InstallmentPayment(int paymentId,
                              double downPayment,
                              int numberOfInstallments,
                              double monthlyAmount,
                              double remainingBalance) {

        this.paymentId = paymentId;
        this.downPayment = downPayment;
        this.numberOfInstallments = numberOfInstallments;
        this.monthlyAmount = monthlyAmount;
        this.remainingBalance = remainingBalance;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public double getDownPayment() {
        return downPayment;
    }

    public int getNumberOfInstallments() {
        return numberOfInstallments;
    }

    public double getMonthlyAmount() {
        return monthlyAmount;
    }

    public double getRemainingBalance() {
        return remainingBalance;
    }
}