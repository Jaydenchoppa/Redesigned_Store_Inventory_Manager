package views;

/**
 * EmployeeFormDialog
 * Author: Templeton Liyabona Dyantyi
 * Student Number: 222623047
 */

import domain.Employee;

import javax.swing.*;
import java.awt.*;

public class EmployeeFormDialog extends JDialog {

    private JTextField txtId;
    private JTextField txtName;
    private JTextField txtPosition;
    private JTextField txtSalary;

    private JButton btnSave;
    private JButton btnCancel;

    private ClientApp client;
    private Runnable onSaved;

    public EmployeeFormDialog(
            JFrame parent,
            ClientApp client,
            Runnable onSaved) {

        super(parent, "Add Employee", true);

        this.client = client;
        this.onSaved = onSaved;

        txtId = new JTextField();
        txtName = new JTextField();
        txtPosition = new JTextField();
        txtSalary = new JTextField();

        btnSave = new JButton("Save");
        btnCancel = new JButton("Cancel");

        setLayout(new GridLayout(5, 2, 5, 5));

        add(new JLabel("Employee ID:"));
        add(txtId);

        add(new JLabel("Employee Name:"));
        add(txtName);

        add(new JLabel("Position:"));
        add(txtPosition);

        add(new JLabel("Salary:"));
        add(txtSalary);

        add(btnSave);
        add(btnCancel);

        btnSave.addActionListener(e -> saveEmployee());

        btnCancel.addActionListener(e -> dispose());

        setSize(400, 250);
        setLocationRelativeTo(parent);
    }

    private void saveEmployee() {

        Employee employee = new Employee();

        employee.setEmployeeId(txtId.getText());
        employee.setEmployeeName(txtName.getText());
        employee.setPosition(txtPosition.getText());

        try {

            employee.setSalary(
                    Double.parseDouble(txtSalary.getText())
            );

            client.createEmployee(employee);

            JOptionPane.showMessageDialog(
                    this,
                    "Employee added successfully."
            );

            onSaved.run();

            dispose();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to add employee: "
                            + ex.getMessage()
            );
        }
    }
}
