import javax.swing.*;
import java.awt.*;
import java.util.List;
import dataaccess.OrderDAO;
import dataaccess.ReviewDAO;
import model.Order;
import model.Review;
import dataaccess.FoodItemDAO;
import model.FoodItem;

@SuppressWarnings("unused")
public class ProfileFrame extends JFrame {

    public ProfileFrame(String username) {
        setTitle(username + "'s Profile");
        setSize(450, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new RoundedPanel(20, new Color(255, 182, 193));
        mainPanel.setLayout(new BorderLayout(10, 10));
        add(mainPanel);

        JLabel title = new JLabel("Profile - " + username, SwingConstants.CENTER);
        title.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        mainPanel.add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(255, 240, 245));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(new JScrollPane(centerPanel), BorderLayout.CENTER);

        // Total Orders
        OrderDAO orderDAO = new OrderDAO();
        List<Order> orders = orderDAO.getOrdersByUsername(username);
        JLabel ordersLabel = new JLabel("Total Orders: " + orders.size());
        ordersLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        centerPanel.add(ordersLabel);
        centerPanel.add(Box.createVerticalStrut(10));

        // User Reviews
        ReviewDAO reviewDAO = new ReviewDAO();
        List<Review> reviews = reviewDAO.getReviewsByRestaurant(""); // optional: show all reviews by user filtered by username
        JLabel reviewLabel = new JLabel("Your Reviews:");
        reviewLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        centerPanel.add(reviewLabel);
        centerPanel.add(Box.createVerticalStrut(5));

        for (Review r : reviews) {
            if (!r.getUsername().equals(username)) continue;
            JPanel reviewPanel = new RoundedPanel(15, Color.WHITE);
            reviewPanel.setLayout(new BoxLayout(reviewPanel, BoxLayout.Y_AXIS));
            reviewPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

            JLabel restaurant = new JLabel("Restaurant: " + r.getRestaurantId());
            JLabel rating = new JLabel("Rating: " + r.getRating());
            JLabel comment = new JLabel("Comment: " + r.getComment());

            reviewPanel.add(restaurant);
            reviewPanel.add(rating);
            reviewPanel.add(comment);
            reviewPanel.add(Box.createVerticalStrut(5));

            centerPanel.add(reviewPanel);
            centerPanel.add(Box.createVerticalStrut(5));
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
