package entity;

public class SupplierPhone {

    private int supplierId;
    private String supplierPhone;

    public SupplierPhone(int supplierId, String supplierPhone) {
        this.supplierId = supplierId;
        this.supplierPhone = supplierPhone;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public String getSupplierPhone() {
        return supplierPhone;
    }
}