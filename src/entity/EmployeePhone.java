package entity;

public class EmployeePhone {

    private int employeeId;
    private String phoneNumber;

    public EmployeePhone(int employeeId, String phoneNumber) {
        this.employeeId = employeeId;
        this.phoneNumber = phoneNumber;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}