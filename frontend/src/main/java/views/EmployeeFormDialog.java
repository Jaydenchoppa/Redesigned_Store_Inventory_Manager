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

    private final JTextField txtUsername = new JTextField();
    private final JPasswordField txtPassword = new JPasswordField();
    private final JPasswordField txtConfirm = new JPasswordField();
    private final JTextField txtName = new JTextField();

    private final JButton btnSave = new JButton("Save");
    private final JButton btnCancel = new JButton("Cancel");

    private final ClientApp client;
    private final Runnable onSaved;

    public EmployeeFormDialog(JFrame parent, ClientApp client, Runnable onSaved) {
        super(parent, "Add Employee", true);
        this.client = client;
        this.onSaved = onSaved;

        JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        form.add(new JLabel("Username:"));
        form.add(txtUsername);
        form.add(new JLabel("Password (min 6):"));
        form.add(txtPassword);
        form.add(new JLabel("Confirm password:"));
        form.add(txtConfirm);
        form.add(new JLabel("Full name:"));
        form.add(txtName);

        JPanel buttons = new JPanel();
        buttons.add(btnSave);
        buttons.add(btnCancel);

        setLayout(new BorderLayout());
        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        btnSave.addActionListener(e -> saveEmployee());
        btnCancel.addActionListener(e -> dispose());
        getRootPane().setDefaultButton(btnSave);

        setSize(420, 220);
        setLocationRelativeTo(parent);
    }

    private void saveEmployee() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirm = new String(txtConfirm.getPassword());
        String name = txtName.getText().trim();

        if (!username.matches("[A-Za-z0-9._-]{3,30}")) {
            warn("Username must be 3-30 characters: letters, numbers, . _ or -");
            return;
        }
        if (password.length() < 6) {
            warn("Password must be at least 6 characters.");
            return;
        }
        if (!password.equals(confirm)) {
            warn("The two passwords do not match.");
            return;
        }
        if (name.isEmpty()) {
            warn("Please enter the employee's full name.");
            return;
        }

        Employee employee = new Employee();
        employee.setUsername(username);
        employee.setPassword(password);
        employee.setEmployeeName(name);

        btnSave.setEnabled(false);
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                client.createEmployee(employee);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    JOptionPane.showMessageDialog(EmployeeFormDialog.this,
                            "Employee \"" + username + "\" created. They can now log in.");
                    onSaved.run();
                    dispose();
                } catch (Exception ex) {
                    btnSave.setEnabled(true);
                    String message = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
                    JOptionPane.showMessageDialog(EmployeeFormDialog.this, message,
                            "Could not add employee", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private void warn(String message) {
        JOptionPane.showMessageDialog(this, message, "Check your details", JOptionPane.WARNING_MESSAGE);
    }
}