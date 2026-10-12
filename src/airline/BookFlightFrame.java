package airline;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.Random;

public class BookFlightFrame extends JFrame {

    private JComboBox<CustomerOption> comboCustomer;
    private JComboBox<FlightOption> comboFlight;
    private JTextField txtSeatNo;
    private JTextField txtBookingDate;
    private JButton btnBook, btnClose;

    // Customer option for dropdown
    private static class CustomerOption {
        int id;
        String label;
        CustomerOption(int id, String label) {
            this.id = id;
            this.label = label;
        }
        @Override
        public String toString() {
            return label;
        }
    }

    // Flight option for dropdown
    private static class FlightOption {
        int id;
        String label;
        FlightOption(int id, String label) {
            this.id = id;
            this.label = label;
        }
        @Override
        public String toString() {
            return label;
        }
    }

    public BookFlightFrame() {
        setTitle("Book Flight");
        setSize(720, 380);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        initUI();
        loadCustomers();
        loadFlights();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.PRIMARY_BG);

        // -------- HEADER --------
        JLabel lblHeader = new JLabel("Book Flight Ticket");
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

        comboCustomer = new JComboBox<>();
        comboFlight = new JComboBox<>();
        styleCombo(comboCustomer);
        styleCombo(comboFlight);

        txtSeatNo = createField();
        txtBookingDate = createField();
        txtBookingDate.setText(LocalDate.now().toString());
        txtBookingDate.setToolTipText("Format: YYYY-MM-DD");

        int row = 0;
        addRow(form, gbc, row++, "Customer:", comboCustomer);
        addRow(form, gbc, row++, "Flight:", comboFlight);
        addRow(form, gbc, row++, "Seat No:", txtSeatNo);
        addRow(form, gbc, row++, "Booking Date:", txtBookingDate);

        btnBook = new JButton("Confirm Booking");
        btnClose = new JButton("Close");
        stylePrimary(btnBook);
        styleSecondary(btnClose);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(UITheme.PRIMARY_BG);
        btnPanel.add(btnBook);
        btnPanel.add(btnClose);

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 1.0;
        form.add(btnPanel, gbc);

        // -------- RIGHT: ONLY PLANE IMAGE (NO TEXT) --------
        JPanel illustration = new FlightIllustrationPanel();
        illustration.setPreferredSize(new Dimension(260, 0));

        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(UITheme.PRIMARY_BG);
        center.add(form, BorderLayout.CENTER);
        center.add(illustration, BorderLayout.EAST);

        root.add(header, BorderLayout.NORTH);
        root.add(center, BorderLayout.CENTER);

        setContentPane(root);

        // Actions
        btnBook.addActionListener(e -> bookFlight());
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

    private void styleCombo(JComboBox<?> combo) {
        combo.setBackground(Color.WHITE);
        combo.setForeground(Color.BLACK);
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

    // ✈ Plane image-like drawing – sirf plane + line, NO TEXT
    private static class FlightIllustrationPanel extends JPanel {
        FlightIllustrationPanel() {
            setBackground(UITheme.CARD_BG);
            setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
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
            int y = h / 2;

            // card
            int cardX = 10;
            int cardY = 20;
            int cardW = w - 30;
            int cardH = h - 50;

            g2.setColor(new Color(15, 23, 42));
            g2.fillRoundRect(cardX, cardY, cardW, cardH, 24, 24);

            // route line
            int x1 = cardX + 35;
            int x2 = cardX + cardW - 35;

            g2.setColor(UITheme.TEXT_LIGHT);
            g2.setStroke(new BasicStroke(2f));
            g2.drawLine(x1, y, x2, y);

            // plane — side view
            int planeX = (x1 + x2) / 2;
            int bodyLen = 80;
            int bodyH = 16;

            // body
            g2.setColor(UITheme.ACCENT);
            g2.fillRoundRect(planeX - bodyLen / 2, y - bodyH / 2, bodyLen, bodyH, 12, 12);

            // nose
            int noseSize = 14;
            g2.fillPolygon(
                    new int[]{planeX + bodyLen / 2, planeX + bodyLen / 2 + noseSize, planeX + bodyLen / 2},
                    new int[]{y - bodyH / 2, y, y + bodyH / 2},
                    3
            );

            // tail
            int tailSize = 12;
            g2.fillPolygon(
                    new int[]{planeX - bodyLen / 2, planeX - bodyLen / 2 - tailSize, planeX - bodyLen / 2},
                    new int[]{y - bodyH / 2, y - bodyH / 2 - tailSize, y},
                    3
            );

            // wing
            g2.fillPolygon(
                    new int[]{planeX - 8, planeX + 8, planeX + 20, planeX - 20},
                    new int[]{y, y, y - 20, y - 20},
                    4
            );
        }
    }

    // ------- DATA LOADERS -------

    private void loadCustomers() {
        comboCustomer.removeAllItems();
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT id, name FROM customers";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String label = id + " - " + rs.getString("name");
                comboCustomer.addItem(new CustomerOption(id, label));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading customers: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadFlights() {
        comboFlight.removeAllItems();
        try {
            Connection con = DBConnection.getConnection();

            // Simple flight list
            String sql = "SELECT id, flight_code FROM flights";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String label = rs.getString("flight_code");
                comboFlight.addItem(new FlightOption(id, label));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading flights: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ------- BOOKING LOGIC -------

    private String generatePNR() {
        Random random = new Random();
        int num = 100000 + random.nextInt(900000);
        return "PNR" + num;
    }

    private void bookFlight() {
        CustomerOption customer = (CustomerOption) comboCustomer.getSelectedItem();
        FlightOption flight = (FlightOption) comboFlight.getSelectedItem();
        String seat = txtSeatNo.getText().trim();
        String date = txtBookingDate.getText().trim();

        if (customer == null || flight == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select customer and flight!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (seat.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Seat number is required!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (date.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Booking date is required!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String pnr = generatePNR();

        try {
            Connection con = DBConnection.getConnection();
            String sql = "INSERT INTO bookings " +
                    "(pnr, customer_id, flight_id, booking_date, status, seat_no) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, pnr);
            ps.setInt(2, customer.id);
            ps.setInt(3, flight.id);
            ps.setString(4, date);
            ps.setString(5, "CONFIRMED");
            ps.setString(6, seat);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this,
                        "Flight booked successfully!\nPNR: " + pnr);
                txtSeatNo.setText("");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to book flight.",
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
