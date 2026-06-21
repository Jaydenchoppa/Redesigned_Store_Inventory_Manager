/* Product.java
Product POJO
Author: Zacharia Dipudi (230700659)
Date: 21 June 2026*/
package za.ac.cput.redesigned_store_inventory_manager.domain;

public class Product {
    private String productId;
    private String productName;
    private String productDescription;
    private String productPrice;


    private Product(){

    }
    private Product(Builder builder){
        this.productId= builder.productId;
        this.productName=builder.productName;
        this.productDescription=builder.productDescription;
        this.productPrice=builder.productPrice;

    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }



    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", productPrice='" + productPrice + '\'' +
                '}';
    }

    public static class Builder{
        private String productId;
        private String productName;
        private String productDescription;
        private String productPrice;

        public Builder copy(Product product){
            this.productId=product.productId;
            this.productName=product.productName;
            this.productDescription=product.productDescription;
            this.productPrice=product.productPrice;
            return this;
        }

        public Builder setProductId(String productId){
            this.productId=productId;
            return this;
        }
        public Builder setProductName(String productName){
            this.productName=productName;
            return this;
        }
        public Builder setProductDescription( String productDescription){
            this.productDescription=productDescription;
            return this;
        }
        public Builder setProductPrice(String productPrice){
            this.productPrice=productPrice;
            return this;


        }
        public Product build(){
            return new Product(this);
        }
    }

}


