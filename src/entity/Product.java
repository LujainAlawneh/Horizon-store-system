package entity;

public class Product {

    private int productId;
    private String productName;
    private String brand;
    private double price;
    private int warrantyPeriod;
    private int categoryId;
    private int quantity;

    public Product(int productId, String productName, String brand,
                   double price, int warrantyPeriod, int categoryId, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.brand = brand;
        this.price = price;
        this.warrantyPeriod = warrantyPeriod;
        this.categoryId = categoryId;
        this.quantity = quantity;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getBrand() {
        return brand;
    }

    public double getPrice() {
        return price;
    }

    public int getWarrantyPeriod() {
        return warrantyPeriod;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getQuantity() {
        return quantity;
    }
}