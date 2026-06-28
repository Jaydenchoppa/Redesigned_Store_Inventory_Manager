package za.ac.cput.redesigned_store_inventory_manager.factory;

/* CustomerFactory.java
Customer Factory class
Author: Matthew Ferreira (230048870)
Date: 28 June 2026*/

import za.ac.cput.redesigned_store_inventory_manager.domain.Customer;

public class CustomerFactory {
    public static Customer createCustomer(
            String customerId,
            String firstName,
            String surname,
            String email,
            String phoneNumber,
            String address,
            String city,
            String postalCode,
            String country){
        return new Customer.Builder()
                .setCustomerId(customerId)
                .setFirstName(firstName)
                .setSurname(surname)
                .setEmail(email)
                .setPhoneNumber(phoneNumber)
                .setAddress(address)
                .setCity(city)
                .setPostalCode(postalCode)
                .setCountry(country)
                .build();
    }
}
