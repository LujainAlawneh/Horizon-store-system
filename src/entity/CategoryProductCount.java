 package entity;

public class CategoryProductCount {

    private String categoryName;
    private int productCount;
    private double averagePrice;

    public CategoryProductCount(String categoryName, int productCount, double averagePrice) {
        this.categoryName = categoryName;
        this.productCount = productCount;
        this.averagePrice = averagePrice;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getProductCount() {
        return productCount;
    }

    public double getAveragePrice() {
        return averagePrice;
    }
}