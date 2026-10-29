package za.ac.cput.redesigned_store_inventory_manager.domain;

/* Customer.java
Customer POJO
Author: Matthew Ferreira (230048870)
Date: 21 June 2026*/

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Customer {
    @Id
    private String customerId;
    private String firstName;
    private String surname;
    private String email;
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

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


    public String getUsername() { return username; }
    public String getPassword() { return password; }

    private Customer (Builder builder){
        this.customerId = builder.customerId;
        this.firstName = builder.firstName;
        this.surname = builder.surname;
        this.email = builder.email;
        this.username = builder.username;
        this.password = builder.password;
    }

    public static class Builder{
        private String customerId;
        private String firstName;
        private String surname;
        private String email;
        private String username;
        private String password;

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


        public Builder setUsername(String username){
            this.username = username;
            return this;
        }

        public Builder setPassword(String password){
            this.password = password;
            return this;
        }

        public Builder copy(Customer customer){
            this.customerId = customer.getCustomerId();
            this.firstName = customer.getFirstName();
            this.surname = customer.getSurname();
            this.email = customer.getEmail();
            this.username = customer.getUsername();
            this.password = customer.getPassword();
            return this;
        }

        public Customer build(){
            return new Customer(this);
        }
    }

}
