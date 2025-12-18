package dataaccess;

import model.FoodItem;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CartDAO {

    private static final String FILE_PATH = "data/cart.txt";

    // Save cart to file
    public void saveCart(Map<FoodItem, Integer> cart) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Map.Entry<FoodItem, Integer> entry : cart.entrySet()) {
                bw.write(entry.getKey().getId() + "|" + entry.getValue());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load cart from file
    public Map<String, Integer> loadCart() {
        Map<String, Integer> cart = new HashMap<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return cart;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length < 2) continue;
                cart.put(parts[0], Integer.parseInt(parts[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cart;
    }

    // Clear cart file
    public void clearCart() {
        File file = new File(FILE_PATH);
        if (file.exists()) file.delete();
    }
}
