package za.ac.cput.redesigned_store_inventory_manager.domain;

/*
 Employee.java
 Employee POJO class
 Author: Templeton Liyabona Dyantyi (222623047)
 Date: 21 June 2026
 */

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String employeeId;
    private String employeeName;
    private String position;
    private double salary;
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    protected Employee() {}

    private Employee(Builder builder) {
        this.employeeId = builder.employeeId;
        this.employeeName = builder.employeeName;
        this.position = builder.position;
        this.salary = builder.salary;
        this.username = builder.username;
        this.password = builder.password;
        this.id = builder.id;
    }

    public Long getId() {
        return id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getPosition() {
        return position;
    }

    public double getSalary() {
        return salary;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", employeeId='" + employeeId + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", position='" + position + '\'' +
                ", salary=" + salary +
                ", username='" + username + '\'' +
                '}';
    }

    public static class Builder {

        private String employeeId;
        private String employeeName;
        private String position;
        private double salary;
        private String username;
        private Long id;
        private String password;

        public Builder setEmployeeId(String employeeId) {
            this.employeeId = employeeId;
            return this;
        }

        public Builder setEmployeeName(String employeeName) {
            this.employeeName = employeeName;
            return this;
        }

        public Builder setPosition(String position) {
            this.position = position;
            return this;
        }

        public Builder setSalary(double salary) {
            this.salary = salary;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder copy(Employee employee) {
            this.id = employee.getId();
            this.employeeId = employee.getEmployeeId();
            this.employeeName = employee.getEmployeeName();
            this.position = employee.getPosition();
            this.salary = employee.getSalary();
            this.username = employee.getUsername();
            this.password = employee.getPassword();
            return this;
        }

        public Employee build() {
            return new Employee(this);
        }
    }
}