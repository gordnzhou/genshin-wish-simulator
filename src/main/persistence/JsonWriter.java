package persistence;

import model.Wish;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.*;
import java.util.Map;

// Represents a writer that writes JSON representation of workroom to file
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

    // EFFECTS: converts inventory to JSON format
    private JSONObject inventoryToJson(Map<Wish, Integer> inventory) {
        JSONObject json = new JSONObject();
        JSONArray data = new JSONArray();

        for (Map.Entry<Wish, Integer> entry : inventory.entrySet()) {
            JSONArray jsonArray = new JSONArray();
            Wish wish = entry.getKey();
            int count = entry.getValue();
            jsonArray.put(wish.toJson());
            jsonArray.put(count);
            data.put(jsonArray);
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