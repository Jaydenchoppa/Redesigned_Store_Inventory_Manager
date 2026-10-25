package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerRegisterGUI extends JFrame implements ActionListener {

    private final JTextField txtFirstName = new JTextField(20);
    private final JTextField txtSurname = new JTextField(20);
    private final JTextField txtEmail = new JTextField(20);
    private final JTextField txtUsername = new JTextField(20);
    private final JPasswordField txtPassword = new JPasswordField(20);
    private final JPasswordField txtConfirm = new JPasswordField(20);
    private final JButton btnRegister = new JButton("Sign Up");
    private final JButton btnBack = new JButton("Back to Login");
    private final ClientApp client;

    public CustomerRegisterGUI(ClientApp client) {
        super("Customer Sign Up");
        this.client = client;
    }

    public void setGUI() {
        JPanel form = new JPanel(new GridLayout(6, 2, 5, 5));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        form.add(new JLabel("First name:")); form.add(txtFirstName);
        form.add(new JLabel("Surname:")); form.add(txtSurname);
        form.add(new JLabel("Email:")); form.add(txtEmail);
        form.add(new JLabel("Username:")); form.add(txtUsername);
        form.add(new JLabel("Password (min 6):")); form.add(txtPassword);
        form.add(new JLabel("Confirm password:")); form.add(txtConfirm);

        JPanel buttons = new JPanel();
        buttons.add(btnRegister);
        buttons.add(btnBack);

        setLayout(new BorderLayout());
        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        btnRegister.addActionListener(this);
        btnBack.addActionListener(this);
        getRootPane().setDefaultButton(btnRegister);

        setSize(430, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnRegister) {
            register();
        } else {
            dispose();
            new LoginGUI(client).setGUI();
        }
    }

    private void register() {
        final String firstName = txtFirstName.getText().trim();
        final String surname = txtSurname.getText().trim();
        final String email = txtEmail.getText().trim();
        final String username = txtUsername.getText().trim();
        final String password = new String(txtPassword.getPassword());
        final String confirm = new String(txtConfirm.getPassword());

        if (firstName.isEmpty() || surname.isEmpty()) {
            warn("Please enter your first name and surname.");
            return;
        }
        if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            warn("Please enter a valid email address.");
            return;
        }
        if (!username.matches("[A-Za-z0-9._-]{3,30}")) {
            warn("Username must be 3-30 characters: letters, numbers, . _ or -");
            return;
        }
        if (password.length() < 6) {
            warn("Password must be at least 6 characters.");
            return;
        }
        if (!password.equals(confirm)) {
            warn("The two passwords do not match.");
            return;
        }

        btnRegister.setEnabled(false);
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                client.registerCustomer(firstName, surname, email, username, password);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    JOptionPane.showMessageDialog(CustomerRegisterGUI.this, "Account created! Please log in.");
                    dispose();
                    new LoginGUI(client).setGUI();
                } catch (Exception ex) {
                    btnRegister.setEnabled(true);
                    String message = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
                    JOptionPane.showMessageDialog(CustomerRegisterGUI.this, message,
                            "Sign up failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private void warn(String message) {
        JOptionPane.showMessageDialog(this, message, "Check your details", JOptionPane.WARNING_MESSAGE);
    }
}
