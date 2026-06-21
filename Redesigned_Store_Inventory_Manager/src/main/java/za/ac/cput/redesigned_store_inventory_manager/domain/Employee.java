package za.ac.cput.redesigned_store_inventory_manager.domain;

/*
 Employee.java
 Employee POJO class
 Author: Templeton Liyabona Dyantyi (222623047)
 Date: 21 June 2026
 */

public class Employee {

    private String employeeId;
    private String employeeName;
    private String position;
    private double salary;

    private Employee(Builder builder) {
        this.employeeId = builder.employeeId;
        this.employeeName = builder.employeeName;
        this.position = builder.position;
        this.salary = builder.salary;
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

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId='" + employeeId + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", position='" + position + '\'' +
                ", salary=" + salary +
                '}';
    }

    public static class Builder {

        private String employeeId;
        private String employeeName;
        private String position;
        private double salary;

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

        public Employee build() {
            return new Employee(this);
        }
    }
}