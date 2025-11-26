package model;

import java.util.List;

public class Restaurant 
{
    private String id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String hours;
    private List<String> cuisines;
    private String diningType;
    private String priceRange;
    private double rating;

    public Restaurant(String id, String name, String address, String phone, String email,
                      String hours, List<String> cuisines, String diningType, String priceRange, double rating) 
    {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.hours = hours;
        this.cuisines = cuisines;
        this.diningType = diningType;
        this.priceRange = priceRange;
        this.rating = rating;
    }

    // Getters
    public String getId() 
    { 
        return id; 
    }

    public String getName() 
    { 
        return name; 
    }

    public String getAddress() 
    { 
        return address; 
    }

    public String getPhone() 
    { 
        return phone; 
    }

    public String getEmail() 
    { 
        return email; 
    }

    public String getHours() 
    { 
        return hours; 
    }

    public List<String> getCuisines() 
    { 
        return cuisines; 
    }

    public String getDiningType() 
    { 
        return diningType; 
    }

    public String getPriceRange() 
    { 
        return priceRange; 
    }

    public double getRating() 
    { 
        return rating; 
    }
}
