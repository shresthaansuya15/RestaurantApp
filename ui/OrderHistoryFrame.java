import dataaccess.OrderDAO;
import dataaccess.FoodItemDAO;
import model.Order;
import model.FoodItem;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class OrderHistoryFrame extends JFrame {
    public OrderHistoryFrame(String username) {
        setTitle("Order History");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(255, 182, 193));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        List<Order> orders = new OrderDAO().getOrdersByUsername(username);
        FoodItemDAO foodDAO = new FoodItemDAO();

        if (orders.isEmpty()) {
            JLabel empty = new JLabel("No past orders!");
            empty.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
            mainPanel.add(empty);
        } else {
            for (Order order : orders) {
                FoodItem item = foodDAO.getAllFoodItems().stream()
                        .filter(f -> f.getId().equals(order.getFoodId()))
                        .findFirst().orElse(null);
                if (item == null) continue;

                JPanel row = new JPanel(new GridLayout(2,1));
                row.setBackground(new Color(255, 240, 245));
                row.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.PINK,1,true),
                        BorderFactory.createEmptyBorder(5,5,5,5)
                ));

                JLabel name = new JLabel(order.getQuantity() + "x " + item.getName());
                name.setFont(new Font("Arial", Font.BOLD, 14));
                JLabel details = new JLabel("Total: $" + order.getTotalPrice() + " | Status: " + order.getStatus());
                details.setFont(new Font("Arial", Font.PLAIN, 13));

                row.add(name);
                row.add(details);
                mainPanel.add(row);
                mainPanel.add(Box.createVerticalStrut(5));
            }
        }

        JScrollPane scroll = new JScrollPane(mainPanel);
        scroll.setBorder(null);
        add(scroll);
        setVisible(true);
    }
}
