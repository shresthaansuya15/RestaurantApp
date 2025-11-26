package dataaccess;

import model.Review;
import java.io.*;
import java.util.*;

public class ReviewDAO 
{
    private String filePath = "data/reviews.txt";

    public List<Review> getAllReviews() 
    {
        List<Review> reviews = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) 
        {
            String line = br.readLine(); // skip header

            while ((line = br.readLine()) != null) 
            {
                if (line.trim().isEmpty()) continue; // skip empty lines

                String[] parts = line.split("\\|");

                if (parts.length < 5) continue;

                reviews.add(new Review(parts[0], parts[1], parts[2],
                                       Integer.parseInt(parts[3]), parts[4]));
            }
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        return reviews;
    }

    public List<Review> getReviewsByRestaurant(String restaurantId) 
    {
        List<Review> all = getAllReviews();
        List<Review> filtered = new ArrayList<>();
        for (Review r : all) 
        {
            if (r.getRestaurantId().equals(restaurantId)) filtered.add(r);
        }
        return filtered;
    }
}
