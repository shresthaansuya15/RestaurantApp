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
                if (line.trim().isEmpty()) continue; 
                String[] parts = line.split("\\|");
                if (parts.length < 4) continue;
                users.add(new User(parts[0], parts[1], parts[2], parts[3]));
            }
        } 
        catch (IOException e) { e.printStackTrace(); }
        return users;
    }

    public User getUserByUsername(String username) {
        for (User u : getAllUsers()) {
            if (u.getUsername().equals(username)) return u;
        }
        return null;
    }

    public boolean updateUser(User updatedUser) {
        List<User> users = getAllUsers();
        boolean found = false;

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(updatedUser.getUsername())) {
                users.set(i, updatedUser);
                found = true;
                break;
            }
        }

        if (!found) return false;

        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            pw.println("Username|Password|FullName|Email");
            for (User u : users) {
                pw.println(
                    u.getUsername() + "|" +
                    u.getPassword() + "|" +
                    u.getFullName() + "|" +
                    u.getEmail()
                );
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Optional: validate login
    public boolean validateUser(String username, String password) {
        User u = getUserByUsername(username);
        return u != null && u.getPassword().equals(password);
    }
}
