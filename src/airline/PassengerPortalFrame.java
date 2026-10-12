package airline;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PassengerPortalFrame extends JFrame {

    private JTextField txtPNR;
    private JLabel lblNameValue;
    private JLabel lblFlightValue;
    private JLabel lblRouteValue;
    private JLabel lblDateValue;
    private JLabel lblSeatValue;
    private JLabel lblStatusValue;

    private JButton btnView, btnClose;

    public PassengerPortalFrame() {
        setTitle("Passenger Ticket View");
        setSize(650, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        initUI();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.PRIMARY_BG);

        // -------- HEADER --------
        JLabel lblHeader = new JLabel("Passenger Ticket Portal");
        lblHeader.setFont(UITheme.TITLE_FONT);
        lblHeader.setForeground(UITheme.TEXT_LIGHT);

        JLabel lblSub = new JLabel("Enter your PNR to view ticket details");
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

        // -------- TOP: PNR INPUT --------
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        searchPanel.setBackground(UITheme.PRIMARY_BG);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        JLabel lblPNR = new JLabel("PNR:");
        lblPNR.setForeground(UITheme.TEXT_LIGHT);

        txtPNR = new JTextField(15);
        txtPNR.setBackground(Color.WHITE);
        txtPNR.setForeground(Color.BLACK);
        txtPNR.setCaretColor(Color.BLACK);
        txtPNR.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.ACCENT, 1),
                BorderFactory.createEmptyBorder(3, 6, 3, 6)
        ));

        btnView = new JButton("View Ticket");
        btnClose = new JButton("Close");
        stylePrimary(btnView);
        styleSecondary(btnClose);

        searchPanel.add(lblPNR);
        searchPanel.add(txtPNR);
        searchPanel.add(btnView);
        searchPanel.add(btnClose);

        JPanel north = new JPanel(new BorderLayout());
        north.setBackground(UITheme.PRIMARY_BG);
        north.add(header, BorderLayout.NORTH);
        north.add(searchPanel, BorderLayout.SOUTH);

        // -------- CENTER: TICKET DETAILS --------
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBackground(UITheme.PRIMARY_BG);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 25, 15, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;

        lblNameValue = createValueLabel();
        lblFlightValue = createValueLabel();
        lblRouteValue = createValueLabel();
        lblDateValue = createValueLabel();
        lblSeatValue = createValueLabel();
        lblStatusValue = createValueLabel();

        int row = 0;
        addRow(detailsPanel, gbc, row++, "Passenger Name:", lblNameValue);
        addRow(detailsPanel, gbc, row++, "Flight Code:", lblFlightValue);
        addRow(detailsPanel, gbc, row++, "Route:", lblRouteValue);
        addRow(detailsPanel, gbc, row++, "Booking Date:", lblDateValue);
        addRow(detailsPanel, gbc, row++, "Seat No:", lblSeatValue);
        addRow(detailsPanel, gbc, row++, "Status:", lblStatusValue);

        root.add(north, BorderLayout.NORTH);
        root.add(detailsPanel, BorderLayout.CENTER);

        setContentPane(root);

        // Actions
        btnView.addActionListener(e -> fetchTicket());
        btnClose.addActionListener(e -> dispose());
    }

    private JLabel createValueLabel() {
        JLabel lbl = new JLabel("-");
        lbl.setForeground(UITheme.TEXT_LIGHT);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return lbl;
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row,
                        String label, JComponent valueComp) {

        JLabel lbl = new JLabel(label);
        lbl.setForeground(UITheme.TEXT_MUTED);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(valueComp, gbc);
    }

    private void stylePrimary(JButton btn) {
        btn.setFont(UITheme.BUTTON_FONT);
        btn.setBackground(UITheme.ACCENT);
        btn.setForeground(UITheme.PRIMARY_BG);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 16, 5, 16));
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
    }

    private void styleSecondary(JButton btn) {
        btn.setFont(UITheme.BUTTON_FONT);
        btn.setBackground(UITheme.CARD_BG);
        btn.setForeground(UITheme.TEXT_LIGHT);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 16, 5, 16));
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
    }

    // --------- DB LOGIC: GET TICKET BY PNR ---------
    private void fetchTicket() {
        String pnr = txtPNR.getText().trim();

        if (pnr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter PNR!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        clearLabels();

        try {
            Connection con = DBConnection.getConnection();

            String sql =
                    "SELECT b.pnr, c.name AS customer, f.flight_code, " +
                    "a1.code AS src, a2.code AS dst, " +
                    "b.booking_date, b.status, b.seat_no " +
                    "FROM bookings b " +
                    "JOIN customers c ON b.customer_id = c.id " +
                    "JOIN flights f ON b.flight_id = f.id " +
                    "JOIN airports a1 ON f.source_airport_id = a1.id " +
                    "JOIN airports a2 ON f.destination_airport_id = a2.id " +
                    "WHERE b.pnr = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, pnr);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                lblNameValue.setText(rs.getString("customer"));
                lblFlightValue.setText(rs.getString("flight_code"));
                lblRouteValue.setText(
                        rs.getString("src") + " ➜ " + rs.getString("dst")
                );
                lblDateValue.setText(rs.getString("booking_date"));
                lblSeatValue.setText(rs.getString("seat_no"));
                lblStatusValue.setText(rs.getString("status"));
            } else {
                JOptionPane.showMessageDialog(this,
                        "No ticket found for PNR: " + pnr,
                        "Info", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearLabels() {
        lblNameValue.setText("-");
        lblFlightValue.setText("-");
        lblRouteValue.setText("-");
        lblDateValue.setText("-");
        lblSeatValue.setText("-");
        lblStatusValue.setText("-");
    }
    // Extra constructor: direct PNR se open
    public PassengerPortalFrame(String pnr) {
        this(); // pehle UI bana
        txtPNR.setText(pnr);
        fetchTicket(); // auto ticket load
    }


    // For direct testing (optional)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PassengerPortalFrame().setVisible(true));
    }
}
