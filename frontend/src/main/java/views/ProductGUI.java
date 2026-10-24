package views;

import domain.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;



    public class ProductGUI extends JFrame implements ActionListener {

        private JPanel pnlNorth, pnlSouth, pnlCentre;
        private JTable table;
        private DefaultTableModel tableModel;
        private JLabel lbl;
        private JButton addProduct, btnDelete, btnEdit;
        private ClientApp client;

        public ProductGUI() {

            pnlNorth = new JPanel();
            pnlSouth = new JPanel();
            pnlCentre = new JPanel();

            client = new ClientApp();

            tableModel = new DefaultTableModel(
                    new Object[]{
                            "Product ID",
                            "Product Name",
                            "Description",
                            "Price"
                    }, 0) {

                @Override
                public boolean isCellEditable(int row, int col) {
                    return false;
                }
            };

            table = new JTable(tableModel);

            addProduct = new JButton("Add product");
            btnDelete = new JButton("Delete");
            btnEdit = new JButton("Edit");

            lbl = new JLabel("Product Manager");

            setGUI();
            loadProducts();
        }

        public void setGUI() {

            setLayout(new BorderLayout());

            // NORTH
            pnlNorth.setLayout(new BorderLayout());

            lbl.setText("Product Manager");
            lbl.setFont(new java.awt.Font(
                    "Arial",
                    java.awt.Font.BOLD,
                    22
            ));

            pnlNorth.add(lbl, BorderLayout.WEST);
            pnlNorth.add(addProduct, BorderLayout.EAST);

            pnlCentre.setLayout(new BorderLayout());

            JScrollPane scrollPane = new JScrollPane(table);

            pnlCentre.add(scrollPane, BorderLayout.CENTER);


            pnlSouth.setLayout(
                    new FlowLayout(FlowLayout.RIGHT)
            );

            pnlSouth.add(btnEdit);
            pnlSouth.add(btnDelete);

            // ADD PANELS
            add(pnlNorth, BorderLayout.NORTH);
            add(pnlCentre, BorderLayout.CENTER);
            add(pnlSouth, BorderLayout.SOUTH);

            // BUTTON LISTENERS
            addProduct.addActionListener(this);
            btnEdit.addActionListener(this);
            btnDelete.addActionListener(this);

            // FRAME SETTINGS
            setTitle("Product Management System");
            setSize(800, 500);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setVisible(true);
        }

        private void loadProducts() {

            new SwingWorker<List<Product>, Void>() {

                @Override
                public List<Product> doInBackground()
                        throws Exception {

                    return (List<Product>) client.getAllProducts();
                }

                @Override
                protected void done() {

                    try {

                        tableModel.setRowCount(0);

                        for (Product p : get()) {
                            tableModel.addRow(new Object[]{

                                    p.getProductId(),
                                    p.getProductName(),
                                    p.getProductDescription(),
                                    p.getProductPrice()

                            });
                        }

                    } catch (Exception ex) {

                        JOptionPane.showMessageDialog(
                                ProductGUI.this,
                                "Failed to load products: "
                                        + ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }

            }.execute();
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == addProduct) {

                JOptionPane.showMessageDialog(
                        this,
                        "Add Product clicked"
                );

            } else if (e.getSource() == btnEdit) {

                JOptionPane.showMessageDialog(
                        this,
                        "Edit Product clicked"
                );

            } else if (e.getSource() == btnDelete) {

                JOptionPane.showMessageDialog(
                        this,
                        "Delete Product clicked"
                );
            }
        }
    }

