package entity;

import java.sql.Date;

public class Customer {

    private int customerId;
    private String firstName;
    private String lastName;
    private Date customerBirthDate;
    private String customerEmail;
    private String address;
    private String phoneNumber;

    public Customer(int customerId, String firstName,
                    String lastName, Date customerBirthDate,
                    String customerEmail, String address,
                    String phoneNumber) {

        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.customerBirthDate = customerBirthDate;
        this.customerEmail = customerEmail;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public Customer(int customerId, String firstName,
                    String lastName, Date customerBirthDate,
                    String customerEmail, String address) {

        this(customerId, firstName, lastName, customerBirthDate,
                customerEmail, address, "");
    }

    public Customer(String firstName,
                    String lastName, Date customerBirthDate,
                    String customerEmail, String address) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.customerBirthDate = customerBirthDate;
        this.customerEmail = customerEmail;
        this.address = address;
        this.phoneNumber = "";
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getCustomerBirthDate() {
        return customerBirthDate;
    }

    public void setCustomerBirthDate(Date customerBirthDate) {
        this.customerBirthDate = customerBirthDate;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}