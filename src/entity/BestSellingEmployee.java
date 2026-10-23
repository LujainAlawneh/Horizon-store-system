 package entity;

public class BestSellingEmployee {

    private int employeeId;
    private String employeeName;
    private int numberOfOrders;
    private double totalSales;

    public BestSellingEmployee(int employeeId, String employeeName, int numberOfOrders, double totalSales) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.numberOfOrders = numberOfOrders;
        this.totalSales = totalSales;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public int getNumberOfOrders() {
        return numberOfOrders;
    }

    public double getTotalSales() {
        return totalSales;
    }
}