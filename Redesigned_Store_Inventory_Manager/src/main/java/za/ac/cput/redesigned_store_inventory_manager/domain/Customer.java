package za.ac.cput.redesigned_store_inventory_manager.domain;

/* Customer.java
Customer POJO
Author: Matthew Ferreira (230048870)
Date: 21 June 2026*/

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Customer {
    @Id
    private String customerId;
    private String firstName;
    private String surname;
    private String email;
    private String phoneNumber;
    private String address;
    private String city;
    private String postalCode;
    private String country;

    protected Customer() {}

    public String getCustomerId() {
        return customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCountry() {
        return country;
    }

    private Customer (Builder builder){
        this.customerId = builder.customerId;
        this.firstName = builder.firstName;
        this.surname = builder.surname;
        this.email = builder.email;
        this.phoneNumber = builder.phoneNumber;
        this.address = builder.address;
        this.city = builder.city;
        this.postalCode = builder.postalCode;
        this.country = builder.country;
    }

    public static class Builder{
        private String customerId;
        private String firstName;
        private String surname;
        private String email;
        private String phoneNumber;
        private String address;
        private String city;
        private String postalCode;
        private String country;

        public Builder setCustomerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder setSurname(String surname) {
            this.surname = surname;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        public Builder setCity(String city) {
            this.city = city;
            return this;
        }

        public Builder setPostalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        public Builder setCountry(String country) {
            this.country = country;
            return this;
        }

        public Builder copy(Customer customer){
            this.customerId = customer.getCustomerId();
            this.firstName = customer.getFirstName();
            this.surname = customer.getSurname();
            this.email = customer.getEmail();
            this.phoneNumber = customer.getPhoneNumber();
            this.address = customer.getAddress();
            this.city = customer.getCity();
            this.postalCode = customer.getPostalCode();
            this.country = customer.getCountry();
            return this;
        }

        public Customer build(){
            return new Customer(this);
        }
    }

}
