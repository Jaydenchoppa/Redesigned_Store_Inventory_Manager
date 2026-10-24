package views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import domain.Store;

public class StoreGUI extends JFrame implements ActionListener {
    private JPanel pnlNorth, pnlSouth, pnlCentre;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel lbl;
    private JButton addStore, btnDelete, btnEdit;
    private ClientApp client;

    public StoreGUI() {
        pnlNorth = new JPanel();
        pnlSouth = new JPanel();
        pnlCentre = new JPanel();

        client = new ClientApp();

        tableModel = new DefaultTableModel(new Object[]{"ID", "Store Name", "Phone Number", "Location"}, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;

            }
        };

        table = new JTable(tableModel);
        addStore = new JButton("Add store");
        btnDelete = new JButton("Delete");
        btnEdit = new JButton("Edit");
        lbl = new JLabel("Store Manager");

        setGUI();
        loadStores();
        }

        public void setGUI(){
            setLayout(new BorderLayout());


            pnlNorth.setLayout(new BorderLayout());

            lbl.setText("Store Manager");
            lbl.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 22));

            pnlNorth.add(lbl, BorderLayout.WEST);
            pnlNorth.add(addStore, BorderLayout.EAST);

            pnlCentre.setLayout(new BorderLayout());


            JScrollPane scrollPane = new JScrollPane(table);

            pnlCentre.add(scrollPane, BorderLayout.CENTER);
            pnlSouth.setLayout(new FlowLayout(FlowLayout.RIGHT));

            pnlSouth.add(btnEdit);
            pnlSouth.add(btnDelete);

            add(pnlNorth, BorderLayout.NORTH);
            add(pnlCentre, BorderLayout.CENTER);
            add(pnlSouth, BorderLayout.SOUTH);

            addStore.addActionListener(this);
            btnEdit.addActionListener(this);
            btnDelete.addActionListener(this);

            setTitle("Store Management System");
            setSize(800, 500);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setVisible(true);


    }
    private void loadStores() {
        new SwingWorker<List<Store>, Void>() {

            @Override
            protected List<Store> doInBackground() throws Exception {

                return client.getAllStores();
            }


            @Override
            protected void done() {

                try {


                    tableModel.setRowCount(0);


                    for (Store s : get()) {

                        tableModel.addRow(new Object[]{

                                s.getStoreId(),
                                s.getStoreName(),
                                s.getStorePhoneNumber(),
                                s.getStoreLocation()

                        });
                    }

                } catch (Exception ex) {

                    JOptionPane.showMessageDialog(
                            StoreGUI.this,
                            "Failed to load stores: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }

        }.execute();
    }
    @Override
    public void actionPerformed(ActionEvent e) {


    }
}
