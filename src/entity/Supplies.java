package entity;

public class Supplies {

    private int supplierId;
    private int productId;

    public Supplies(int supplierId, int productId) {
        this.supplierId = supplierId;
        this.productId = productId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public int getProductId() {
        return productId;
    }
}