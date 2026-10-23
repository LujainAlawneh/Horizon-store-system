 package entity;

public class TopCustomer {

    private int customerId;
    private String customerName;
    private int numberOfOrders;

    public TopCustomer(int customerId, String customerName, int numberOfOrders) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.numberOfOrders = numberOfOrders;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getNumberOfOrders() {
        return numberOfOrders;
    }
}