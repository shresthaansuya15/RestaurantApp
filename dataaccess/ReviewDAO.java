package dataaccess;

import model.Restaurant;
import java.io.*;
import java.util.*;

public class RestaurantDAO 
{
    private String filePath = "data/restaurant.txt";

    public List<Restaurant> getAllRestaurants() 
    {
        List<Restaurant> restaurants = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) 
        {
            String line = br.readLine(); // skip header

            while ((line = br.readLine()) != null) 
            {
                if (line.trim().isEmpty()) continue; // skip empty lines

                String[] parts = line.split("\\|");

                if (parts.length < 10) continue;

                String id = parts[0];
                String name = parts[1];
                String address = parts[2];
                String phone = parts[3];
                String email = parts[4];
                String hours = parts[5];
                List<String> cuisines = Arrays.asList(parts[6].split(","));
                String diningType = parts[7];
                String priceRange = parts[8];
                double rating = 0.0;
                try 
                {
                    rating = Double.parseDouble(parts[9]);
                } 
                catch (NumberFormatException e) 
                {
                    rating = 0.0; // default if missing or invalid
                }

                restaurants.add(new Restaurant(id, name, address, phone, email, hours, cuisines, diningType, priceRange, rating));
            }
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        return restaurants;
    }

    // New method to filter restaurants by cuisine
    public List<Restaurant> getRestaurantsByCuisine(String cuisine) 
    {   
        List<Restaurant> filtered = new ArrayList<>();
        for (Restaurant r : getAllRestaurants()) 
        {
            for (String c : r.getCuisines()) 
            {
                if (c.trim().equalsIgnoreCase(cuisine)) 
                {
                    filtered.add(r);
                    break;
                }
            }
        }
        return filtered;
    }
}
