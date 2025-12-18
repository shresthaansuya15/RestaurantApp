package model;

public class FoodItem 
{
    private String id;
    private String restaurantId;
    private String name;
    private double price;

    public FoodItem(String id, String restaurantId, String name, double price) 
    {
        this.id = id;
        this.restaurantId = restaurantId;
        this.name = name;
        this.price = price;
    }

    // Getters
    public String getId() 
    { 
        return id; 
    }

    public String getRestaurantId()  
    { 
        return restaurantId; 
    }

    public String getName() 
    { 
        return name; 
    }

    public double getPrice() 
    { 
        return price; 
    }

    @Override
    public String toString()
    {
        return name + " ($" + price + ")";
    }
}
