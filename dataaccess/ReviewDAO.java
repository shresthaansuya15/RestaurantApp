package dataaccess;

import model.Review;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {

    private String filePath = "data/reviews.txt";

    // Get all reviews for a specific restaurant
    public List<Review> getReviewsByRestaurant(String restaurantId) {
        List<Review> reviews = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) return reviews; // file not found yet

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split("\\|", 5);
                if (parts.length < 5) continue;

                if (parts[1].equals(restaurantId)) {
                    reviews.add(new Review(
                            parts[0],
                            parts[1],
                            parts[2],
                            Integer.parseInt(parts[3]),
                            parts[4]
                    ));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    // Add a new review
    public void addReview(Review review) {
        File file = new File(filePath);
        boolean writeHeader = !file.exists();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            if (writeHeader) {
                bw.write("ReviewID|RestaurantID|Username|Rating|Comment");
                bw.newLine();
            }
            bw.write(
                    review.getReviewId() + "|" +
                    review.getRestaurantId() + "|" +
                    review.getUsername() + "|" +
                    review.getRating() + "|" +
                    review.getComment()
            );
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
