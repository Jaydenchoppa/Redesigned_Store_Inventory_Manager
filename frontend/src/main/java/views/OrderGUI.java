package views;

import domain.Order;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class OrderGUI extends JFrame implements ActionListener {

    private JPanel pnlNorth, pnlCenter, pnlSouth;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnRefresh;
    private JLabel lblTitle;
    private ClientApp client;

    public OrderGUI(ClientApp client) {
        super("Store Inventory Manager - Order History");
        this.client = client;

        pnlNorth = new JPanel();
        pnlCenter = new JPanel();
        pnlSouth = new JPanel();
        tableModel = new DefaultTableModel(new Object[]{
                "Order Num", "Customer", "Item (Product ID)", "Quantity", "Total Amount", "Order Date", "Status"
        }, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        btnRefresh = new JButton("Refresh");
        lblTitle = new JLabel("Order History");
    }

    public void setGUI() {
        pnlNorth.setLayout(new FlowLayout());
        pnlCenter.setLayout(new BorderLayout());
        pnlSouth.setLayout(new FlowLayout());

        pnlNorth.add(lblTitle);
        pnlCenter.add(new JScrollPane(table), BorderLayout.CENTER);
        pnlSouth.add(btnRefresh);

        this.add(pnlNorth, BorderLayout.NORTH);
        this.add(pnlCenter, BorderLayout.CENTER);
        this.add(pnlSouth, BorderLayout.SOUTH);

        btnRefresh.addActionListener(this);

        this.setSize(800, 450);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);

        loadOrders();
    }

    private void loadOrders() {
        new SwingWorker<List<Order>, Void>() {
            protected List<Order> doInBackground() throws Exception {
                return client.getAllOrders();
            }
            protected void done() {
                try {
                    tableModel.setRowCount(0);
                    for (Order o : get()) {
                        tableModel.addRow(new Object[]{
                                o.getOrderNum(),
                                o.getCustomerId(),
                                o.getItem(),
                                o.getQuantity(),
                                o.getTotalAmount(),
                                o.getOrderDate(),
                                o.getStatus()
                        });
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(OrderGUI.this, "Failed to load orders: " + ex.getMessage());
                }
            }
        }.execute();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnRefresh) {
            loadOrders();
        }
    }
}