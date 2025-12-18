import dataaccess.*;
import model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.Timer;

@SuppressWarnings("unused")
public class MainFrame extends JFrame {

    private RestaurantDAO restaurantDAO;
    private ReviewDAO reviewDAO;
    private String username;

    public MainFrame(String username) {
        this.username = username;
        setTitle("Restaurant Advisor");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        restaurantDAO = new RestaurantDAO();
        reviewDAO = new ReviewDAO();

        // Load all restaurants with user favorites
        List<Restaurant> allRestaurants = restaurantDAO.getAllRestaurants(username);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(255, 182, 193));

        JLabel bgLabel = new JLabel(new ImageIcon(getClass().getResource("/resources/backgrounds/main_bg.png")));
        bgLabel.setBounds(0, 0, 800, 600);
        panel.add(bgLabel);

        // === Top Panel ===
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBounds(0, 20, 800, 100);

        JLabel welcomeLabel = new JLabel("Hi, " + username + "! What are you craving today?");
        welcomeLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        welcomeLabel.setForeground(Color.BLACK);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
        topPanel.add(welcomeLabel, BorderLayout.WEST);

        // Top-right buttons
        JPanel topRightPanel = new JPanel();
        topRightPanel.setLayout(new BoxLayout(topRightPanel, BoxLayout.Y_AXIS));
        topRightPanel.setOpaque(false);
        topRightPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        topRightPanel.setPreferredSize(new Dimension(180, 120));

        JButton profileBtn = createTopButton("Profile");
        profileBtn.setMaximumSize(new Dimension(160, 40));
        profileBtn.addActionListener(e -> new ProfileFrame(username));
        topRightPanel.add(profileBtn);
        topRightPanel.add(Box.createVerticalStrut(15));

        JButton orderHistoryBtn = createTopButton("Order History");
        orderHistoryBtn.setMaximumSize(new Dimension(160, 40));
        orderHistoryBtn.addActionListener(e -> new OrderHistoryFrame(username));
        topRightPanel.add(orderHistoryBtn);
        topRightPanel.add(Box.createVerticalStrut(15));

        JButton favBtn = createTopButton("Favorites");
        favBtn.setMaximumSize(new Dimension(160, 40));
        favBtn.addActionListener(e -> showFavorites(username, allRestaurants));
        topRightPanel.add(favBtn);

        topPanel.add(topRightPanel, BorderLayout.EAST);
        bgLabel.add(topPanel);

        // === Cuisine Buttons Panel ===
        JPanel cuisinePanel = new JPanel(null);
        cuisinePanel.setOpaque(false);
        cuisinePanel.setBounds(50, 150, 700, 400);
        bgLabel.add(cuisinePanel);

        String[] cuisines = {"Indian", "Chinese", "Italian", "Mexican", "American", "Japanese", 
                             "Vietnamese", "French", "Mediterranean", "Vegetarian", "Asian"};
        int x = 50, y = 150;

        for (String cuisine : cuisines) {
            ImageIcon icon = null;
            try {
                icon = new ImageIcon(getClass().getResource("/resources/cuisine_icons/" + cuisine.toLowerCase() + ".png"));
                Image img = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                icon = new ImageIcon(img);
            } catch (Exception ex) {
                System.out.println("Image not found for " + cuisine);
            }

            JButton btn = new JButton(cuisine, icon);
            btn.setHorizontalTextPosition(SwingConstants.CENTER);
            btn.setVerticalTextPosition(SwingConstants.BOTTOM);
            btn.setBounds(x, y, 120, 80);
            btn.setBackground(new Color(255, 182, 193, 200));
            btn.setForeground(Color.BLACK);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createLineBorder(Color.PINK, 2));

