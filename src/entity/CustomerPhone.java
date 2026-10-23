package entity;

public class CustomerPhone {

    private int customerId;
    private String phoneNumber;

    public CustomerPhone(int customerId, String phoneNumber) {
        this.customerId = customerId;
        this.phoneNumber = phoneNumber;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}