package dataaccess;

import model.Restaurant;
import java.io.*;
import java.util.*;

public class RestaurantDAO {

    private String filePath = "data/restaurant.txt";
    private String favFolder = "data/favorites/";

    public RestaurantDAO() {
        // create favorites folder if not exist
        File folder = new File(favFolder);
        if (!folder.exists()) folder.mkdirs();
    }

    // Load all restaurants
    public List<Restaurant> getAllRestaurants() {
        List<Restaurant> restaurants = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                // id|name|address|phone|email|hours|cuisines(comma)|diningType|priceRange|rating
                String id = parts[0];
                String name = parts[1];
                String address = parts[2];
                String phone = parts[3];
                String email = parts[4];
                String hours = parts[5];
                List<String> cuisines = Arrays.asList(parts[6].split(","));
                String diningType = parts[7];
                String priceRange = parts[8];
                double rating = Double.parseDouble(parts[9]);
                Restaurant r = new Restaurant(id, name, address, phone, email, hours, cuisines, diningType, priceRange, rating, false);
                restaurants.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return restaurants;
    }

    // New method: Load all restaurants AND mark favorites for a username
    public List<Restaurant> getAllRestaurants(String username) {
        List<Restaurant> restaurants = getAllRestaurants(); // load all
        // load user's favorites
        File favFile = new File(favFolder + username + "_favorites.txt");
        if (favFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(favFile))) {
                String line;
                Set<String> favIds = new HashSet<>();
                while ((line = br.readLine()) != null) {
                    favIds.add(line.trim());
                }
                for (Restaurant r : restaurants) {
                    if (favIds.contains(r.getId())) {
                        r.setFavorite(true);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return restaurants;
    }

    // Update favorite status and save to file
    public void updateFavorite(String username, Restaurant restaurant, List<Restaurant> allRestaurants) {
        // Toggle favorite
        restaurant.setFavorite(!restaurant.isFavorite());

        // Load all favorites from file (if exists)
        File favFile = new File(favFolder + username + "_favorites.txt");
        Set<String> favIds = new HashSet<>();
        if (favFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(favFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    favIds.add(line.trim());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Update favorite set
        if (restaurant.isFavorite()) {
            favIds.add(restaurant.getId());
        } else {
            favIds.remove(restaurant.getId());
        }

        // Save back to file
        try (PrintWriter pw = new PrintWriter(new FileWriter(favFile))) {
            for (String id : favIds) {
                pw.println(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Restaurant> getRestaurantsByCuisine(String cuisine, String username) {
        List<Restaurant> list = new ArrayList<>();
        for (Restaurant r : getAllRestaurants(username)) {
            if (r.getCuisines().contains(cuisine)) list.add(r);
        }
        return list;
    }

    // Get a restaurant by its ID
    public Restaurant getRestaurantById(String id) {
        for (Restaurant r : getAllRestaurants()) {
            if (r.getId().equals(id)) return r;
        }
        return null; // not found
    }

}
