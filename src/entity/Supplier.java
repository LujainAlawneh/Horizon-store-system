package entity;

public class Supplier {

    private int supplierId;
    private String supplierName;
    private String supplierAddress;
    private String companyName;
    private String email;
    private String phoneNumber;

    public Supplier(int supplierId, String supplierName,
                    String supplierAddress,
                    String companyName,
                    String email,
                    String phoneNumber) {

        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.supplierAddress = supplierAddress;
        this.companyName = companyName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Supplier(int supplierId, String supplierName,
                    String supplierAddress,
                    String companyName,
                    String email) {

        this(supplierId, supplierName, supplierAddress, companyName, email, "");
    }

    public int getSupplierId() { return supplierId; }
    public String getSupplierName() { return supplierName; }
    public String getSupplierAddress() { return supplierAddress; }
    public String getCompanyName() { return companyName; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }

    public void setSupplierId(int supplierId) { this.supplierId = supplierId; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
    public void setSupplierAddress(String supplierAddress) { this.supplierAddress = supplierAddress; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}