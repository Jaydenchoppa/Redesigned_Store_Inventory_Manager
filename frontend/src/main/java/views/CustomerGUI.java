package views;

/**
 * Matthew Ferreira
 * 230048870
 *
 */

import domain.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CustomerGUI extends JFrame implements ActionListener {

    private JPanel pnlNorth, pnlCenter, pnlSouth;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnDelete, btnRefresh;
    private JLabel lblTitle;
    private ClientApp client;

    public CustomerGUI(ClientApp client) {
        super("Store Inventory Manager - Customers");
        this.client = client;

        pnlNorth = new JPanel();
        pnlCenter = new JPanel();
        pnlSouth = new JPanel();
        tableModel = new DefaultTableModel(new Object[]{
                "ID", "First Name", "Surname", "Email"
        }, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        btnDelete = new JButton("Delete");
        btnRefresh = new JButton("Refresh");
        lblTitle = new JLabel("Customer Management");
    }

    public void setGUI() {
        pnlNorth.setLayout(new FlowLayout());
        pnlCenter.setLayout(new BorderLayout());
        pnlSouth.setLayout(new FlowLayout());

        pnlNorth.add(lblTitle);
        pnlCenter.add(new JScrollPane(table), BorderLayout.CENTER);
        pnlSouth.add(btnDelete);
        pnlSouth.add(btnRefresh);

        this.add(pnlNorth, BorderLayout.NORTH);
        this.add(pnlCenter, BorderLayout.CENTER);
        this.add(pnlSouth, BorderLayout.SOUTH);

        btnDelete.addActionListener(this);
        btnRefresh.addActionListener(this);

        this.setSize(700, 450);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);

        loadCustomers();
    }

    private void loadCustomers() {
        new SwingWorker<List<Customer>, Void>() {
            protected List<Customer> doInBackground() throws Exception {
                return client.getAllCustomers();
            }
            protected void done() {
                try {
                    tableModel.setRowCount(0);
                    for (Customer c : get()) {
                        tableModel.addRow(new Object[]{
                                c.getCustomerId(),
                                c.getFirstName(),
                                c.getSurname(),
                                c.getEmail()
                        });
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(CustomerGUI.this, "Failed to load customers: " + ex.getMessage());
                }
            }
        }.execute();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnRefresh) {
            loadCustomers();

        } else if (e.getSource() == btnDelete) {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a row first.");
                return;
            }
            String id = (String) tableModel.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Delete this customer?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;

            new SwingWorker<Void, Void>() {
                protected Void doInBackground() throws Exception {
                    client.deleteCustomer(id);
                    return null;
                }
                protected void done() {
                    try {
                        get();
                    } catch (Exception ex) {
                        String message = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
                        JOptionPane.showMessageDialog(CustomerGUI.this, message, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    loadCustomers();
                }
            }.execute();
        }
    }
}