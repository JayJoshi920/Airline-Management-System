package airline;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Random;

public class PaymentFrame extends JFrame {

    private JTextField txtPNR;
    private JTextField txtAmount;
    private JTextField txtPaymentDate;
    private JComboBox<String> comboMethod;
    private JButton btnPay, btnClose;

    public PaymentFrame() {
        setTitle("Payment");
        setSize(720, 320);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        initUI();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.PRIMARY_BG);

        // HEADER
        JLabel lblHeader = new JLabel("Record Payment");
        lblHeader.setFont(UITheme.TITLE_FONT);
        lblHeader.setForeground(UITheme.TEXT_LIGHT);

        JLabel lblSub = new JLabel("Enter PNR, amount and payment method");
        lblSub.setFont(UITheme.SUBTITLE_FONT);
        lblSub.setForeground(UITheme.TEXT_MUTED);

        JPanel headerText = new JPanel(new GridLayout(2, 1));
        headerText.setOpaque(false);
        headerText.add(lblHeader);
        headerText.add(lblSub);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UITheme.PRIMARY_BG);
        header.setBorder(BorderFactory.createEmptyBorder(10, 15, 0, 15));
        header.add(headerText, BorderLayout.WEST);

        // LEFT FORM
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UITheme.PRIMARY_BG);
        form.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtPNR = createField();
        txtAmount = createField();
        txtPaymentDate = createField();
        txtPaymentDate.setText(LocalDate.now().toString());
        txtPaymentDate.setToolTipText("Format: YYYY-MM-DD");

        comboMethod = new JComboBox<>(new String[]{"CARD", "UPI", "CASH"});
        comboMethod.setBackground(Color.WHITE);
        comboMethod.setForeground(Color.BLACK);

        int row = 0;
        addRow(form, gbc, row++, "PNR:", txtPNR);
        addRow(form, gbc, row++, "Amount:", txtAmount);
        addRow(form, gbc, row++, "Method:", comboMethod);
        addRow(form, gbc, row++, "Payment Date:", txtPaymentDate);

        btnPay = new JButton("Pay");
        btnClose = new JButton("Close");
        stylePrimary(btnPay);
        styleSecondary(btnClose);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(UITheme.PRIMARY_BG);
        btnPanel.add(btnPay);
        btnPanel.add(btnClose);

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 1.0;
        form.add(btnPanel, gbc);

        // RIGHT: CARD + QR STYLE IMAGE
        JPanel illustration = new PaymentIllustrationPanel();
        illustration.setPreferredSize(new Dimension(260, 0));

        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(UITheme.PRIMARY_BG);
        center.add(form, BorderLayout.CENTER);
        center.add(illustration, BorderLayout.EAST);

        root.add(header, BorderLayout.NORTH);
        root.add(center, BorderLayout.CENTER);

        setContentPane(root);

        // Actions
        btnPay.addActionListener(e -> makePayment());
        btnClose.addActionListener(e -> dispose());
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

    // 💳 Illustration: card + mini QR (sirf image feel)
    private static class PaymentIllustrationPanel extends JPanel {
        PaymentIllustrationPanel() {
            setBackground(UITheme.PRIMARY_BG);
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int w = getWidth();
            int h = getHeight();

            int cardW = w - 30;
            int cardH = h - 50;
            if (cardH < 150) cardH = 150;
            int cardX = 10;
            int cardY = 20;

            // main card
            g2.setColor(UITheme.CARD_BG);
            g2.fillRoundRect(cardX, cardY, cardW, cardH, 24, 24);

            // top stripe
            g2.setColor(UITheme.ACCENT);
            g2.fillRoundRect(cardX + 15, cardY + 25, cardW - 30, 18, 10, 10);

            // chip
            g2.setColor(new Color(148, 163, 184));
            g2.fillRoundRect(cardX + 25, cardY + 60, 30, 22, 6, 6);

            // masked card number
            g2.setColor(UITheme.TEXT_LIGHT);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            g2.drawString("****  ****  ****  1234", cardX + 70, cardY + 78);

            // QR-like square
            int qrSize = 60;
            int qrX = cardX + cardW - qrSize - 20;
            int qrY = cardY + cardH - qrSize - 30;
            g2.setColor(Color.WHITE);
            g2.fillRect(qrX, qrY, qrSize, qrSize);
            g2.setColor(UITheme.CARD_BG);
            for (int i = 0; i < 3; i++) {
                int s = 18 - i * 6;
                g2.fillRect(qrX + 4 + i * 2, qrY + 4 + i * 2, s, s);
            }
        }
    }

    // ---------- PAYMENT LOGIC ----------

    private String generateTransactionId() {
        Random random = new Random();
        int num = 100000 + random.nextInt(900000);
        return "TXN" + num;
    }

    private void makePayment() {
        String pnr = txtPNR.getText().trim();
        String amountStr = txtAmount.getText().trim();
        String method = (String) comboMethod.getSelectedItem();
        String date = txtPaymentDate.getText().trim();

        if (pnr.isEmpty() || amountStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "PNR and Amount are required!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Invalid amount!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (date.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Payment date is required!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String txnId = generateTransactionId();

        try {
            Connection con = DBConnection.getConnection();

            // booking find by PNR
            String findSql = "SELECT id FROM bookings WHERE pnr = ?";
            PreparedStatement ps = con.prepareStatement(findSql);
            ps.setString(1, pnr);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(this,
                        "No booking found for this PNR!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int bookingId = rs.getInt("id");

            String insertSql = "INSERT INTO payments " +
                    "(booking_id, amount, method, payment_date, transaction_id) " +
                    "VALUES (?, ?, ?, ?, ?)";
            ps = con.prepareStatement(insertSql);
            ps.setInt(1, bookingId);
            ps.setDouble(2, amount);
            ps.setString(3, method);
            ps.setString(4, date);
            ps.setString(5, txnId);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this,
                        "Payment recorded successfully!");
                txtPNR.setText("");
                txtAmount.setText("");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to record payment.",
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
