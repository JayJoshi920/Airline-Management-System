package airline;

import javax.swing.*;
import java.awt.*;

public class MainDashboard extends JFrame {

    private JButton btnAddCustomer;
    private JButton btnAddFlight;
    private JButton btnBookFlight;
    private JButton btnViewBookings;
    private JButton btnPayment;
    private JButton btnCancelBooking;
    private JButton btnExit;

    public MainDashboard() {
        setTitle("Airline Management - Dashboard");
        setSize(700, 420);                         // thoda bada
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        initUI();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.PRIMARY_BG);

        // ---------- HEADER ----------
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UITheme.PRIMARY_BG);
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 5, 20));

        JLabel lblTitle = new JLabel("Dashboard");
        lblTitle.setFont(UITheme.TITLE_FONT);
        lblTitle.setForeground(UITheme.TEXT_LIGHT);

        JLabel lblSub = new JLabel("Manage customers, flights, bookings, payments and cancellations");
        lblSub.setFont(UITheme.SUBTITLE_FONT);
        lblSub.setForeground(UITheme.TEXT_MUTED);

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);
        titlePanel.add(lblTitle);
        titlePanel.add(lblSub);

        header.add(titlePanel, BorderLayout.WEST);

        root.add(header, BorderLayout.NORTH);

        // ---------- CENTER BUTTON GRID ----------
        JPanel center = new JPanel(new GridLayout(2, 3, 15, 15));
        center.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        center.setBackground(UITheme.PRIMARY_BG);

        btnAddCustomer   = createMenuButton("Add Customer");
        btnAddFlight     = createMenuButton("Add Flight");
        btnBookFlight    = createMenuButton("Book Flight");
        btnViewBookings  = createMenuButton("View Bookings");
        btnPayment       = createMenuButton("Payment");
        btnCancelBooking = createMenuButton("Cancel Booking");

        center.add(btnAddCustomer);
        center.add(btnAddFlight);
        center.add(btnBookFlight);
        center.add(btnViewBookings);
        center.add(btnPayment);
        center.add(btnCancelBooking);

        // ---------- FOOTER (EXIT) ----------
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        footer.setBackground(UITheme.PRIMARY_BG);

        btnExit = new JButton("Exit");
        styleExitButton(btnExit);
        footer.add(btnExit);

        root.add(center, BorderLayout.CENTER);
        root.add(footer, BorderLayout.SOUTH);

        setContentPane(root);

        // ---------- ACTIONS ----------
        btnAddCustomer.addActionListener(e -> new AddCustomerFrame().setVisible(true));
        btnAddFlight.addActionListener(e -> new AddFlightFrame().setVisible(true));
        btnBookFlight.addActionListener(e -> new BookFlightFrame().setVisible(true));
        btnViewBookings.addActionListener(e -> new ViewBookingsFrame().setVisible(true));
        btnPayment.addActionListener(e -> new PaymentFrame().setVisible(true));
        btnCancelBooking.addActionListener(e -> new CancelBookingFrame().setVisible(true));
        btnExit.addActionListener(e -> System.exit(0));
    }

    // Card-style menu buttons – dark bg + white text + visible border
    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(UITheme.BUTTON_FONT);
        btn.setForeground(UITheme.TEXT_LIGHT);
        btn.setBackground(UITheme.CARD_BG);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(UITheme.ACCENT, 2));
        btn.setContentAreaFilled(false);  // LAF ko ignore karo
        btn.setOpaque(true);              // hamara color use karo
        btn.setPreferredSize(new Dimension(180, 80));
        return btn;
    }

    // Exit button thoda highlighted
    private void styleExitButton(JButton btn) {
        btn.setFont(UITheme.BUTTON_FONT);
        btn.setBackground(UITheme.ACCENT);
        btn.setForeground(UITheme.PRIMARY_BG);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(6, 20, 6, 20));
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
    }
}
