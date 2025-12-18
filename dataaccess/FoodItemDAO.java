package dataaccess;

import model.FoodItem;
import java.io.*;
import java.util.*;

public class FoodItemDAO 
{
    private String filePath = "data/fooditems.txt";

    public List<FoodItem> getAllFoodItems() 
    {
        List<FoodItem> items = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) 
        {
            String line = br.readLine(); // skip header

            while ((line = br.readLine()) != null) 
            {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split("\\|");
                if (parts.length < 4) continue;

                String id = parts[0];
                String restaurantId = parts[1];
                String name = parts[2];
                double price = Double.parseDouble(parts[3]);

                items.add(new FoodItem(id, restaurantId, name, price));
            }
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }      
        return items;
    }

    public List<FoodItem> getFoodItemsByRestaurant(String restaurantId) 
    {
        List<FoodItem> filtered = new ArrayList<>();

        for (FoodItem item : getAllFoodItems()) 
        {
            if (item.getRestaurantId().equals(restaurantId)) 
            {
                filtered.add(item);
            }
        }
        return filtered;
    }

    // NEW: Get a single food item by its ID
    public FoodItem getFoodItemById(String id) 
    {
        for (FoodItem item : getAllFoodItems()) 
        {
            if (item.getId().equals(id)) 
            {
                return item;
            }
        }
        return null; // not found
    }
}
