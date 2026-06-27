package za.ac.cput.redesigned_store_inventory_manager.domain;

/*
 Supplier.java
 Supplier POJO class
 Author: Templeton Liyabona Dyantyi (222623047)
 Date: 21 June 2026
 */

public class Supplier {

    private String supplierId;
    private String supplierName;
    private String contactDetails;

    private Supplier(Builder builder) {
        this.supplierId = builder.supplierId;
        this.supplierName = builder.supplierName;
        this.contactDetails = builder.contactDetails;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getContactDetails() {
        return contactDetails;
    }

    @Override
    public String toString() {
        return "Supplier{" +
                "supplierId='" + supplierId + '\'' +
                ", supplierName='" + supplierName + '\'' +
                ", contactDetails='" + contactDetails + '\'' +
                '}';
    }

    public static class Builder {

        private String supplierId;
        private String supplierName;
        private String contactDetails;

        public Builder setSupplierId(String supplierId) {
            this.supplierId = supplierId;
            return this;
        }

        public Builder setSupplierName(String supplierName) {
            this.supplierName = supplierName;
            return this;
        }

        public Builder setContactDetails(String contactDetails) {
            this.contactDetails = contactDetails;
            return this;
        }

        public Supplier build() {
            return new Supplier(this);
        }
    }
}
