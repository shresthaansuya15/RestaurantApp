package model;

public class FoodItem 
{
    private String restaurantId;
    private String foodId;
    private String name;
    private double price;

    public FoodItem(String restaurantId, String foodId, String name, double price) 
    {
        this.restaurantId = restaurantId;
        this.foodId = foodId;
        this.name = name;
        this.price = price;
    }

    // Getters
    public String getRestaurantId() 
    { 
        return restaurantId; 
    }

    public String getFoodId() 
    { 
        return foodId; 
    }

    public String getName() 
    { 
        return name; 
    }

    public double getPrice() 
    { 
        return price; 
    }
}
