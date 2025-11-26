package model;

public class User 
{
    private String username;
    private String password;
    private String fullName;
    private String email;

    public User(String username, String password, String fullName, String email) 
    {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
    }

    // Getters
    public String getUsername() 
    { 
        return username; 
    }

    public String getPassword() 
    { 
        return password; 
    }

    public String getFullName() 
    { 
        return fullName; 
    }

    public String getEmail() 
    { 
        return email; 
    }
}
