/* Store.java
Store POJO
Author: Zacharia Dipudi (230700659)
Date: 21 June 2026*/
package za.ac.cput.redesigned_store_inventory_manager.domain;

public class Store {
    private String storeId;
    private String storeName;
    private String storePhoneNumber;
    private String storeLocation;
    private Product product;

    private Store(){

    }

    private Store (Builder builder){
        this.storeId=builder.storeId;
        this.storeName=builder.storeName;
        this.storePhoneNumber=builder.storePhoneNumber;
        this.storeLocation=builder.storeLocation;
        this.product=builder.product;
    }
    private Store(String storeId, String storeName, String storePhoneNumber, String storeLocation, Product product){
        this.storeId=storeId;
        this.storeName=storeName;
        this.storePhoneNumber=storePhoneNumber;
        this.storeLocation=storeLocation;
        this.product=product;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreLocation() {
        return storeLocation;
    }

    public void setStoreLocation(String storeLocation) {
        this.storeLocation = storeLocation;
    }

    public String getStorePhoneNumber() {
        return storePhoneNumber;
    }

    public void setStorePhoneNumber(String storePhoneNumber) {
        this.storePhoneNumber = storePhoneNumber;
    }

    public String getStoreName() {
        return storeName;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    @Override
    public String toString() {
        return "Store{" +
                "storeId='" + storeId + '\'' +
                ", storeName='" + storeName + '\'' +
                ", storePhoneNumber='" + storePhoneNumber + '\'' +
                ", storeLocation='" + storeLocation + '\'' +
                ", product=" + product +
                '}';
    }

    public static class Builder{
        private String storeId;
        private String storeName;
        private String storePhoneNumber;
        private String storeLocation;
        private Product product;

        public Builder copy(Store store) {
            this.storeId = store.storeId;
            this.storeName = store.storeName;
            this.storePhoneNumber = store.storePhoneNumber;
            this.storeLocation = store.storeLocation;
            this.product = store.product;
            return this;
        }

        public Builder setStoreId(String storeId){
            this.storeId=storeId;
            return this;
        }
        public Builder setStoreName(String storeName){
            this.storeName=storeName;
            return this;
        }
        public Builder setStorePhoneNumber(String storePhoneNumber){
            this.storePhoneNumber=storePhoneNumber;
            return this;
        }
        public Builder setStoreLocation(String storeLocation){
            this.storeLocation=storeLocation;
            return this;
        }
        public Builder setProduct(Product product){
            this.product=product;
            return this;
        }
        public Store Build(){

            return new Store(this);
        }
    }
}


