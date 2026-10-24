package views;

/**
 * SupplierFormDialog
 * Author: Templeton Liyabona Dyantyi
 * Student Number: 222623047
 */

import domain.Supplier;

import javax.swing.*;
import java.awt.*;

public class SupplierFormDialog extends JDialog {

    private JTextField txtId;
    private JTextField txtName;
    private JTextField txtContact;

    private JButton btnSave;
    private JButton btnCancel;

    private ClientApp client;
    private Runnable onSaved;

    public SupplierFormDialog(
            JFrame parent,
            ClientApp client,
            Runnable onSaved) {

        super(parent, "Add Supplier", true);

        this.client = client;
        this.onSaved = onSaved;

        txtId = new JTextField();
        txtName = new JTextField();
        txtContact = new JTextField();

        btnSave = new JButton("Save");
        btnCancel = new JButton("Cancel");

        setLayout(new GridLayout(4, 2, 5, 5));

        add(new JLabel("Supplier ID:"));
        add(txtId);

        add(new JLabel("Supplier Name:"));
        add(txtName);

        add(new JLabel("Contact Details:"));
        add(txtContact);

        add(btnSave);
        add(btnCancel);

        btnSave.addActionListener(e -> saveSupplier());

        btnCancel.addActionListener(e -> dispose());

        setSize(400, 200);
        setLocationRelativeTo(parent);
    }

    private void saveSupplier() {

        Supplier supplier = new Supplier();

        supplier.setSupplierId(txtId.getText());
        supplier.setSupplierName(txtName.getText());
        supplier.setContactDetails(txtContact.getText());

        try {

            client.createSupplier(supplier);

            JOptionPane.showMessageDialog(
                    this,
                    "Supplier added successfully."
            );

            onSaved.run();

            dispose();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to add supplier: "
                            + ex.getMessage()
            );
        }
    }
}
