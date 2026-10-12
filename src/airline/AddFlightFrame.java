package airline;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AddFlightFrame extends JFrame {

    private JTextField txtFlightCode;
    private JTextField txtDepartureTime;
    private JTextField txtArrivalTime;
    private JTextField txtBasePrice;
    private JComboBox<AirportOption> comboSource;
    private JComboBox<AirportOption> comboDestination;
    private JComboBox<String> comboDepAmPm;
    private JComboBox<String> comboArrAmPm;
    private JButton btnSave, btnClose;

    // Airport dropdown object
    private static class AirportOption {
        int id;
        String label;
        AirportOption(int id, String label) {
            this.id = id;
            this.label = label;
        }
        @Override
        public String toString() {
            return label;
        }
    }

    public AddFlightFrame() {
        setTitle("Add New Flight");
        setSize(720, 380);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        initUI();
        loadAirports();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.PRIMARY_BG);

        // -------- HEADER ----------
        JLabel lblHeader = new JLabel("Add New Flight");
        lblHeader.setFont(UITheme.TITLE_FONT);
        lblHeader.setForeground(UITheme.TEXT_LIGHT);

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(UITheme.PRIMARY_BG);
        header.setBorder(BorderFactory.createEmptyBorder(10, 15, 0, 15));
        header.add(lblHeader);

        // -------- LEFT FORM ----------
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UITheme.PRIMARY_BG);
        form.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtFlightCode = new JTextField(18);
        txtDepartureTime = new JTextField(12);
        txtArrivalTime = new JTextField(12);
        txtBasePrice = new JTextField(18);

        comboSource = new JComboBox<>();
        comboDestination = new JComboBox<>();
        comboDepAmPm = new JComboBox<>(new String[]{"AM", "PM"});
        comboArrAmPm = new JComboBox<>(new String[]{"AM", "PM"});

        // Hints
        txtDepartureTime.setToolTipText("Example: 10:30");
        txtArrivalTime.setToolTipText("Example: 14:45");

        int row = 0;
        addRow(form, gbc, row++, "Flight Code:", txtFlightCode);
        addRow(form, gbc, row++, "Source Airport:", comboSource);
        addRow(form, gbc, row++, "Destination Airport:", comboDestination);

        // Departure time + AM/PM
        JPanel depPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        depPanel.setBackground(UITheme.PRIMARY_BG);
        depPanel.add(txtDepartureTime);
        depPanel.add(comboDepAmPm);
        addRow(form, gbc, row++, "Departure Time:", depPanel);

        // Arrival time + AM/PM
        JPanel arrPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        arrPanel.setBackground(UITheme.PRIMARY_BG);
        arrPanel.add(txtArrivalTime);
        arrPanel.add(comboArrAmPm);
        addRow(form, gbc, row++, "Arrival Time:", arrPanel);

        addRow(form, gbc, row++, "Base Price:", txtBasePrice);

        btnSave = new JButton("Save");
        btnClose = new JButton("Close");
        stylePrimary(btnSave);
        styleSecondary(btnClose);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.setBackground(UITheme.PRIMARY_BG);
        btnPanel.add(btnSave);
        btnPanel.add(btnClose);

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 1.0;
        form.add(btnPanel, gbc);

        // -------- RIGHT: SIMPLE ROUTE DIAGRAM ----------
        JPanel diagramPanel = new FlightDiagramPanel();
        diagramPanel.setPreferredSize(new Dimension(260, 0));

        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(UITheme.PRIMARY_BG);
        center.add(form, BorderLayout.CENTER);
        center.add(diagramPanel, BorderLayout.EAST);

        root.add(header, BorderLayout.NORTH);
        root.add(center, BorderLayout.CENTER);

        setContentPane(root);

        btnSave.addActionListener(e -> saveFlight());
        btnClose.addActionListener(e -> dispose());
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

    // Small route diagram on right
    private static class FlightDiagramPanel extends JPanel {
        FlightDiagramPanel() {
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
            int y = h / 2;

            g2.setColor(UITheme.TEXT_MUTED);
            g2.setFont(UITheme.SUBTITLE_FONT);
            g2.drawString("Flight Route", 10, 20);

            int r = 18;
            int x1 = 40;
            int x2 = w - 60;

            g2.setColor(UITheme.ACCENT);
            g2.fillOval(x1 - r / 2, y - r / 2, r, r);

            g2.setColor(new Color(251, 191, 36));
            g2.fillOval(x2 - r / 2, y - r / 2, r, r);

            g2.setColor(UITheme.TEXT_LIGHT);
            g2.setStroke(new BasicStroke(2f));
            g2.drawLine(x1 + r / 2, y, x2 - r / 2, y);

            int ax = (x1 + x2) / 2;
            int size = 7;
            g2.fillPolygon(
                    new int[]{ax, ax - size, ax - size},
                    new int[]{y, y - size, y + size},
                    3
            );

            g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            g2.drawString("Source", x1 - 8, y + r + 12);
            g2.drawString("Destination", x2 - 25, y + r + 12);
        }
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

    // 🚨 IMPORTANT: Airports auto insert if table is empty
    private void loadAirports() {
        comboSource.removeAllItems();
        comboDestination.removeAllItems();

        try {
            Connection con = DBConnection.getConnection();

            // Pehle check karo koi airport hai ya nahi
            String countSql = "SELECT COUNT(*) FROM airports";
            PreparedStatement cps = con.prepareStatement(countSql);
            ResultSet crs = cps.executeQuery();
            int count = 0;
            if (crs.next()) {
                count = crs.getInt(1);
            }

            // Agar table khaali hai to kuch default airports insert karo
            if (count == 0) {
                String insertSql = "INSERT INTO airports(code, name, city, country) VALUES " +
                        ('(' + "\"DEL\",\"Indira Gandhi Intl\",\"Delhi\",\"India\"" + "),") +
                        ('(' + "\"BOM\",\"Chhatrapati Shivaji Intl\",\"Mumbai\",\"India\"" + "),") +
                        ('(' + "\"BLR\",\"Kempegowda Intl\",\"Bengaluru\",\"India\"" + ')');
                Statement st = con.createStatement();
                st.executeUpdate(insertSql);
            }

            String sql = "SELECT id, code, city FROM airports";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                AirportOption opt = new AirportOption(
                        rs.getInt("id"),
                        rs.getString("code") + " - " + rs.getString("city")
                );
                comboSource.addItem(opt);
                comboDestination.addItem(opt);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading airports: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveFlight() {
        String code = txtFlightCode.getText().trim();
        String depTime = txtDepartureTime.getText().trim();
        String arrTime = txtArrivalTime.getText().trim();
        String priceStr = txtBasePrice.getText().trim();

        AirportOption src = (AirportOption) comboSource.getSelectedItem();
        AirportOption dst = (AirportOption) comboDestination.getSelectedItem();

        if (code.isEmpty() || src == null || dst == null) {
            JOptionPane.showMessageDialog(this,
                    "Flight code, source and destination are required!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (src.id == dst.id) {
            JOptionPane.showMessageDialog(this,
                    "Source and destination cannot be same!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (depTime.isEmpty() || arrTime.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Departure and Arrival time are required!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double price = 0;
        try {
            if (!priceStr.isEmpty()) {
                price = Double.parseDouble(priceStr);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Invalid price!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Time string with AM/PM
        String depFull = depTime + " " + comboDepAmPm.getSelectedItem();
        String arrFull = arrTime + " " + comboArrAmPm.getSelectedItem();

        try {
            Connection con = DBConnection.getConnection();
            String sql = "INSERT INTO flights " +
                    "(flight_code, source_airport_id, destination_airport_id, departure_time, arrival_time, base_price) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, code);
            ps.setInt(2, src.id);
            ps.setInt(3, dst.id);
            ps.setString(4, depFull);
            ps.setString(5, arrFull);
            ps.setDouble(6, price);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Flight added successfully!");
                txtFlightCode.setText("");
                txtDepartureTime.setText("");
                txtArrivalTime.setText("");
                txtBasePrice.setText("");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to add flight.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
