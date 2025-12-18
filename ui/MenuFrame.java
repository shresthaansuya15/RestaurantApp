import dataaccess.FoodItemDAO;
import model.FoodItem;
import model.Order;
import dataaccess.OrderDAO;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.List;

// Custom Rounded Button
class RoundedButton extends JButton {
    private int radius;

    public RoundedButton(String text, int radius) {
        super(text);
        this.radius = radius;
        setContentAreaFilled(false);
        setFocusPainted(false);
        setForeground(Color.BLACK);
        setBackground(new Color(255, 182, 193)); // light pink
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
        g2.dispose();
    }
}

public class MenuFrame extends JFrame {
    private Map<FoodItem, Integer> cart = new HashMap<>();
    private JLabel totalLabel;

    public MenuFrame(String restaurantId, String restaurantName, String address,
                     String diningType, String hours, String username) {

        setTitle("Menu - " + restaurantName);
        setSize(480, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(255, 182, 193));

        // Top panel for restaurant info
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setPreferredSize(new Dimension(450, 140));

        // Restaurant Image
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/resources/restaurant_images/" + restaurantId + ".jpg"));
            Image img = icon.getImage().getScaledInstance(150, 120, Image.SCALE_SMOOTH);
            JLabel imgLabel = new JLabel(new ImageIcon(img));
            topPanel.add(imgLabel, BorderLayout.WEST);
        } catch (Exception ex) {
            System.out.println("Image not found for restaurant " + restaurantId);
        }

        // Restaurant Details
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
        menuPanel.setOpaque(false);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        FoodItemDAO dao = new FoodItemDAO();
        List<FoodItem> menu = dao.getFoodItemsByRestaurant(restaurantId);

        for (FoodItem item : menu) {
            JPanel row = new JPanel(new GridBagLayout());
            row.setBackground(Color.WHITE);
            row.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.PINK, 1, true),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(0, 5, 0, 5);
            gbc.anchor = GridBagConstraints.WEST;

            JLabel itemName = new JLabel(item.getName());
            itemName.setFont(new Font("Arial", Font.BOLD, 14));
            gbc.gridx = 0;
            gbc.weightx = 0.5;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            row.add(itemName, gbc);

            JLabel price = new JLabel("$" + item.getPrice());
            price.setFont(new Font("Arial", Font.PLAIN, 13));
            gbc.gridx = 1;
            gbc.weightx = 0;
            row.add(price, gbc);

            SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 20, 1);
            JSpinner qtySpinner = new JSpinner(model);
            gbc.gridx = 2;
            row.add(qtySpinner, gbc);

            RoundedButton addBtn = new RoundedButton("Add", 20);
            addBtn.setPreferredSize(new Dimension(70, 30));
            addBtn.addActionListener(e -> {
                int qty = (Integer) qtySpinner.getValue();
                cart.put(item, qty);
                updateTotal();
            });
            gbc.gridx = 3;
            gbc.anchor = GridBagConstraints.EAST;
            row.add(addBtn, gbc);

            row.setMaximumSize(new Dimension(420, 50));
            menuPanel.add(row);
            menuPanel.add(Box.createVerticalStrut(8));
        }

        JScrollPane scroll = new JScrollPane(menuPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(10);
        mainPanel.add(scroll, BorderLayout.CENTER);

        // Bottom panel for total and checkout
        JPanel bottomPanel = new JPanel(null);
        bottomPanel.setPreferredSize(new Dimension(450, 50));
        bottomPanel.setOpaque(false);

        totalLabel = new JLabel("Total: $0.00");
        totalLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        totalLabel.setBounds(10, 10, 200, 30);
        bottomPanel.add(totalLabel);

        RoundedButton checkoutBtn = new RoundedButton("Checkout", 20);
        checkoutBtn.setBounds(320, 10, 120, 30);
        checkoutBtn.addActionListener(e -> checkout(username, restaurantId));
        bottomPanel.add(checkoutBtn);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void updateTotal() {
        double total = 0;
        for (Map.Entry<FoodItem, Integer> entry : cart.entrySet()) {
            total += entry.getKey().getPrice() * entry.getValue();
        }
        totalLabel.setText(String.format("Total: $%.2f", total));
    }

    private void checkout(String username, String restaurantId) {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty!");
            return;
        }

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
