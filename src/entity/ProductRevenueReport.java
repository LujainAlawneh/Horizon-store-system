package entity;

public class ProductRevenueReport {

    private int productId;
    private String productName;
    private double salesRevenue;
    private double returnLoss;
    private double netRevenue;

    public ProductRevenueReport(int productId,
                                String productName,
                                double salesRevenue,
                                double returnLoss,
                                double netRevenue) {

        this.productId = productId;
        this.productName = productName;
        this.salesRevenue = salesRevenue;
        this.returnLoss = returnLoss;
        this.netRevenue = netRevenue;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public double getSalesRevenue() {
        return salesRevenue;
    }

    public double getReturnLoss() {
        return returnLoss;
    }

    public double getNetRevenue() {
        return netRevenue;
    }
}