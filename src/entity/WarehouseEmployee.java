package entity;

public class WarehouseEmployee {

    private int employeeId;
    private boolean forkliftCertified;

    public WarehouseEmployee(int employeeId,
                             boolean forkliftCertified) {

        this.employeeId = employeeId;
        this.forkliftCertified = forkliftCertified;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public boolean isForkliftCertified() {
        return forkliftCertified;
    }
}