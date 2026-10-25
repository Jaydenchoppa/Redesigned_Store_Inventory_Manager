package views;

import domain.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Jayden Avontuur
 */
public class LoginGUI extends JFrame implements ActionListener {

    private JPanel pnlNorth, pnlCenter, pnlSouth;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JRadioButton radioEmployee, radioCustomer;
    private ButtonGroup roleGroup;
    private JButton btnLogin, btnGoToRegister;
    private JLabel lblTitle, lblUsername, lblPassword, lblRole;
    private ClientApp client;

    public LoginGUI(ClientApp client) {
        super("Store Inventory Manager - Login");
        this.client = client;

        pnlNorth = new JPanel();
        pnlCenter = new JPanel();
        pnlSouth = new JPanel();
        txtUsername = new JTextField(20);
        txtPassword = new JPasswordField(20);

        radioEmployee = new JRadioButton("Employee", true);
        radioCustomer = new JRadioButton("Customer");
        roleGroup = new ButtonGroup();
        roleGroup.add(radioEmployee);
        roleGroup.add(radioCustomer);

        btnLogin = new JButton("Login");
        btnGoToRegister = new JButton("Sign Up as Customer");
        lblTitle = new JLabel("Store Inventory Manager");
        lblUsername = new JLabel("Username:");
        lblPassword = new JLabel("Password:");
        lblRole = new JLabel("Login as:");
    }

    public void setGUI() {
        pnlNorth.setLayout(new FlowLayout());
        pnlCenter.setLayout(new GridLayout(3, 2, 5, 5));
        pnlSouth.setLayout(new FlowLayout());

        pnlNorth.add(lblTitle);

        pnlCenter.add(lblRole);
        JPanel rolePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rolePanel.add(radioEmployee);
        rolePanel.add(radioCustomer);
        pnlCenter.add(rolePanel);

        pnlCenter.add(lblUsername);
        pnlCenter.add(txtUsername);
        pnlCenter.add(lblPassword);
        pnlCenter.add(txtPassword);

        pnlSouth.add(btnLogin);
        pnlSouth.add(btnGoToRegister);

        this.add(pnlNorth, BorderLayout.NORTH);
        this.add(pnlCenter, BorderLayout.CENTER);
        this.add(pnlSouth, BorderLayout.SOUTH);

        btnLogin.addActionListener(this);
        btnGoToRegister.addActionListener(this);

        this.getRootPane().setDefaultButton(btnLogin); // Enter key submits
        this.setSize(600, 220);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLogin) {
            attemptLogin();
        } else if (e.getSource() == btnGoToRegister) {
            this.dispose();
            new CustomerRegisterGUI(client).setGUI();
        }
    }

    private void attemptLogin() {
        final String username = txtUsername.getText().trim();
        final String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        final boolean asEmployee = radioEmployee.isSelected();
        btnLogin.setEnabled(false);
        btnLogin.setText("Logging in...");

        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() {
                return asEmployee
                        ? client.authenticateUser(username, password)
                        : client.authenticateCustomer(username, password);
            }

            @Override
            protected void done() {
                btnLogin.setEnabled(true);
                btnLogin.setText("Login");

                boolean success;
                try {
                    success = get();
                } catch (Exception ex) {
                    success = false;
                }

                if (!success) {
                    String message = client.getLastError().isEmpty() ? "Invalid login!" : client.getLastError();
                    JOptionPane.showMessageDialog(LoginGUI.this, message, "Login Failed", JOptionPane.ERROR_MESSAGE);
                    txtPassword.setText("");
                    return;
                }

                dispose();
                if (asEmployee) {
                    new InventoryGUI(client).setGUI();
                } else {
                    new ShopGUI(client, username).setGUI();
                }
            }
        }.execute();
    }
}
