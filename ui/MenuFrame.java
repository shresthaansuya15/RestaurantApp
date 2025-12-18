import dataaccess.FoodItemDAO;
import dataaccess.OrderDAO;
import model.FoodItem;
import model.Order;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("unused")
public class MenuFrame extends JFrame {
    private Map<FoodItem, Integer> cart = new HashMap<>();
    private JLabel totalLabel;
    private JLabel itemCountLabel;
    private JPanel cartPanel;

    public MenuFrame(String restaurantId, String restaurantName, String address,
                     String diningType, String hours, String username) {

        setTitle("Menu - " + restaurantName);
        setSize(480, 650);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(255, 182, 193)); // pastel pink
        add(mainPanel);

        // ----- Top Panel: Restaurant Info -----
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Left: restaurant image
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/resources/restaurant_images/" + restaurantId + ".jpg"));
            Image img = icon.getImage().getScaledInstance(150, 120, Image.SCALE_SMOOTH);
            JLabel imgLabel = new JLabel(new ImageIcon(img));
            topPanel.add(imgLabel, BorderLayout.WEST);
        } catch (Exception ex) {
            System.out.println("Image not found for restaurant " + restaurantId);
        }

        // Right: restaurant info
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

        // ----- Menu Panel -----
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(255, 240, 245));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        FoodItemDAO dao = new FoodItemDAO();
        List<FoodItem> menu = dao.getFoodItemsByRestaurant(restaurantId);

        int addButtonWidth = 70; // fixed width for perfect alignment
        for (FoodItem item : menu) {
            JPanel row = new RoundedPanel(15, Color.WHITE);
            row.setMaximumSize(new Dimension(420, 60));
            row.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 10, 5, 10);
            gbc.anchor = GridBagConstraints.WEST;

            // Item Name
            JLabel name = new JLabel(item.getName());
            name.setFont(new Font("Arial", Font.BOLD, 14));
            gbc.gridx = 0;
            gbc.weightx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            row.add(name, gbc);

            // Price
            JLabel price = new JLabel("$" + item.getPrice());
            price.setFont(new Font("Arial", Font.PLAIN, 13));
            gbc.gridx = 1;
            gbc.weightx = 0;
            gbc.fill = GridBagConstraints.NONE;
            row.add(price, gbc);

            // Quantity Selector (starts at 0)
            SpinnerNumberModel model = new SpinnerNumberModel(0, 0, 20, 1);
            JSpinner qtySpinner = new JSpinner(model);
            ((JSpinner.DefaultEditor) qtySpinner.getEditor()).getTextField().setColumns(2);
            gbc.gridx = 2;
            row.add(qtySpinner, gbc);

            // Add Button
            JButton addBtn = new JButton("Add") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    if (getModel().isPressed()) {
                        g2.setColor(new Color(255, 105, 180));
                    } else if (getModel().isRollover()) {
                        g2.setColor(new Color(255, 135, 180));
                    } else {
                        g2.setColor(new Color(255, 182, 193));
                    }
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
                if (qty == 0) {
                    cart.remove(item);
                } else {
                    cart.put(item, qty);
                }
                updateTotal();
                animateToCart(addBtn);
                showConfirmation(item, qty); // Show "1x item added" popup
            });

            gbc.gridx = 3;
            row.add(addBtn, gbc);

            menuPanel.add(row);
            menuPanel.add(Box.createVerticalStrut(5));
        }

        JScrollPane scroll = new JScrollPane(menuPanel);
        scroll.setBorder(null);
        mainPanel.add(scroll, BorderLayout.CENTER);

        // ----- Cart Summary Panel -----
        cartPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        cartPanel.setOpaque(false);

        itemCountLabel = new JLabel("Items: 0");
        itemCountLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 14));

        totalLabel = new JLabel("Total: $0.00");
        totalLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 16));

        // Checkout button
        JButton checkoutBtn = createRoundedButton("Checkout");
        checkoutBtn.addActionListener(e -> checkout(username, restaurantId));

        cartPanel.add(itemCountLabel);
        cartPanel.add(totalLabel);
        cartPanel.add(checkoutBtn);
        mainPanel.add(cartPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // ----- Helper to create pink rounded buttons -----
    private JButton createRoundedButton(String text) {
        JButton btn = new JButton(text) {
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
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ----- Update Cart Total -----
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

    // ----- Checkout -----
    private void checkout(String username, String restaurantId) {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty!");
            return;
        }

        // Show confirmation dialog with list of items
        StringBuilder sb = new StringBuilder();
        double subtotal = 0;
        for (Map.Entry<FoodItem, Integer> entry : cart.entrySet()) {
            sb.append(entry.getValue()).append("x ").append(entry.getKey().getName())
              .append(" - $").append(entry.getKey().getPrice() * entry.getValue()).append("\n");
            subtotal += entry.getKey().getPrice() * entry.getValue();
        }
        sb.append("\nSubtotal: $").append(String.format("%.2f", subtotal));

        int confirm = JOptionPane.showConfirmDialog(this, sb.toString(), "Confirm this order",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
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

    // ----- Flying animation -----
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

    // ----- Popup for "1x item added" -----
    private void showConfirmation(FoodItem item, int qty) {
        if (qty > 0) {
            JOptionPane.showMessageDialog(this,
                    qty + "x " + item.getName() + " added to cart!",
                    "Added", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // ----- Rounded panel for menu items -----
    class RoundedPanel extends JPanel {
        private int radius;
        private Color bgColor;

        public RoundedPanel(int radius, Color bgColor) {
            super();
            this.radius = radius;
            this.bgColor = bgColor;
            setOpaque(false);
        }

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
