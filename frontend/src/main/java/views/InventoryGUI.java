package views;

import domain.Inventory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
    * InventoryGUI
 * @author Jayden Avontuur
 */
public class InventoryGUI extends JFrame implements ActionListener {

    private JPanel pnlNorth, pnlCenter, pnlSouth;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnAdd, btnDelete, btnRefresh, btnLogout;
    private JButton btnOrders, btnSuppliers, btnCustomers, btnEmployees;
    private JLabel lblTitle;
    private ClientApp client;

    public InventoryGUI(ClientApp client) {
        super("Store Inventory Manager - Inventory");
        this.client = client;

        pnlNorth = new JPanel();
        pnlCenter = new JPanel();
        pnlSouth = new JPanel();
        tableModel = new DefaultTableModel(new Object[]{
                "ID", "Product ID", "Product Name", "Category", "Supplier", "Qty", "Unit Price", "Location"
        }, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        btnAdd = new JButton("Add");
        btnDelete = new JButton("Delete");
        btnRefresh = new JButton("Refresh");
        btnLogout = new JButton("Log out");
        btnOrders = new JButton("Order History");
        btnSuppliers = new JButton("Suppliers");
        btnCustomers = new JButton("Customers");
        btnEmployees = new JButton("Employees");
        lblTitle = new JLabel("Inventory Management");
    }

    public void setGUI() {
        // Header: title centred on top, navigation buttons centred underneath it
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 20f));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JPanel pnlNavButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        pnlNavButtons.add(btnOrders);
        pnlNavButtons.add(btnSuppliers);
        pnlNavButtons.add(btnCustomers);
        pnlNavButtons.add(btnEmployees);

        pnlNorth.setLayout(new BorderLayout());
        pnlNorth.add(lblTitle, BorderLayout.NORTH);
        pnlNorth.add(pnlNavButtons, BorderLayout.CENTER);

        pnlCenter.setLayout(new BorderLayout());
        pnlSouth.setLayout(new FlowLayout());

        pnlCenter.add(new JScrollPane(table), BorderLayout.CENTER);
        pnlSouth.add(btnAdd);
        pnlSouth.add(btnDelete);
        pnlSouth.add(btnRefresh);
        pnlSouth.add(btnLogout);

        this.add(pnlNorth, BorderLayout.NORTH);
        this.add(pnlCenter, BorderLayout.CENTER);
        this.add(pnlSouth, BorderLayout.SOUTH);

        btnAdd.addActionListener(this);
        btnDelete.addActionListener(this);
        btnRefresh.addActionListener(this);
        btnLogout.addActionListener(this);
        btnOrders.addActionListener(this);
        btnSuppliers.addActionListener(this);
        btnCustomers.addActionListener(this);
        btnEmployees.addActionListener(this);

        this.setSize(900, 450);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        loadInventory();
    }

    private void loadInventory() {
        new SwingWorker<List<Inventory>, Void>() {
            protected List<Inventory> doInBackground() throws Exception {
                return client.getAllInventory();
            }
            protected void done() {
                try {
                    tableModel.setRowCount(0);
                    for (Inventory inv : get()) {
                        tableModel.addRow(new Object[]{
                                inv.getInventoryId(),
                                inv.getProduct() != null ? inv.getProduct().getProductId() : "",
                                inv.getProduct() != null ? inv.getProduct().getProductName() : "",
                                inv.getCategory() != null ? inv.getCategory().getName() : "",
                                inv.getSupplier() != null ? inv.getSupplier().getSupplierId() : "",
                                inv.getQuantityInStock(),
                                inv.getUnitPrice(),
                                inv.getLocation()
                        });
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(InventoryGUI.this, "Failed to load inventory: " + ex.getMessage());
                }
            }
        }.execute();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnRefresh) {
            loadInventory();

        } else if (e.getSource() == btnAdd) {
            new InventoryFormDialog(this, client, this::loadInventory).setVisible(true);

        } else if (e.getSource() == btnDelete) {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a row first.");
                return;
            }
            Long id = (Long) tableModel.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Delete this inventory item?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;

            new SwingWorker<Void, Void>() {
                protected Void doInBackground() throws Exception {
                    client.deleteInventory(id);
                    return null;
                }
                protected void done() {
                    try {
                        get();
                    } catch (Exception ex) {
                        String message = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
                        JOptionPane.showMessageDialog(InventoryGUI.this, message, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    loadInventory();
                }
            }.execute();

        } else if (e.getSource() == btnOrders) {
            new OrderGUI(client).setGUI();

        } else if (e.getSource() == btnSuppliers) {
            new SupplierGUI(client).setGUI();

        } else if (e.getSource() == btnCustomers) {
            new CustomerGUI(client).setGUI();

        } else if (e.getSource() == btnEmployees) {
            new EmployeeGUI(client).setGUI();

        } else if (e.getSource() == btnLogout) {
            client.logoutAndShowLogin();
        }
    }
}