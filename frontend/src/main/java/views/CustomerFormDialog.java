package views;

/**
 * Matthew Ferreira
 * 230048870
 *
 */

import domain.Customer;

import javax.swing.*;
import java.awt.*;

public class CustomerFormDialog extends JDialog {

    private JTextField txtFirstName, txtSurname, txtEmail;
    private JButton btnSave, btnCancel;
    private ClientApp client;
    private Runnable onSuccess;

    public CustomerFormDialog(JFrame parent, ClientApp client, Runnable onSuccess) {
        super(parent, "Add Customer", true);
        this.client = client;
        this.onSuccess = onSuccess;

        JPanel form = new JPanel(new GridLayout(3, 2, 5, 5));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        txtFirstName = new JTextField();
        txtSurname = new JTextField();
        txtEmail = new JTextField();

        form.add(new JLabel("First Name:")); form.add(txtFirstName);
        form.add(new JLabel("Surname:")); form.add(txtSurname);
        form.add(new JLabel("Email:")); form.add(txtEmail);

        JPanel buttons = new JPanel();
        btnSave = new JButton("Save");
        btnCancel = new JButton("Cancel");
        buttons.add(btnSave);
        buttons.add(btnCancel);

        this.setLayout(new BorderLayout());
        this.add(form, BorderLayout.CENTER);
        this.add(buttons, BorderLayout.SOUTH);
        this.setSize(400, 200);
        this.setLocationRelativeTo(parent);

        btnSave.addActionListener(e -> save());
        btnCancel.addActionListener(e -> dispose());
        getRootPane().setDefaultButton(btnSave);
    }

    private void save() {
        String firstName = txtFirstName.getText().trim();
        String surname = txtSurname.getText().trim();
        String email = txtEmail.getText().trim();

        if (firstName.isEmpty() || surname.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in first name, surname and email.");
            return;
        }

        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setSurname(surname);
        customer.setEmail(email);

        new SwingWorker<Void, Void>() {
            protected Void doInBackground() throws Exception {
                client.createCustomer(customer);
                return null;
            }
            protected void done() {
                try {
                    get();
                    onSuccess.run();
                    dispose();
                } catch (Exception ex) {
                    String message = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
                    JOptionPane.showMessageDialog(CustomerFormDialog.this, message, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }
}