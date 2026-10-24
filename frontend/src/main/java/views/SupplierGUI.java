package views;

/**
 * SupplierGUI
 * Author: Templeton Liyabona Dyantyi
 * Student Number: 222623047
 */

import domain.Supplier;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class SupplierGUI extends JFrame implements ActionListener {

    private JPanel pnlNorth, pnlCenter, pnlSouth;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnAdd, btnDelete, btnRefresh;
    private JLabel lblTitle;
    private ClientApp client;

    public SupplierGUI(ClientApp client) {
        super("Store Inventory Manager - Suppliers");
        this.client = client;

        pnlNorth = new JPanel();
        pnlCenter = new JPanel();
        pnlSouth = new JPanel();

        tableModel = new DefaultTableModel(new Object[]{
                "ID", "Supplier Name", "Contact Details"
        }, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        table = new JTable(tableModel);

        btnAdd = new JButton("Add");
        btnDelete = new JButton("Delete");
        btnRefresh = new JButton("Refresh");

        lblTitle = new JLabel("Supplier Management");
    }

    public void setGUI() {

        pnlNorth.setLayout(new FlowLayout());
        pnlCenter.setLayout(new BorderLayout());
        pnlSouth.setLayout(new FlowLayout());

        pnlNorth.add(lblTitle);

        pnlCenter.add(new JScrollPane(table), BorderLayout.CENTER);

        pnlSouth.add(btnAdd);
        pnlSouth.add(btnDelete);
        pnlSouth.add(btnRefresh);

        this.add(pnlNorth, BorderLayout.NORTH);
        this.add(pnlCenter, BorderLayout.CENTER);
        this.add(pnlSouth, BorderLayout.SOUTH);

        btnAdd.addActionListener(this);
        btnDelete.addActionListener(this);
        btnRefresh.addActionListener(this);

        this.setSize(700, 450);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);

        loadSuppliers();
    }

    private void loadSuppliers() {

        new SwingWorker<List<Supplier>, Void>() {

            protected List<Supplier> doInBackground() throws Exception {
                return client.getAllSuppliers();
            }

            protected void done() {

                try {

                    tableModel.setRowCount(0);

                    for (Supplier s : get()) {

                        tableModel.addRow(new Object[]{
                                s.getSupplierId(),
                                s.getSupplierName(),
                                s.getContactDetails()
                        });
                    }

                } catch (Exception ex) {

                    JOptionPane.showMessageDialog(
                            SupplierGUI.this,
                            "Failed to load suppliers: "
                                    + ex.getMessage()
                    );
                }
            }

        }.execute();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnRefresh) {

            loadSuppliers();

        } else if (e.getSource() == btnAdd) {

            new SupplierFormDialog(
                    this,
                    client,
                    this::loadSuppliers
            ).setVisible(true);

        } else if (e.getSource() == btnDelete) {

            int row = table.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(
                        this,
                        "Select a row first."
                );
                return;
            }

            String id = (String) tableModel.getValueAt(row, 0);

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Delete this supplier?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            new SwingWorker<Void, Void>() {

                protected Void doInBackground() throws Exception {
                    client.deleteSupplier(id);
                    return null;
                }

                protected void done() {
                    loadSuppliers();
                }

            }.execute();
        }
    }

}
