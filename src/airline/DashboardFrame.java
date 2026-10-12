package airline;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DashboardFrame extends JFrame {

    public DashboardFrame(String username) {
        setTitle("Airline Management - Dashboard");
        setSize(900, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        initUI(username);
    }

    private void initUI(String username) {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.PRIMARY_BG);

        // ---------- TOP BAR ----------
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(UITheme.CARD_BG);
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));

        JLabel appTitle = new JLabel("Airline Management System");
        appTitle.setFont(UITheme.TITLE_FONT);
        appTitle.setForeground(UITheme.TEXT_LIGHT);

        JLabel userLabel = new JLabel("Logged in as: " + username);
        userLabel.setFont(UITheme.SUBTITLE_FONT);
        userLabel.setForeground(UITheme.TEXT_MUTED);

        topBar.add(appTitle, BorderLayout.WEST);
        topBar.add(userLabel, BorderLayout.EAST);

        // ---------- CENTER GRID ----------
        JPanel center = new JPanel(new GridLayout(2, 3, 18, 18));
        center.setBorder(BorderFactory.createEmptyBorder(20, 18, 20, 18));
        center.setBackground(UITheme.PRIMARY_BG);

        center.add(createCard("Add Customer", "Create new passenger profile", () -> {
            new AddCustomerFrame().setVisible(true);
        }));

        center.add(createCard("Add Flight", "Add new flight route & timing", () -> {
            new AddFlightFrame().setVisible(true);
        }));

        center.add(createCard("Book Flight", "Book ticket for a customer", () -> {
            new BookFlightFrame().setVisible(true);
        }));

        center.add(createCard("View Bookings", "See all flight bookings", () -> {
            new ViewBookingsFrame().setVisible(true);
        }));

        center.add(createCard("Payments", "Record ticket payments", () -> {
            new PaymentFrame().setVisible(true);
        }));

        center.add(createCard("Cancel Booking", "Cancel an existing ticket", () -> {
            new CancelBookingFrame().setVisible(true);
        }));

        // ---------- ADD TO ROOT ----------
        root.add(topBar, BorderLayout.NORTH);
        root.add(center, BorderLayout.CENTER);

        setContentPane(root);
    }

    // ---------- CARD CREATOR ----------

    private JPanel createCard(String title, String subtitle, Runnable onClick) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(UITheme.CARD_BG);
        card.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(UITheme.TEXT_LIGHT);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel lblSub = new JLabel(subtitle);
        lblSub.setForeground(UITheme.TEXT_MUTED);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 4));
        textPanel.setOpaque(false);
        textPanel.add(lblTitle);
        textPanel.add(lblSub);

        card.add(textPanel, BorderLayout.CENTER);

        // small right arrow icon-like
        JLabel arrow = new JLabel("➜");
        arrow.setForeground(UITheme.ACCENT);
        arrow.setFont(new Font("Segoe UI", Font.BOLD, 20));
        card.add(arrow, BorderLayout.EAST);

        // Hover effect + click
        Color normalBg = UITheme.CARD_BG;
        Color hoverBg = new Color(30, 41, 59);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(hoverBg);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(normalBg);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                onClick.run();
            }
        });

        return card;
    }

    // For testing directly
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // yahan pe "Admin" ki jagah login se actual username pass kar sakte ho
            new DashboardFrame("Admin").setVisible(true);
        });
    }
}
