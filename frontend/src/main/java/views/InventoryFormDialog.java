package views;

import domain.*;

import javax.swing.*;
import java.awt.*;

public class InventoryFormDialog extends JDialog {

    private JTextField txtProductId, txtProductName, txtSupplierId,
            txtCategoryName, txtQuantity, txtUnitPrice, txtLocation;

    private JButton btnSave, btnCancel;

    private final ClientApp client;
    private final Runnable onSuccess;
    private final Long inventoryId;
    private final boolean updateMode;

    public InventoryFormDialog(
            JFrame parent, ClientApp client, Runnable onSuccess) {

        this(parent, client, onSuccess, null, null);
    }

    // New constructor: Update inventory
    public InventoryFormDialog(
            JFrame parent,
            ClientApp client,
            Runnable onSuccess,
            Long inventoryId,
            Inventory existing) {

        super(parent, existing == null
                ? "Add Inventory Item"
                : "Update Inventory Item", true);

        this.client = client;
        this.onSuccess = onSuccess;
        this.inventoryId = inventoryId;
        this.updateMode = existing != null;

        JPanel form = new JPanel(new GridLayout(7, 2, 5, 5));

        txtProductId = new JTextField();
        txtProductName = new JTextField();
        txtSupplierId = new JTextField();
        txtCategoryName = new JTextField();
        txtQuantity = new JTextField();
        txtUnitPrice = new JTextField();
        txtLocation = new JTextField();

        form.add(new JLabel("Product ID:"));
        form.add(txtProductId);

        form.add(new JLabel("Product Name:"));
        form.add(txtProductName);

        form.add(new JLabel("Supplier ID:"));
        form.add(txtSupplierId);

        form.add(new JLabel("Category:"));
        form.add(txtCategoryName);

        form.add(new JLabel("Quantity In Stock:"));
        form.add(txtQuantity);

        form.add(new JLabel("Unit Price:"));
        form.add(txtUnitPrice);

        form.add(new JLabel("Location:"));
        form.add(txtLocation);

        JPanel buttons = new JPanel();

        btnSave = new JButton("Save");
        btnCancel = new JButton("Cancel");

        buttons.add(btnSave);
        buttons.add(btnCancel);

        setLayout(new BorderLayout(5, 5));
        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        // Populate fields when editing an existing item.
        if (existing != null) {
            txtProductId.setText(existing.getProduct() != null
                    ? existing.getProduct().getProductId() : "");

            txtProductName.setText(existing.getProduct() != null
                    ? existing.getProduct().getProductName() : "");

            txtSupplierId.setText(existing.getSupplier() != null
                    ? existing.getSupplier().getSupplierId() : "");

            txtCategoryName.setText(existing.getCategory() != null
                    ? existing.getCategory().getName() : "");

            txtQuantity.setText(
                    String.valueOf(existing.getQuantityInStock()));

            txtUnitPrice.setText(
                    String.valueOf(existing.getUnitPrice()));

            txtLocation.setText(
                    existing.getLocation() != null
                            ? existing.getLocation() : "");
        }

        // Avoid changing the product identity accidentally during an update.
        txtProductId.setEditable(!updateMode);

        setSize(420, 340);
        setLocationRelativeTo(parent);

        btnSave.addActionListener(e -> save());
        btnCancel.addActionListener(e -> dispose());
    }

    private void save() {
        try {
            String productId = txtProductId.getText().trim();
            String productName = txtProductName.getText().trim();
            String supplierId = txtSupplierId.getText().trim();
            String categoryName = txtCategoryName.getText().trim();
            String quantityText = txtQuantity.getText().trim();
            String priceText = txtUnitPrice.getText().trim();
            String location = txtLocation.getText().trim();

            if (productId.isEmpty()
                    || productName.isEmpty()
                    || supplierId.isEmpty()
                    || categoryName.isEmpty()
                    || quantityText.isEmpty()
                    || priceText.isEmpty()
                    || location.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this, "Please complete all fields.");
                return;
            }

            int quantity = Integer.parseInt(quantityText);
            double price = Double.parseDouble(priceText);

            if (quantity < 0 || price < 0
                    || !Double.isFinite(price)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Quantity and price must be valid, non-negative numbers.");
                return;
            }

            Product product = new Product();
            product.setProductId(productId);
            product.setProductName(productName);

            Supplier supplier = new Supplier();
            supplier.setSupplierId(supplierId);

            Category category = new Category(categoryName, "");

            Inventory inventory = new Inventory();
            inventory.setProduct(product);
            inventory.setSupplier(supplier);
            inventory.setCategory(category);
            inventory.setQuantityInStock(quantity);
            inventory.setUnitPrice(price);
            inventory.setLocation(location);

            btnSave.setEnabled(false);

            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    if (updateMode) {
                        client.updateInventory(inventoryId, inventory);
                    } else {
                        client.createInventory(inventory);
                    }
                    return null;
                }

                @Override
                protected void done() {
                    btnSave.setEnabled(true);

                    try {
                        get();

                        JOptionPane.showMessageDialog(
                                InventoryFormDialog.this,
                                updateMode
                                        ? "Inventory updated successfully."
                                        : "Inventory added successfully.");

                        onSuccess.run();
                        dispose();

                    } catch (Exception ex) {
                        Throwable cause = ex.getCause() != null
                                ? ex.getCause() : ex;

                        JOptionPane.showMessageDialog(
                                InventoryFormDialog.this,
                                cause.getMessage() != null
                                        ? cause.getMessage()
                                        : "The operation failed.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }.execute();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Enter a whole number for quantity and a valid number for price.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}