            final Rectangle originalBounds = btn.getBounds();
            final int targetWidth = (int)(originalBounds.width * 1.2);
            final int targetHeight = (int)(originalBounds.height * 1.2);
            Timer growTimer = new Timer(10, null);
            Timer shrinkTimer = new Timer(10, null);

            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    shrinkTimer.stop();
                    btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    btn.setBackground(new Color(255, 105, 180));
                    btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
                    growTimer.addActionListener(ev -> {
                        Rectangle b = btn.getBounds();
                        if (b.width < targetWidth) {
                            int newWidth = Math.min(b.width + 2, targetWidth);
                            int newHeight = Math.min(b.height + 2, targetHeight);
                            int newX = b.x - 1;
                            int newY = b.y - 1;
                            btn.setBounds(newX, newY, newWidth, newHeight);
                        } else {
                            growTimer.stop();
                        }
                    });
                    growTimer.start();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    growTimer.stop();
                    btn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    btn.setBackground(new Color(255, 182, 193, 200));
                    btn.setBorder(BorderFactory.createLineBorder(Color.PINK, 2));
                    shrinkTimer.addActionListener(ev -> {
                        Rectangle b = btn.getBounds();
                        if (b.width > originalBounds.width) {
                            int newWidth = Math.max(b.width - 2, originalBounds.width);
                            int newHeight = Math.max(b.height - 2, originalBounds.height);
                            int newX = b.x + 1;
                            int newY = b.y + 1;
                            btn.setBounds(newX, newY, newWidth, newHeight);
                        } else {
                            shrinkTimer.stop();
                        }
                    });
                    shrinkTimer.start();
                }
            });

            btn.addActionListener(e -> showRestaurants(cuisine, username, allRestaurants));
            bgLabel.add(btn);
            x += 140;
            if (x > 700) {
                x = 50;
                y += 100;
            }
        }

        add(panel);
        setVisible(true);
    }

    // Helper to create styled top buttons
    private JButton createTopButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(255, 182, 193));
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createLineBorder(Color.PINK.darker(), 2, true));
        btn.setPreferredSize(new Dimension(150, 40));
        btn.setMaximumSize(new Dimension(160, 40));
        return btn;
    }

    // === Show restaurants by cuisine ===
    private void showRestaurants(String cuisine, String username, List<Restaurant> allRestaurants) {
        List<Restaurant> cuisineRestaurants = restaurantDAO.getRestaurantsByCuisine(cuisine, username);
        showRestaurantListFrame(cuisineRestaurants, cuisine + " Restaurants", username, allRestaurants);
    }

    private void showFavorites(String username, List<Restaurant> allRestaurants) {
        List<Restaurant> favorites = new ArrayList<>();
        for (Restaurant r : allRestaurants) {
            if (r.isFavorite()) favorites.add(r);
        }
        showRestaurantListFrame(favorites, "Your Favorites", username, allRestaurants);
    }

    // === Reusable Restaurant List Frame ===
    private void showRestaurantListFrame(List<Restaurant> restaurants, String title, String username, List<Restaurant> allRestaurants) {
        final String[] sortMode = { "NONE" };
        @SuppressWarnings("unchecked")
        final List<Restaurant>[] currentList = new List[]{new ArrayList<>(restaurants)};

        JFrame frame = new JFrame(title);
        frame.setSize(650, 600);
        frame.setLocationRelativeTo(this);

        JLabel bgLabel = new JLabel(new ImageIcon(getClass().getResource("/resources/backgrounds/main_bg.png"))); 
        bgLabel.setLayout(new BorderLayout()); 
        frame.setContentPane(bgLabel); 

        JPanel mainPanel = new JPanel(new BorderLayout()); 
        mainPanel.setOpaque(false);

        // Top bar
        JPanel topBar = new JPanel();
        topBar.setLayout(new BoxLayout(topBar, BoxLayout.X_AXIS));
        topBar.setBackground(new Color(255, 160, 180, 200));
        topBar.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        JButton backBtn = new JButton("← Back");
        backBtn.setBackground(Color.WHITE);
        backBtn.setPreferredSize(new Dimension(100, 30));
        backBtn.addActionListener(e -> frame.dispose());
        topBar.add(backBtn);
        topBar.add(Box.createHorizontalStrut(10));

        JTextField searchField = new JTextField();
        searchField.setMaximumSize(new Dimension(150, 30));
        topBar.add(searchField);
        topBar.add(Box.createHorizontalStrut(10));

        JButton searchBtn = new JButton("Search");
        searchBtn.setBackground(Color.WHITE);
        searchBtn.setPreferredSize(new Dimension(80, 30));
        topBar.add(searchBtn);
        topBar.add(Box.createHorizontalStrut(10));

        JButton sortHighBtn = new JButton("Sort: High Rating");
        sortHighBtn.setBackground(Color.WHITE);
        sortHighBtn.setPreferredSize(new Dimension(130, 30));
        topBar.add(sortHighBtn);
        topBar.add(Box.createHorizontalStrut(10));

        JButton sortLowBtn = new JButton("Sort: Low Rating");
        sortLowBtn.setBackground(Color.WHITE);
        sortLowBtn.setPreferredSize(new Dimension(130, 30));
        topBar.add(sortLowBtn);
        topBar.add(Box.createHorizontalStrut(5));

        JButton sortAlphaBtn = new JButton("Sort: A-Z");
        sortAlphaBtn.setBackground(Color.WHITE);
        sortAlphaBtn.setPreferredSize(new Dimension(100, 30));
        topBar.add(sortAlphaBtn);
        topBar.add(Box.createHorizontalStrut(10));

        JCheckBox favOnlyCheck = new JCheckBox("Favorites Only");
        favOnlyCheck.setBackground(new Color(255, 160, 180, 200));
        topBar.add(favOnlyCheck);

        mainPanel.add(topBar, BorderLayout.NORTH);

        // Restaurant list panel
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(listPanel); 
        scrollPane.getViewport().setOpaque(false); 
        scrollPane.setOpaque(false); 
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Load restaurants dynamically
        Runnable[] loadListRef = new Runnable[1];
        loadListRef[0] = () -> {
            listPanel.removeAll();

            List<Restaurant> filteredList = new ArrayList<>(restaurants);

            if (favOnlyCheck.isSelected()) {
                filteredList.removeIf(r -> !r.isFavorite());
            }

            String keyword = searchField.getText().trim().toLowerCase();
            if (!keyword.isEmpty()) {
                filteredList.removeIf(r -> !r.getName().toLowerCase().contains(keyword));
            }

            // APPLY SORTING
            switch (sortMode[0]) {
                case "HIGH":
                    filteredList.sort((a, b) -> Double.compare(b.getRating(), a.getRating()));
                    break;
                case "LOW":
                    filteredList.sort(Comparator.comparingDouble(Restaurant::getRating));
                    break;
                case "ALPHA":
                    filteredList.sort(Comparator.comparing(Restaurant::getName, String.CASE_INSENSITIVE_ORDER));
                    break;
            }

            currentList[0] = filteredList;

            for (Restaurant r : currentList[0]) {
                JPanel card = new JPanel(new BorderLayout());
                card.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
                card.setBackground(new Color(255, 255, 255, 200));
                card.setMaximumSize(new Dimension(600, 130));

                // Left image
                try {
                    ImageIcon icon = new ImageIcon(getClass().getResource("/resources/restaurant_images/" + r.getId() + ".jpg"));
                    Image img = icon.getImage();
                    int newWidth = 120;
                    int newHeight = (img.getHeight(null) * newWidth) / img.getWidth(null);
                    Image scaledImg = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                    JLabel imgLabel = new JLabel(new ImageIcon(scaledImg));
                    card.add(imgLabel, BorderLayout.WEST);
                } catch (Exception ex) {
                    System.out.println("Image not found for " + r.getName());
                }

                // Info panel
                JPanel infoPanel = new JPanel();
                infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
                infoPanel.setOpaque(false);
                infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

                JLabel name = new JLabel(r.getName());
                name.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
                name.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel ratingLabel = new JLabel("Rating: " + r.getRating() + " ⭐");
                ratingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                String priceText;
                switch (r.getPriceRange()) {
                    case "$": priceText = "Inexpensive"; break;
                    case "$$": priceText = "Moderate"; break;
                    case "$$$": priceText = "Expensive"; break;
                    case "$$$$": priceText = "Luxury"; break;
                    default: priceText = "Not specified";
                }
                JLabel priceLabel = new JLabel("Price: " + priceText);
                priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                JButton detailBtn = new JButton("View Details");
                detailBtn.setBackground(new Color(255, 182, 193));
                detailBtn.addActionListener(ev -> showDetail(r, username));

                JButton menuButton = new JButton("View Menu 🍽");
                menuButton.setBackground(Color.PINK);

                menuButton.addActionListener(e -> {
                    new MenuFrame(
                        r.getId(),
                        r.getName(),
                        r.getAddress(),
                        r.getDiningType(),
                        r.getHours(),
                        username   // logged-in user
                    );
                });

                JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
                buttonRow.setOpaque(false);
                buttonRow.add(detailBtn);
                buttonRow.add(menuButton);
                buttonRow.setAlignmentX(Component.LEFT_ALIGNMENT);

                infoPanel.add(name);
                infoPanel.add(ratingLabel);
                infoPanel.add(priceLabel);
                infoPanel.add(Box.createVerticalStrut(8));
                infoPanel.add(buttonRow);

                // Favorite button on right
                JButton favBtn = new JButton(r.isFavorite() ? "★" : "☆");
                favBtn.setBackground(new Color(255, 182, 193));
                favBtn.setFocusPainted(false);
                favBtn.setPreferredSize(new Dimension(50, 50));
                favBtn.addActionListener(ev -> {
                    restaurantDAO.updateFavorite(username, r, allRestaurants);
                    favBtn.setText(r.isFavorite() ? "★" : "☆");
                    loadListRef[0].run();
                });

                JPanel rightPanel = new JPanel();
                rightPanel.setOpaque(false);
                rightPanel.setLayout(new BorderLayout());
                rightPanel.add(favBtn, BorderLayout.NORTH);

                card.add(infoPanel, BorderLayout.CENTER);
                card.add(rightPanel, BorderLayout.EAST);

                listPanel.add(card);
                listPanel.add(Box.createVerticalStrut(10));
            }

            listPanel.revalidate();
            listPanel.repaint();
        };

        searchBtn.addActionListener(e -> loadListRef[0].run());
        sortHighBtn.addActionListener(e -> { 
            sortMode[0] = "HIGH";
            loadListRef[0].run(); 
        });
        sortLowBtn.addActionListener(e -> { 
            sortMode[0] = "LOW";
            loadListRef[0].run(); 
        });
        sortAlphaBtn.addActionListener(e -> { 
            sortMode[0] = "ALPHA";
            loadListRef[0].run(); 
        });
        favOnlyCheck.addActionListener(e -> loadListRef[0].run());

        loadListRef[0].run();

        bgLabel.add(mainPanel);
        frame.setVisible(true);
    }

    // === Show Detail Page with Reviews ===
    private void showDetail(Restaurant r, String username) {
        JFrame detailFrame = new JFrame(r.getName() + " Details");
        detailFrame.setSize(700, 600);
        detailFrame.setLocationRelativeTo(null);

        JLabel detailBg = new JLabel(new ImageIcon(getClass().getResource("/resources/backgrounds/main_bg.png")));
        detailBg.setLayout(new BorderLayout());
        detailFrame.setContentPane(detailBg);

        JPanel content = new JPanel(new BorderLayout(20, 0));
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Left info
        JPanel infoDetail = new JPanel();
        infoDetail.setLayout(new BoxLayout(infoDetail, BoxLayout.Y_AXIS));
        infoDetail.setOpaque(false);

        JLabel nameDetail = new JLabel(r.getName());
        nameDetail.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
        JLabel addressDetail = new JLabel("Address: " + r.getAddress());
        JLabel ratingDetail = new JLabel("Rating: " + r.getRating() + " ⭐");

        String priceDetail;
        switch (r.getPriceRange()) {
            case "$": priceDetail = "Inexpensive"; break;
            case "$$": priceDetail = "Moderate"; break;
            case "$$$": priceDetail = "Expensive"; break;
            case "$$$$": priceDetail = "Luxury"; break;
            default: priceDetail = "Not specified";
        }

        JLabel descriptionLabel = new JLabel("<html><body style='width: 250px;'>Cuisines: " + String.join(", ", r.getCuisines()) +
                "<br>Dining Type: " + r.getDiningType() +
                "<br>Price Range: " + priceDetail +
                "<br>Hours: " + r.getHours() +
                "<br>Phone: " + r.getPhone() +
                "<br>Email: " + r.getEmail() + "</body></html>");

        infoDetail.add(nameDetail);
        infoDetail.add(Box.createVerticalStrut(10));
        infoDetail.add(addressDetail);
        infoDetail.add(Box.createVerticalStrut(5));
        infoDetail.add(ratingDetail);
        infoDetail.add(Box.createVerticalStrut(5));
        infoDetail.add(descriptionLabel);

        content.add(infoDetail, BorderLayout.WEST);

        // Right image
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/resources/restaurant_images/" + r.getId() + ".jpg"));
            Image img = icon.getImage();
            int newWidth = 250;
            int newHeight = (img.getHeight(null) * newWidth) / img.getWidth(null);
            Image scaledImg = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            JLabel imgLabel = new JLabel(new ImageIcon(scaledImg));
            content.add(imgLabel, BorderLayout.EAST);
        } catch (Exception ex) {
            System.out.println("Image not found for " + r.getName());
        }

        // === Review Panel ===
        JPanel reviewPanel = new JPanel();
        reviewPanel.setLayout(new BoxLayout(reviewPanel, BoxLayout.Y_AXIS));
        reviewPanel.setOpaque(false);
        reviewPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Text area
        JTextArea reviewBox = new JTextArea();
        reviewBox.setLineWrap(true);
        reviewBox.setWrapStyleWord(true);
        reviewBox.setRows(3);
        reviewBox.setBackground(new Color(255, 182, 193));
        reviewBox.setText(username + ", ready to share your thoughts?");
        JScrollPane reviewScroll = new JScrollPane(reviewBox);
        reviewScroll.setPreferredSize(new Dimension(400, 70));
        reviewScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        reviewPanel.add(reviewScroll);
        reviewPanel.add(Box.createVerticalStrut(5));

        // Rating dropdown
        String[] ratings = {"1⭐","2⭐⭐","3⭐⭐⭐","4⭐⭐⭐⭐","5⭐⭐⭐⭐⭐"};
        JComboBox<String> ratingBox = new JComboBox<>(ratings);
        ratingBox.setBackground(new Color(255, 182, 193));
        ratingBox.setSelectedIndex(4);
        reviewPanel.add(ratingBox);
        reviewPanel.add(Box.createVerticalStrut(5));

        // Submit button
        JButton submitBtn = new JButton("Submit Review");
        submitBtn.setBackground(new Color(255, 182, 193));
        reviewPanel.add(submitBtn);
        reviewPanel.add(Box.createVerticalStrut(10));

        // Previous reviews
        java.util.List<Review> reviews = reviewDAO.getReviewsByRestaurant(r.getId());

        if (!reviews.isEmpty()) {
            JLabel historyTitle = new JLabel("Previous Reviews");
            historyTitle.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
            historyTitle.setForeground(new Color(139,0,139));
            historyTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
            reviewPanel.add(historyTitle);
            reviewPanel.add(Box.createVerticalStrut(5));

            JPanel historyPanel = new JPanel();
            historyPanel.setLayout(new BoxLayout(historyPanel, BoxLayout.Y_AXIS));
            historyPanel.setOpaque(false);

            for (Review rev : reviews) {
                JPanel revPanel = new JPanel();
                revPanel.setLayout(new BoxLayout(revPanel, BoxLayout.Y_AXIS));
                revPanel.setBackground(new Color(255, 182, 193)); 
                revPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

                JLabel userLabel = new JLabel(rev.getUsername() + " - " + rev.getRating() + " ⭐");
                userLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 12));

                JTextArea commentLabel = new JTextArea(rev.getComment());
                commentLabel.setLineWrap(true);
                commentLabel.setWrapStyleWord(true);
                commentLabel.setEditable(false);
                commentLabel.setBackground(new Color(255, 240, 245));

                revPanel.add(userLabel);
                revPanel.add(commentLabel);
                revPanel.add(Box.createVerticalStrut(5));

                historyPanel.add(revPanel);
                historyPanel.add(Box.createVerticalStrut(5));
            }

            JScrollPane historyScroll = new JScrollPane(historyPanel);
            historyScroll.setPreferredSize(new Dimension(400, 150));
            historyScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            reviewPanel.add(historyScroll);
        }

        // Submit action
        submitBtn.addActionListener(ev -> {
            String comment = reviewBox.getText().trim();
            int rating = ratingBox.getSelectedIndex() + 1;

            if (comment.isEmpty() || comment.equals(username + ", ready to share your thoughts?")) {
                JOptionPane.showMessageDialog(detailFrame, "Please enter a review.");
                return;
            }

            String reviewId = UUID.randomUUID().toString();
            Review newReview = new Review(reviewId, r.getId(), username, rating, comment);
            reviewDAO.addReview(newReview);
            reviews.add(newReview);

            // Update average rating
            double avg = reviews.stream().mapToInt(Review::getRating).average().orElse(0.0);
            ratingDetail.setText(String.format("Rating: %.2f ⭐", avg));

            // Reset input fields
            reviewBox.setText(username + ", ready to share your thoughts?");
            ratingBox.setSelectedIndex(4);

            // Refresh previous reviews panel
            reviewPanel.removeAll();

            // Re-add input components
            reviewPanel.add(reviewScroll);
            reviewPanel.add(Box.createVerticalStrut(5));
            reviewPanel.add(ratingBox);
            reviewPanel.add(Box.createVerticalStrut(5));
            reviewPanel.add(submitBtn);
            reviewPanel.add(Box.createVerticalStrut(10));

            // Add previous reviews if any
            if (!reviews.isEmpty()) {
                JLabel historyTitle = new JLabel("Previous Reviews");
                historyTitle.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
                historyTitle.setForeground(new Color(139,0,139));
                historyTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
                reviewPanel.add(historyTitle);
                reviewPanel.add(Box.createVerticalStrut(5));

                JPanel historyPanel = new JPanel();
                historyPanel.setLayout(new BoxLayout(historyPanel, BoxLayout.Y_AXIS));
                historyPanel.setOpaque(false);

                for (Review rev : reviews) {
                    JPanel revPanel = new JPanel();
                    revPanel.setLayout(new BoxLayout(revPanel, BoxLayout.Y_AXIS));
                    revPanel.setBackground(new Color(255, 240, 245));
                    revPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

                    JLabel userLabel = new JLabel(rev.getUsername() + " - " + rev.getRating() + " ⭐");
                    userLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 12));

                    JTextArea commentLabel = new JTextArea(rev.getComment());
                    commentLabel.setLineWrap(true);
                    commentLabel.setWrapStyleWord(true);
                    commentLabel.setEditable(false);
                    commentLabel.setBackground(new Color(255, 240, 245));

                    revPanel.add(userLabel);
                    revPanel.add(commentLabel);
                    revPanel.add(Box.createVerticalStrut(5));

                    historyPanel.add(revPanel);
                    historyPanel.add(Box.createVerticalStrut(5));
                }

                JScrollPane historyScroll = new JScrollPane(historyPanel);
                historyScroll.setPreferredSize(new Dimension(400, 150));
                historyScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                reviewPanel.add(historyScroll);
            }

            reviewPanel.revalidate();
            reviewPanel.repaint();

            JOptionPane.showMessageDialog(detailFrame, "Thanks! Your review was saved.");
        });

        content.add(reviewPanel, BorderLayout.SOUTH);

        detailBg.add(content, BorderLayout.CENTER);
        detailFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame("User"));
    }
}


      
