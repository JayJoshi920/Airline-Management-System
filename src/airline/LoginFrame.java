package airline;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnExit;
    private JRadioButton rbAdmin, rbPassenger;
    private JLabel lblUser, lblPass;

    public LoginFrame() {
        setTitle("Airline Management - Login");
        setSize(420, 320);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        initUI();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.PRIMARY_BG);

        // Header
        JLabel lblTitle = new JLabel("Airline Login Portal");
        lblTitle.setFont(UITheme.TITLE_FONT);
        lblTitle.setForeground(UITheme.TEXT_LIGHT);

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(UITheme.PRIMARY_BG);
        header.setBorder(BorderFactory.createEmptyBorder(10, 15, 0, 15));
        header.add(lblTitle);

        // Center: form
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UITheme.PRIMARY_BG);
        form.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Radio buttons: Admin / Passenger
        rbAdmin = new JRadioButton("Admin");
        rbPassenger = new JRadioButton("Passenger");
        rbAdmin.setSelected(true);

        styleRadio(rbAdmin);
        styleRadio(rbPassenger);

        ButtonGroup group = new ButtonGroup();
        group.add(rbAdmin);
        group.add(rbPassenger);

        JPanel modePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        modePanel.setBackground(UITheme.PRIMARY_BG);
        modePanel.add(rbAdmin);
        modePanel.add(rbPassenger);

        lblUser = new JLabel("Username:");
        lblUser.setForeground(UITheme.TEXT_LIGHT);
        lblPass = new JLabel("Password:");
        lblPass.setForeground(UITheme.TEXT_LIGHT);

        txtUsername = new JTextField(18);
        txtPassword = new JPasswordField(18);
        styleField(txtUsername);
        styleField(txtPassword);

        btnLogin = new JButton("Login");
        btnExit = new JButton("Exit");
        stylePrimary(btnLogin);
        styleSecondary(btnExit);

        int row = 0;

        // Mode row
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        form.add(modePanel, gbc);
        gbc.gridwidth = 1;
        row++;

        // Username / PNR row
        gbc.gridx = 0; gbc.gridy = row;
        form.add(lblUser, gbc);
        gbc.gridx = 1;
        form.add(txtUsername, gbc);
        row++;

        // Password row
        gbc.gridx = 0; gbc.gridy = row;
        form.add(lblPass, gbc);
        gbc.gridx = 1;
        form.add(txtPassword, gbc);
        row++;

        // Buttons row
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(UITheme.PRIMARY_BG);
        btnPanel.add(btnLogin);
        btnPanel.add(btnExit);

        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        form.add(btnPanel, gbc);

        root.add(header, BorderLayout.NORTH);
        root.add(form, BorderLayout.CENTER);

        setContentPane(root);

        // Listeners
        rbAdmin.addActionListener(e -> switchMode());
        rbPassenger.addActionListener(e -> switchMode());

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        btnExit.addActionListener(e -> System.exit(0));
    }

    private void styleRadio(JRadioButton rb) {
        rb.setBackground(UITheme.PRIMARY_BG);
        rb.setForeground(UITheme.TEXT_LIGHT);
        rb.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    }

    private void styleField(JTextField tf) {
        tf.setBackground(Color.WHITE);
        tf.setForeground(Color.BLACK);
        tf.setCaretColor(Color.BLACK);
        tf.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(UITheme.ACCENT, 1),
                        BorderFactory.createEmptyBorder(4, 6, 4, 6)
                )
        );
    }

    private void stylePrimary(JButton btn) {
        btn.setFont(UITheme.BUTTON_FONT);
        btn.setBackground(UITheme.ACCENT);
        btn.setForeground(UITheme.PRIMARY_BG);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
    }

    private void styleSecondary(JButton btn) {
        btn.setFont(UITheme.BUTTON_FONT);
        btn.setBackground(UITheme.CARD_BG);
        btn.setForeground(UITheme.TEXT_LIGHT);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
    }

    // Mode update: Admin vs Passenger
    private void switchMode() {
        if (rbAdmin.isSelected()) {
            lblUser.setText("Username:");
            lblPass.setText("Password:");
            txtPassword.setEnabled(true);
            txtPassword.setText("");
        } else {
            lblUser.setText("PNR:");
            lblPass.setText("Password (disabled):");
            txtPassword.setEnabled(false);
            txtPassword.setText("");
        }
    }

    // Login button logic
    private void handleLogin() {
        if (rbAdmin.isSelected()) {
            handleAdminLogin();
        } else {
            handlePassengerLogin();
        }
    }

    // -------- ADMIN LOGIN --------
    private void handleAdminLogin() {
        String user = txtUsername.getText().trim();
        String pass = new String(txtPassword.getPassword());

        // yahan tum apna actual admin check laga sakte ho
        // abhi ke liye simple:
        if (user.equals("admin") && pass.equals("admin")) {
            JOptionPane.showMessageDialog(this, "Admin login successful!");
            // Dashboard open
            new DashboardFrame(user).setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Invalid admin username or password!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // -------- PASSENGER LOGIN (READ-ONLY PORTAL) --------
    private void handlePassengerLogin() {
        String pnr = txtUsername.getText().trim();

        if (pnr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter PNR!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Passenger portal open, sirf read-only ticket view
        PassengerPortalFrame frame = new PassengerPortalFrame(pnr);
        frame.setVisible(true);
        this.dispose();
    }

    // Test run
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
