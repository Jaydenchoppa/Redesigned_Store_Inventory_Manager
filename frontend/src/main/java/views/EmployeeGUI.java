package views;

/**
 * EmployeeGUI
 * Author: Templeton Liyabona Dyantyi
 * Student Number: 222623047
 */

import domain.Employee;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class EmployeeGUI extends JFrame implements ActionListener {

    private JPanel pnlNorth, pnlCenter, pnlSouth;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnAdd, btnDelete, btnRefresh;
    private JLabel lblTitle;
    private ClientApp client;

    public EmployeeGUI(ClientApp client) {
        super("Store Inventory Manager - Employees");
        this.client = client;

        pnlNorth = new JPanel();
        pnlCenter = new JPanel();
        pnlSouth = new JPanel();

        tableModel = new DefaultTableModel(new Object[]{
                "ID", "Employee Name", "Position", "Salary"
        }, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        table = new JTable(tableModel);

        btnAdd = new JButton("Add");
        btnDelete = new JButton("Delete");
        btnRefresh = new JButton("Refresh");

        lblTitle = new JLabel("Employee Management");
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

        loadEmployees();
    }

    private void loadEmployees() {

        new SwingWorker<List<Employee>, Void>() {

            protected List<Employee> doInBackground() throws Exception {
                return client.getAllEmployees();
            }

            protected void done() {

                try {

                    tableModel.setRowCount(0);

                    for (Employee employee : get()) {

                        tableModel.addRow(new Object[]{
                                employee.getEmployeeId(),
                                employee.getEmployeeName(),
                                employee.getPosition(),
                                employee.getSalary()
                        });
                    }

                } catch (Exception ex) {

                    JOptionPane.showMessageDialog(
                            EmployeeGUI.this,
                            "Failed to load employees: "
                                    + ex.getMessage()
                    );
                }
            }

        }.execute();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnRefresh) {

            loadEmployees();

        } else if (e.getSource() == btnAdd) {

            new EmployeeFormDialog(
                    this,
                    client,
                    this::loadEmployees
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
                    "Delete this employee?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            new SwingWorker<Void, Void>() {

                protected Void doInBackground() throws Exception {
                    client.deleteEmployee(id);
                    return null;
                }

                protected void done() {
                    loadEmployees();
                }

            }.execute();
        }
    }
}