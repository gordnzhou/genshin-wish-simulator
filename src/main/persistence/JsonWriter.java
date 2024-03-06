package persistence;

import model.banner.Banner;
import model.wish.Wish;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.*;
import java.util.Map;

// Represents a writer that writes JSON representation of banner and inventory to file
// credits: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of inventory to a file
    public void writeInventory(Map<Wish, Integer> inventory) {
        JSONObject json = inventoryToJson(inventory);
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of banner to a file
    public void writeBanner(Banner banner) {
        JSONObject json = banner.toJson();
        saveToFile(json.toString(TAB));
    }

    // EFFECTS: converts inventory to JSON format
    private JSONObject inventoryToJson(Map<Wish, Integer> inventory) {
        JSONObject json = new JSONObject();

        JSONArray data = new JSONArray();

        // inventory is converted to JSON as an array of 2-long arrays
        // with key as 1st element and its corresponding value as 2nd
        for (Map.Entry<Wish, Integer> entry : inventory.entrySet()) {
            JSONArray entryArray = new JSONArray();
            Wish wish = entry.getKey();
            int count = entry.getValue();

            entryArray.put(wish.toJson());
            entryArray.put(count);

            data.put(entryArray);
        }

        json.put("data", data);
        return json;
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}