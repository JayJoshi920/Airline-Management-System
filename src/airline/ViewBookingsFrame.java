package airline;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ViewBookingsFrame extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;

    public ViewBookingsFrame() {
        setTitle("View Bookings");
        setSize(950, 430);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        initUI();
        loadBookings();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.PRIMARY_BG);

        // -------- HEADER ----------
        JLabel headerLbl = new JLabel("All Bookings");
        headerLbl.setFont(UITheme.TITLE_FONT);
        headerLbl.setForeground(UITheme.TEXT_LIGHT);

        JLabel subLbl = new JLabel("Customer, flight, date, status and seat details");
        subLbl.setFont(UITheme.SUBTITLE_FONT);
        subLbl.setForeground(UITheme.TEXT_MUTED);

        JPanel headerText = new JPanel(new GridLayout(2, 1));
        headerText.setOpaque(false);
        headerText.add(headerLbl);
        headerText.add(subLbl);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UITheme.PRIMARY_BG);
        header.setBorder(BorderFactory.createEmptyBorder(10, 15, 5, 15));
        header.add(headerText, BorderLayout.WEST);

        // -------- TABLE ----------
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{
                "PNR", "Customer", "Flight", "Source", "Destination",
                "Booking Date", "Status", "Seat"
        });

        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(24);

        table.setBackground(UITheme.PRIMARY_BG);
        table.setForeground(UITheme.TEXT_LIGHT);
        table.setGridColor(new Color(30, 41, 59));
        table.setSelectionBackground(UITheme.ACCENT);   // yahi use kar rahe hain
        table.setSelectionForeground(UITheme.TEXT_LIGHT);

        table.setForeground(Color.WHITE);
        table.setBackground(new Color(15, 23, 42));
        table.setSelectionForeground(Color.WHITE);
        table.setSelectionBackground(new Color(30, 64, 175));

        // center some columns
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(center); // PNR
        table.getColumnModel().getColumn(3).setCellRenderer(center); // Source
        table.getColumnModel().getColumn(4).setCellRenderer(center); // Destination
        table.getColumnModel().getColumn(6).setCellRenderer(center); // Status
        table.getColumnModel().getColumn(7).setCellRenderer(center); // Seat

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(UITheme.PRIMARY_BG);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 15, 15));

        // -------- LEFT IMAGE PANEL ----------
        JPanel imagePanel = new BookingIllustrationPanel();
        imagePanel.setPreferredSize(new Dimension(230, 0));

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(UITheme.PRIMARY_BG);
        centerPanel.add(imagePanel, BorderLayout.WEST);
        centerPanel.add(scroll, BorderLayout.CENTER);

        root.add(header, BorderLayout.NORTH);
        root.add(centerPanel, BorderLayout.CENTER);

        setContentPane(root);
    }

    // ✈ Ticket + plane illustration
    private static class BookingIllustrationPanel extends JPanel {
        BookingIllustrationPanel() {
            setBackground(UITheme.PRIMARY_BG);
            setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 5));
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

            int cardW = w - 20;
            int cardH = h - 40;
            if (cardH < 160) cardH = 160;
            int cardX = 5;
            int cardY = 20;

            g2.setColor(UITheme.CARD_BG);
            g2.fillRoundRect(cardX, cardY, cardW, cardH, 24, 24);

            // perforation
            g2.setColor(new Color(51, 65, 85));
            float[] dash = {4f, 4f};
            g2.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER, 10f, dash, 0f));
            int midX = cardX + cardW / 2;
            g2.drawLine(midX, cardY + 15, midX, cardY + cardH - 15);

            // plane on left
            int planeCenterX = cardX + cardW / 4;
            int planeCenterY = cardY + cardH / 2;
            drawPlane(g2, planeCenterX, planeCenterY);

            // small info lines on right
            g2.setStroke(new BasicStroke(1f));
            int infoX = midX + 15;
            int infoY = cardY + 35;

            g2.setColor(UITheme.TEXT_MUTED);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            g2.drawString("PNR • Name • Flight", infoX, infoY);
            g2.drawString("Status • Seat • Date", infoX, infoY + 16);
        }

        private void drawPlane(Graphics2D g2, int cx, int cy) {
            int bodyLen = 70;
            int bodyH = 16;

            // shadow line
            g2.setColor(new Color(51, 65, 85));
            g2.setStroke(new BasicStroke(2f));
            g2.drawLine(cx - bodyLen / 2 - 15, cy + 18,
                        cx + bodyLen / 2 + 15, cy + 18);

            // body
            g2.setColor(UITheme.ACCENT);
            g2.fillRoundRect(cx - bodyLen / 2, cy - bodyH / 2, bodyLen, bodyH, 12, 12);

            // nose
            int noseSize = 14;
            g2.fillPolygon(
                    new int[]{cx + bodyLen / 2, cx + bodyLen / 2 + noseSize, cx + bodyLen / 2},
                    new int[]{cy - bodyH / 2, cy, cy + bodyH / 2},
                    3
            );

            // tail
            int tailSize = 12;
            g2.fillPolygon(
                    new int[]{cx - bodyLen / 2, cx - bodyLen / 2 - tailSize, cx - bodyLen / 2},
                    new int[]{cy - bodyH / 2, cy - bodyH / 2 - tailSize, cy},
                    3
            );

            // wings
            g2.fillPolygon(
                    new int[]{cx - 10, cx + 10, cx + 22, cx - 22},
                    new int[]{cy, cy, cy - 20, cy - 20},
                    4
            );
        }
    }

    // --------- DATA LOAD ---------

    private void loadBookings() {
        tableModel.setRowCount(0);

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
                    "JOIN airports a2 ON f.destination_airport_id = a2.id";

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getString("pnr"),
                        rs.getString("customer"),
                        rs.getString("flight_code"),
                        rs.getString("src"),
                        rs.getString("dst"),
                        rs.getString("booking_date"),
                        rs.getString("status"),
                        rs.getString("seat_no")
                });
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading bookings: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
