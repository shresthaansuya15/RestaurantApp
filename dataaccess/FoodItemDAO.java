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
                if (line.trim().isEmpty()) continue; // skip empty lines

                String[] parts = line.split("\\|");

                if (parts.length < 4) continue;

                String restaurantId = parts[0];
                String foodId = parts[1];
                String name = parts[2];
                double price = Double.parseDouble(parts[3]);

                items.add(new FoodItem(restaurantId, foodId, name, price));
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
        List<FoodItem> all = getAllFoodItems();
        List<FoodItem> filtered = new ArrayList<>();
        for (FoodItem item : all) 
        {
            if (item.getRestaurantId().equals(restaurantId)) 
            {
                filtered.add(item);
            }
        }
        return filtered;
    }
}
