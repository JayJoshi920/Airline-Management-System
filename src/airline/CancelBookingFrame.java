package airline;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.awt.geom.AffineTransform;


public class CancelBookingFrame extends JFrame {

    private JTextField txtPNR;
    private JTextField txtRefundAmount;
    private JTextField txtCancelDate;
    private JTextArea txtReason;
    private JButton btnCancelBooking, btnClose;

    public CancelBookingFrame() {
        setTitle("Cancel Booking");
        setSize(720, 340);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        initUI();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.PRIMARY_BG);

        // HEADER
        JLabel lblHeader = new JLabel("Cancel Booking");
        lblHeader.setFont(UITheme.TITLE_FONT);
        lblHeader.setForeground(UITheme.TEXT_LIGHT);

        JLabel lblSub = new JLabel("Enter PNR, refund (optional) and reason");
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
        txtRefundAmount = createField();
        txtCancelDate = createField();
        txtCancelDate.setText(LocalDate.now().toString());
        txtCancelDate.setToolTipText("Format: YYYY-MM-DD");

        txtReason = new JTextArea(4, 20);
        txtReason.setLineWrap(true);
        txtReason.setWrapStyleWord(true);
        txtReason.setBackground(Color.WHITE);
        txtReason.setForeground(Color.BLACK);
        txtReason.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(UITheme.ACCENT, 1),
                        BorderFactory.createEmptyBorder(4, 6, 4, 6)
                )
        );
        JScrollPane reasonScroll = new JScrollPane(txtReason);

        int row = 0;
        addRow(form, gbc, row++, "PNR:", txtPNR);
        addRow(form, gbc, row++, "Refund Amount:", txtRefundAmount);

        JLabel lblReason = new JLabel("Reason:");
        lblReason.setForeground(UITheme.TEXT_LIGHT);
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        form.add(lblReason, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        form.add(reasonScroll, gbc);
        row++;

        addRow(form, gbc, row++, "Cancellation Date:", txtCancelDate);

        btnCancelBooking = new JButton("Cancel Booking");
        btnClose = new JButton("Close");
        stylePrimary(btnCancelBooking);
        styleSecondary(btnClose);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(UITheme.PRIMARY_BG);
        btnPanel.add(btnCancelBooking);
        btnPanel.add(btnClose);

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 1.0;
        form.add(btnPanel, gbc);

        // RIGHT: CANCEL IMAGE (plane + red cross feel)
        JPanel illustration = new CancelIllustrationPanel();
        illustration.setPreferredSize(new Dimension(260, 0));

        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(UITheme.PRIMARY_BG);
        center.add(form, BorderLayout.CENTER);
        center.add(illustration, BorderLayout.EAST);

        root.add(header, BorderLayout.NORTH);
        root.add(center, BorderLayout.CENTER);

        setContentPane(root);

        // Actions
        btnCancelBooking.addActionListener(e -> cancelBooking());
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

    // ❌ Cancel illustration
    private static class CancelIllustrationPanel extends JPanel {
        CancelIllustrationPanel() {
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

            g2.setColor(UITheme.CARD_BG);
            g2.fillRoundRect(cardX, cardY, cardW, cardH, 24, 24);

            // inside plane
            int cx = cardX + cardW / 2;
            int cy = cardY + cardH / 2;

            int bodyLen = 70;
            int bodyH = 16;
            g2.setColor(new Color(148, 163, 184));
            g2.fillRoundRect(cx - bodyLen / 2, cy - bodyH / 2, bodyLen, bodyH, 12, 12);

            int noseSize = 14;
            g2.fillPolygon(
                    new int[]{cx + bodyLen / 2, cx + bodyLen / 2 + noseSize, cx + bodyLen / 2},
                    new int[]{cy - bodyH / 2, cy, cy + bodyH / 2},
                    3
            );

            // red cancel cross over plane
            g2.setStroke(new BasicStroke(3f));
            g2.setColor(new Color(239, 68, 68));
            int r = 50;
            g2.drawOval(cx - r / 2, cy - r / 2, r, r);
            g2.drawLine(cx - r / 2 + 5, cy - r / 2 + 5, cx + r / 2 - 5, cy + r / 2 - 5);
            g2.drawLine(cx + r / 2 - 5, cy - r / 2 + 5, cx - r / 2 + 5, cy + r / 2 - 5);
        }
    }

    // ---------- CANCEL LOGIC ----------

    private void cancelBooking() {
        String pnr = txtPNR.getText().trim();
        String refundStr = txtRefundAmount.getText().trim();
        String reason = txtReason.getText().trim();
        String date = txtCancelDate.getText().trim();

        if (pnr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "PNR is required!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double refund = 0;
        if (!refundStr.isEmpty()) {
            try {
                refund = Double.parseDouble(refundStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Invalid refund amount!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        if (date.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Cancellation date is required!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Connection con = DBConnection.getConnection();

            String findSql = "SELECT id, status FROM bookings WHERE pnr = ?";
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
            String currentStatus = rs.getString("status");
            if ("CANCELLED".equalsIgnoreCase(currentStatus)) {
                JOptionPane.showMessageDialog(this,
                        "This booking is already cancelled.",
                        "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String insertSql = "INSERT INTO cancellations " +
                    "(booking_id, cancellation_date, reason, refund_amount) " +
                    "VALUES (?, ?, ?, ?)";
            ps = con.prepareStatement(insertSql);
            ps.setInt(1, bookingId);
            ps.setString(2, date);
            ps.setString(3, reason);
            ps.setDouble(4, refund);

            int rows = ps.executeUpdate();

            String updateSql = "UPDATE bookings SET status = 'CANCELLED' WHERE id = ?";
            PreparedStatement ps2 = con.prepareStatement(updateSql);
            ps2.setInt(1, bookingId);
            ps2.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this,
                        "Booking cancelled successfully!");
                txtPNR.setText("");
                txtRefundAmount.setText("");
                txtReason.setText("");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to cancel booking.",
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
