package za.ac.cput.redesigned_store_inventory_manager.factory;

/*
 EmployeeFactoryTest.java
 Employee Factory Test class
 Author: Templeton Liyabona Dyantyi (222623047)
 Date: 28 June 2026
*/

import org.junit.jupiter.api.Test;
import za.ac.cput.redesigned_store_inventory_manager.domain.Employee;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeFactoryTest {

    @Test
    void createEmployee() {

        Employee employee = EmployeeFactory.createEmployee(
                "EMP001",
                "Templeton",
                "Manager"
        );

        assertNotNull(employee);
        assertEquals("EMP001", employee.getEmployeeId());
        assertEquals("Templeton", employee.getEmployeeName());
    }

    @Test
    void createEmployeeInvalidSalary() {

        Employee employee = EmployeeFactory.createEmployee(
                "EMP001",
                "Templeton",
                "Manager"
        );

        assertNull(employee);
    }
}
