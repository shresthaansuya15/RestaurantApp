package model;

public class Review 
{
    private String reviewId;
    private String restaurantId;
    private String username;
    private int rating;
    private String comment;

    public Review(String reviewId, String restaurantId, String username, int rating, String comment) 
    {
        this.reviewId = reviewId;
        this.restaurantId = restaurantId;
        this.username = username;
        this.rating = rating;
        this.comment = comment;
    }

    // Getters
    public String getReviewId() 
    { 
        return reviewId; 
    }

    public String getRestaurantId() 
    {  
        return restaurantId; 
    }

    public String getUsername() 
    { 
        return username; 
    }

    public int getRating() 
    { 
        return rating; 
    }

    public String getComment() 
    { 
        return comment; 
    }
}
