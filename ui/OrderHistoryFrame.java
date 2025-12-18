import javax.swing.*;
import java.awt.*;
import java.util.List;
import dataaccess.OrderDAO;
import dataaccess.FoodItemDAO;
import model.Order;
import model.FoodItem;

public class OrderHistoryFrame extends JFrame {

    public OrderHistoryFrame(String username) {
        setTitle(username + "'s Orders");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new RoundedPanel(20, new Color(255, 182, 193));
        mainPanel.setLayout(new BorderLayout(10, 10));
        add(mainPanel);

        JLabel title = new JLabel("Order History", SwingConstants.CENTER);
        title.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        mainPanel.add(title, BorderLayout.NORTH);

        JPanel ordersPanel = new JPanel();
        ordersPanel.setLayout(new BoxLayout(ordersPanel, BoxLayout.Y_AXIS));
        ordersPanel.setBackground(new Color(255, 240, 245));
        ordersPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        mainPanel.add(new JScrollPane(ordersPanel), BorderLayout.CENTER);

        OrderDAO orderDAO = new OrderDAO();
        FoodItemDAO foodDAO = new FoodItemDAO();
        List<Order> orders = orderDAO.getOrdersByUsername(username);

        for (Order o : orders) {
            JPanel orderPanel = new RoundedPanel(15, Color.WHITE);
            orderPanel.setLayout(new BoxLayout(orderPanel, BoxLayout.Y_AXIS));
            orderPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

            JLabel restaurant = new JLabel("Restaurant: " + o.getRestaurantId());
            JLabel total = new JLabel("Total: $" + o.getTotalPrice());

            // Display food item names
            StringBuilder itemsText = new StringBuilder("Items: ");
            FoodItem food = foodDAO.getAllFoodItems().stream()
                    .filter(f -> f.getId().equals(o.getFoodId()))
                    .findFirst().orElse(null);
            if (food != null) {
                itemsText.append(food.getName()).append(" x").append(o.getQuantity());
            }

            JLabel itemsLabel = new JLabel(itemsText.toString());

            JButton viewMenu = new JButton("View Menu");
            viewMenu.setBackground(new Color(255, 182, 193));
            viewMenu.setForeground(Color.BLACK);
            viewMenu.setFocusPainted(false);
            viewMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
            viewMenu.addActionListener(e -> new MenuFrame(o.getRestaurantId(), "Restaurant Menu", "", "", "", username));

            orderPanel.add(restaurant);
            orderPanel.add(itemsLabel);
            orderPanel.add(total);
            orderPanel.add(viewMenu);
            orderPanel.add(Box.createVerticalStrut(5));

            ordersPanel.add(orderPanel);
            ordersPanel.add(Box.createVerticalStrut(10));
        }

        setVisible(true);
    }

    class RoundedPanel extends JPanel {
        private int radius;
        private Color bgColor;
        public RoundedPanel(int radius, Color bgColor) {
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
