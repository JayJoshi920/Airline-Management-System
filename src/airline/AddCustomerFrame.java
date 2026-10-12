package airline;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddCustomerFrame extends JFrame {

    private JTextField txtName;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JTextField txtPassport;
    private JTextArea txtAddress;
    private JButton btnSave, btnClose;

    public AddCustomerFrame() {
        setTitle("Add Customer");
        setSize(720, 380);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        initUI();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.PRIMARY_BG);

        // -------- HEADER --------
        JLabel lblHeader = new JLabel("Add New Customer");
        lblHeader.setFont(UITheme.TITLE_FONT);
        lblHeader.setForeground(UITheme.TEXT_LIGHT);

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(UITheme.PRIMARY_BG);
        header.setBorder(BorderFactory.createEmptyBorder(10, 15, 0, 15));
        header.add(lblHeader);

        // -------- LEFT FORM --------
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UITheme.PRIMARY_BG);
        form.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtName = createField();
        txtEmail = createField();
        txtPhone = createField();
        txtPassport = createField();

        txtAddress = new JTextArea(4, 20);
        txtAddress.setLineWrap(true);
        txtAddress.setWrapStyleWord(true);
        txtAddress.setBackground(Color.WHITE);
        txtAddress.setForeground(Color.BLACK);
        txtAddress.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(UITheme.ACCENT, 1),
                        BorderFactory.createEmptyBorder(4, 6, 4, 6)
                )
        );
        JScrollPane addrScroll = new JScrollPane(txtAddress);

        int row = 0;
        addRow(form, gbc, row++, "Name:", txtName);
        addRow(form, gbc, row++, "Email:", txtEmail);
        addRow(form, gbc, row++, "Phone:", txtPhone);
        addRow(form, gbc, row++, "Passport No:", txtPassport);

        JLabel lblAddress = new JLabel("Address:");
        lblAddress.setForeground(UITheme.TEXT_LIGHT);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        form.add(lblAddress, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        form.add(addrScroll, gbc);
        row++;

        // -------- BUTTONS --------
        btnSave = new JButton("Save");
        btnClose = new JButton("Close");
        stylePrimary(btnSave);
        styleSecondary(btnClose);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(UITheme.PRIMARY_BG);
        btnPanel.add(btnSave);
        btnPanel.add(btnClose);

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 1.0;
        form.add(btnPanel, gbc);

        // -------- RIGHT ILLUSTRATION PANEL --------
        JPanel illustration = new CustomerIllustrationPanel();
        illustration.setPreferredSize(new Dimension(240, 0));

        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(UITheme.PRIMARY_BG);
        center.add(form, BorderLayout.CENTER);
        center.add(illustration, BorderLayout.EAST);

        root.add(header, BorderLayout.NORTH);
        root.add(center, BorderLayout.CENTER);

        setContentPane(root);

        // Actions
        btnSave.addActionListener(e -> saveCustomer());
        btnClose.addActionListener(e -> dispose());
    }

    // 🔹 Simple avatar + ID-card style drawing
    private static class CustomerIllustrationPanel extends JPanel {
        CustomerIllustrationPanel() {
            setBackground(UITheme.CARD_BG);
            setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            // Title
            g2.setColor(UITheme.TEXT_MUTED);
            g2.setFont(UITheme.SUBTITLE_FONT);
            g2.drawString("Customer Profile", 10, 20);

            // Card background
            int cardW = w - 30;
            int cardH = h - 60;
            int cardX = 10;
            int cardY = 30;

            g2.setColor(new Color(15, 23, 42)); // dark navy
            g2.fillRoundRect(cardX, cardY, cardW, cardH, 18, 18);

            // Avatar circle
            int centerX = cardX + cardW / 2;
            int headR = 40;
            int headY = cardY + 30;

            g2.setColor(new Color(56, 189, 248)); // cyan
            g2.fillOval(centerX - headR / 2, headY, headR, headR);

            // Body (semi-circle)
            int bodyW = 70;
            int bodyH = 40;
            int bodyY = headY + headR - 5;
            g2.fillRoundRect(centerX - bodyW / 2, bodyY, bodyW, bodyH, 40, 40);

            // Name bar
            int barY = bodyY + bodyH + 15;
            g2.setColor(new Color(30, 64, 175)); // blue bar
            g2.fillRoundRect(cardX + 25, barY, cardW - 50, 18, 10, 10);

            g2.setColor(UITheme.TEXT_LIGHT);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            g2.drawString("Customer Name", cardX + 35, barY + 12);

            // Details lines (email/phone placeholder)
            g2.setColor(UITheme.TEXT_MUTED);
            int lineY1 = barY + 40;
            int lineY2 = lineY1 + 18;
            g2.drawString("Email / Phone", cardX + 30, lineY1);
            g2.drawString("Passport / Address", cardX + 30, lineY2);
        }
    }

    private JTextField createField() {
        JTextField tf = new JTextField(18);
        tf.setBackground(Color.WHITE);
        tf.setForeground(Color.BLACK);
        tf.setCaretColor(Color.BLACK);
        tf.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(UITheme.ACCENT, 1),
                        BorderFactory.createEmptyBorder(4, 6, 4, 6)
                )
        );
        return tf;
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row,
                        String labelText, JComponent field) {

        JLabel label = new JLabel(labelText);
        label.setForeground(UITheme.TEXT_LIGHT);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(field, gbc);
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

    private void saveCustomer() {
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();
        String passport = txtPassport.getText().trim();
        String address = txtAddress.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Name is required!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Connection con = DBConnection.getConnection();
            String sql = "INSERT INTO customers " +
                    "(name, email, phone, passport_no, address) " +
                    "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, passport);
            ps.setString(5, address);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this,
                        "Customer added successfully!");
                txtName.setText("");
                txtEmail.setText("");
                txtPhone.setText("");
                txtPassport.setText("");
                txtAddress.setText("");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to add customer.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
