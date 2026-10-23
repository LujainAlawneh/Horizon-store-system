package entity;

import java.sql.Date;

public class Employee {

    private int employeeId;
    private String firstName;
    private String lastName;
    private String shift;
    private double salary;
    private String address;
    private Integer managerId;
    private Date hireDate;
    private String phoneNumber;

    public Employee(int employeeId,
                    String firstName,
                    String lastName,
                    String shift,
                    double salary,
                    String address,
                    Integer managerId,
                    Date hireDate,
                    String phoneNumber) {

        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.shift = shift;
        this.salary = salary;
        this.address = address;
        this.managerId = managerId;
        this.hireDate = hireDate;
        this.phoneNumber = phoneNumber;
    }

    public Employee(int employeeId,
                    String firstName,
                    String lastName,
                    String shift,
                    double salary,
                    String address,
                    Integer managerId,
                    Date hireDate) {

        this(employeeId,
                firstName,
                lastName,
                shift,
                salary,
                address,
                managerId,
                hireDate,
                "");
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
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

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}