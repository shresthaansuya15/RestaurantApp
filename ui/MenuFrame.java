import dataaccess.FoodItemDAO;
import dataaccess.OrderDAO;
import dataaccess.CartDAO;
import model.FoodItem;
import model.Order;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class MenuFrame extends JFrame {
    private Map<FoodItem, Integer> cart = new HashMap<>();
    private JLabel totalLabel;
    private JLabel itemCountLabel;
    private JPanel cartPanel;
    private CartDAO cartDAO = new CartDAO();
    @SuppressWarnings("unused")
    private String username;
    @SuppressWarnings("unused")
    private String restaurantId;

    public MenuFrame(String restaurantId, String restaurantName, String address,
                     String diningType, String hours, String username) {

        this.username = username;
        this.restaurantId = restaurantId;

        setTitle("Menu - " + restaurantName);
        setSize(480, 700);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(255, 182, 193)); // pastel pink
        add(mainPanel);

        // Top panel for restaurant info
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Restaurant image
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/resources/restaurant_images/" + restaurantId + ".jpg"));
            Image img = icon.getImage().getScaledInstance(150, 120, Image.SCALE_SMOOTH);
            JLabel imgLabel = new JLabel(new ImageIcon(img));
            topPanel.add(imgLabel, BorderLayout.WEST);
        } catch (Exception ex) {
            System.out.println("Image not found for restaurant " + restaurantId);
        }

        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        JLabel nameLabel = new JLabel(restaurantName);
        nameLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
        JLabel addressLabel = new JLabel("Address: " + address);
        JLabel diningLabel = new JLabel("Dining: " + diningType);
        JLabel hoursLabel = new JLabel("Hours: " + hours);

        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(addressLabel);
        infoPanel.add(diningLabel);
        infoPanel.add(hoursLabel);

        topPanel.add(infoPanel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Menu Panel
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(255, 240, 245));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        FoodItemDAO foodDAO = new FoodItemDAO();
        List<FoodItem> menu = foodDAO.getFoodItemsByRestaurant(restaurantId);

        int addButtonWidth = 70; // fixed width
        for (FoodItem item : menu) {
            JPanel row = new RoundedPanel(15, Color.WHITE);
            row.setMaximumSize(new Dimension(420, 60));
            row.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 10, 5, 10);
            gbc.anchor = GridBagConstraints.WEST;

            JLabel name = new JLabel(item.getName());
            name.setFont(new Font("Arial", Font.BOLD, 14));
            gbc.gridx = 0;
            gbc.weightx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            row.add(name, gbc);

            JLabel price = new JLabel("$" + item.getPrice());
            price.setFont(new Font("Arial", Font.PLAIN, 13));
            gbc.gridx = 1;
            gbc.weightx = 0;
            gbc.fill = GridBagConstraints.NONE;
            row.add(price, gbc);

            // Quantity selector (starts at 0)
            SpinnerNumberModel model = new SpinnerNumberModel(0, 0, 20, 1);
            JSpinner qtySpinner = new JSpinner(model);
            ((JSpinner.DefaultEditor) qtySpinner.getEditor()).getTextField().setColumns(2);
            gbc.gridx = 2;
            row.add(qtySpinner, gbc);

            // Add button
            JButton addBtn = new JButton("Add") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    if (getModel().isPressed()) g2.setColor(new Color(255, 105, 180));
                    else if (getModel().isRollover()) g2.setColor(new Color(255, 135, 180));
                    else g2.setColor(new Color(255, 182, 193));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                    super.paintComponent(g);
                }
                @Override
                public void setBorder(Border border) {}
            };
            addBtn.setForeground(Color.BLACK);
            addBtn.setFocusPainted(false);
            addBtn.setContentAreaFilled(false);
            addBtn.setOpaque(false);
            addBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            addBtn.setPreferredSize(new Dimension(addButtonWidth, 30));

            addBtn.addActionListener(e -> {
                int qty = (Integer) qtySpinner.getValue();
                if (qty == 0) cart.remove(item);
                else cart.put(item, qty);
                updateTotal();

                // Popup confirmation
                if (qty > 0) JOptionPane.showMessageDialog(this,
                        qty + "x " + item.getName() + " added!", "Confirm Order", JOptionPane.INFORMATION_MESSAGE);

                animateToCart(addBtn);
            });

            gbc.gridx = 3;
            row.add(addBtn, gbc);

            menuPanel.add(row);
            menuPanel.add(Box.createVerticalStrut(5));
        }

        JScrollPane scroll = new JScrollPane(menuPanel);
        scroll.setBorder(null);
        mainPanel.add(scroll, BorderLayout.CENTER);

        // Cart summary
        cartPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 5));
        cartPanel.setOpaque(false);

        itemCountLabel = new JLabel("Items: 0");
        itemCountLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 14));

        totalLabel = new JLabel("Total: $0.00");
        totalLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 16));

        JButton historyBtn = new JButton("Order History");
        historyBtn.setForeground(Color.BLACK);
        historyBtn.setBackground(new Color(255, 182, 193));
        historyBtn.setFocusPainted(false);
        historyBtn.addActionListener(e -> new OrderHistoryFrame(username));

        JButton checkoutBtn = new JButton("Checkout") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 182, 193));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g);
            }
            @Override
            public void setBorder(Border border) {}
        };
        checkoutBtn.setForeground(Color.BLACK);
        checkoutBtn.setFocusPainted(false);
        checkoutBtn.setContentAreaFilled(false);
        checkoutBtn.setOpaque(false);
        checkoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        checkoutBtn.addActionListener(e -> checkout(username, restaurantId));

        cartPanel.add(itemCountLabel);
        cartPanel.add(totalLabel);
        cartPanel.add(historyBtn);
        cartPanel.add(checkoutBtn);
        mainPanel.add(cartPanel, BorderLayout.SOUTH);

        // Load saved cart
        loadSavedCart();

        // Save cart on close
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cartDAO.saveCart(cart);
                dispose();
            }
        });

        setVisible(true);
    }

    private void loadSavedCart() {
        Map<String, Integer> savedCart = cartDAO.loadCart();
        FoodItemDAO dao = new FoodItemDAO();
        for (String id : savedCart.keySet()) {
            FoodItem item = dao.getAllFoodItems().stream().filter(f -> f.getId().equals(id)).findFirst().orElse(null);
            if (item != null) cart.put(item, savedCart.get(id));
        }
        updateTotal();
    }

    private void updateTotal() {
        double total = 0;
        int count = 0;
        for (Map.Entry<FoodItem, Integer> entry : cart.entrySet()) {
            total += entry.getKey().getPrice() * entry.getValue();
            count += entry.getValue();
        }
        totalLabel.setText(String.format("Total: $%.2f", total));
        itemCountLabel.setText("Items: " + count);
    }

    private void checkout(String username, String restaurantId) {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty!");
            return;
        }

        // Build a message panel to show items and subtotal
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        double subtotal = 0;
        for (Map.Entry<FoodItem, Integer> entry : cart.entrySet()) {
            String line = entry.getValue() + "x " + entry.getKey().getName() + " - $" + 
                        String.format("%.2f", entry.getKey().getPrice() * entry.getValue());
            subtotal += entry.getKey().getPrice() * entry.getValue();
            JLabel label = new JLabel(line);
            label.setFont(new Font("Arial", Font.PLAIN, 14));
            panel.add(label);
        }

        JLabel totalLabelConfirm = new JLabel("Subtotal: $" + String.format("%.2f", subtotal));
        totalLabelConfirm.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        totalLabelConfirm.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        panel.add(totalLabelConfirm);

        int result = JOptionPane.showConfirmDialog(this, panel, 
                        "Confirm Your Order", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            OrderDAO orderDAO = new OrderDAO();
            for (Map.Entry<FoodItem, Integer> entry : cart.entrySet()) {
                Order order = new Order(UUID.randomUUID().toString(),
                        username,
                        restaurantId,
                        entry.getKey().getId(),
                        entry.getValue(),
                        entry.getKey().getPrice() * entry.getValue(),
                        "Pending");
                orderDAO.addOrder(order);
            }

            JOptionPane.showMessageDialog(this, "Order placed successfully!");
            cart.clear();
            updateTotal();
        }
    }

    private void animateToCart(JButton button) {
        JLayeredPane layeredPane = getLayeredPane();
        Point start = SwingUtilities.convertPoint(button.getParent(), button.getLocation(), layeredPane);
        Point end = SwingUtilities.convertPoint(cartPanel, new Point(cartPanel.getWidth() - 60, 10), layeredPane);

        JLabel flying = new JLabel(button.getText());
        flying.setOpaque(true);
        flying.setBackground(new Color(255, 182, 193));
        flying.setForeground(Color.BLACK);
        flying.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        flying.setBounds(start.x, start.y, button.getWidth(), button.getHeight());
        layeredPane.add(flying, JLayeredPane.POPUP_LAYER);

        new Thread(() -> {
            int steps = 30;
            for (int i = 1; i <= steps; i++) {
                int x = start.x + (end.x - start.x) * i / steps;
                int y = start.y + (end.y - start.y) * i / steps;
                SwingUtilities.invokeLater(() -> flying.setLocation(x, y));
                try { Thread.sleep(15); } catch (InterruptedException ignored) {}
            }
            SwingUtilities.invokeLater(() -> layeredPane.remove(flying));
            layeredPane.repaint();
        }).start();
    }

    class RoundedPanel extends JPanel {
        private int radius;
        private Color bgColor;
        public RoundedPanel(int radius, Color bgColor) { super(); this.radius = radius; this.bgColor = bgColor; setOpaque(false);}
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            super.paintComponent(g);
        }
    }
}
