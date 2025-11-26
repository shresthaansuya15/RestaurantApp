package model;

public class Order 
{
    private String orderId;
    private String username;
    private String restaurantId;
    private String foodId;
    private int quantity;
    private double totalPrice;
    private String status;

    public Order(String orderId, String username, String restaurantId, String foodId,
                 int quantity, double totalPrice, String status) 
    {
        this.orderId = orderId;
        this.username = username;
        this.restaurantId = restaurantId;
        this.foodId = foodId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    // Getters
    public String getOrderId() 
    { 
        return orderId; 
    }

    public String getUsername() 
    { 
        return username; 
    }

    public String getRestaurantId() 
    { 
        return restaurantId; 
    }

    public String getFoodId() 
    { 
        return foodId; 
    }

    public int getQuantity() 
    { 
        return quantity; 
    }

    public double getTotalPrice() 
    { 
        return totalPrice; 
    }

    public String getStatus() 
    { 
        return status; 
    }
}
