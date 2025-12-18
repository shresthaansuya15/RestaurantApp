package dataaccess;

import model.Order;
import java.io.*;
import java.util.*;

public class OrderDAO 
{
    private String filePath = "data/orders.txt";

    public List<Order> getAllOrders() 
    {
        List<Order> orders = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) 
        {
            String line = br.readLine(); // skip header

            while ((line = br.readLine()) != null) 
            {
                if (line.trim().isEmpty()) continue; // skip empty lines

                String[] parts = line.split("\\|");

                if (parts.length < 7) continue;

                orders.add(new Order(parts[0], parts[1], parts[2], parts[3],
                                     Integer.parseInt(parts[4]), Double.parseDouble(parts[5]), parts[6]));
            }
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        return orders;
    }

    public List<Order> getOrdersByUsername(String username) 
    {
        List<Order> all = getAllOrders();
        List<Order> filtered = new ArrayList<>();
        for (Order o : all) 
        {
            if (o.getUsername().equals(username)) filtered.add(o);
        }
        return filtered;
    }

    public void addOrder(Order order) 
    {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) 
        {
            bw.write(String.format("%s|%s|%s|%s|%d|%.2f|%s\n",
                    order.getOrderId(),
                    order.getUsername(),
                    order.getRestaurantId(),
                    order.getFoodId(),
                    order.getQuantity(),
                    order.getTotalPrice(),
                    order.getStatus()));
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
}
