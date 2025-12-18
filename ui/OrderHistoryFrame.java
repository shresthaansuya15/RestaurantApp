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
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 🌸 Background panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(255, 240, 245)); // light pink
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 🌸 Title panel (emoji + text)
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        titlePanel.setBackground(new Color(255, 182, 193));

        JLabel emojiLabel = new JLabel("🍽️");
        emojiLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));

        JLabel textLabel = new JLabel("My Order History");
        textLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
        textLabel.setForeground(Color.BLACK);

        titlePanel.add(emojiLabel);
        titlePanel.add(textLabel);

        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // 🌸 Table
        String[] columns = {"Restaurant", "Rating", "Review"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        table.setRowHeight(28);
        table.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(255, 182, 193));

        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // 🌸 Load data
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
