package entity;

public class SalesEmployee {

    private int employeeId;
    private double salesCommission;
    private double salesTarget;

    public SalesEmployee(int employeeId,
                         double salesCommission,
                         double salesTarget) {

        this.employeeId = employeeId;
        this.salesCommission = salesCommission;
        this.salesTarget = salesTarget;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public double getSalesCommission() {
        return salesCommission;
    }

    public double getSalesTarget() {
        return salesTarget;
    }
}