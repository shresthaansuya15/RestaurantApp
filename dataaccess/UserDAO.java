package dataaccess;

import model.User;
import java.io.*;
import java.util.*;

public class UserDAO 
{
    private String filePath = "data/users.txt";

    public List<User> getAllUsers() 
    {
        List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) 
        {
            String line = br.readLine(); // skip header

            while ((line = br.readLine()) != null) 
            {
                if (line.trim().isEmpty()) continue; // skip empty lines

                String[] parts = line.split("\\|");

                if (parts.length < 4) continue;

                users.add(new User(parts[0], parts[1], parts[2], parts[3]));
            }

        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        
        return users;
    }
}
