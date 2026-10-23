 package entity;

public class BestSellingProduct {

    private int productId;
    private String productName;
    private int totalQuantitySold;

    public BestSellingProduct(int productId, String productName, int totalQuantitySold) {
        this.productId = productId;
        this.productName = productName;
        this.totalQuantitySold = totalQuantitySold;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getTotalQuantitySold() {
        return totalQuantitySold;
    }
}