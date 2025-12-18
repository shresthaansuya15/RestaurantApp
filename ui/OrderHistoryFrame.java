import dataaccess.ReviewDAO;
import dataaccess.RestaurantDAO;
import model.Review;
import model.Restaurant;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OrderHistoryFrame extends JFrame {

    public OrderHistoryFrame(String username) {

        setTitle("My Order History");
        setSize(650, 450); // slightly bigger for spacing
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // ===== Background panel =====
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(255, 240, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ===== Title panel =====
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(255, 182, 193));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Emoji on the left
        JLabel emojiLabel = new JLabel("🍽️");
        emojiLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        emojiLabel.setHorizontalAlignment(SwingConstants.LEFT);

        titlePanel.add(emojiLabel, BorderLayout.WEST);

        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // ===== Table =====
        String[] columns = {"Restaurant", "Rating", "Review"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        table.setRowHeight(28);
        table.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(255, 182, 193));

        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ===== Load data =====
        ReviewDAO reviewDAO = new ReviewDAO();
        RestaurantDAO restaurantDAO = new RestaurantDAO();

        List<Review> reviews = reviewDAO.getReviewsByUser(username);
        for (Review r : reviews) {
            Restaurant rest = restaurantDAO.getRestaurantById(r.getRestaurantId());
            String restaurantName = (rest != null) ? rest.getName() : "Unknown";

            model.addRow(new Object[]{
                    restaurantName,
                    r.getRating(),
                    r.getComment()
            });
        }

        add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new OrderHistoryFrame("User"));
    }
}
