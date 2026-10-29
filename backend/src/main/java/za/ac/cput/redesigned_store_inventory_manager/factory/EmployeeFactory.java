package za.ac.cput.redesigned_store_inventory_manager.factory;

/*
 EmployeeFactory.java
 Employee Factory class
 Author: Templeton Liyabona Dyantyi (222623047)
 Date: 28 June 2026
*/

import za.ac.cput.redesigned_store_inventory_manager.domain.Employee;

public class EmployeeFactory {

    public static Employee createEmployee(
            String employeeId,
            String employeeName) {

        if (employeeId == null || employeeId.isBlank())
            return null;

        if (employeeName == null || employeeName.isBlank())
            return null;

        return new Employee.Builder()
                .setEmployeeId(employeeId)
                .setEmployeeName(employeeName)
                .build();
    }
}