package persistence;

import model.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import model.Character;
import org.json.*;

// Represents a reader that reads banner and inventory from JSON data stored in file
// credits: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads banner from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Banner readBanner() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);

        return parseBanner(jsonObject);
    }

    // EFFECTS: readers inventory from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Map<Wish, Integer> readInventory() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);

        JSONArray data = jsonObject.getJSONArray("data");

        Map<Wish, Integer> inventory = new HashMap<>();

        // inventory is read from JSON as an array of 2-long arrays
        // with key as 1st element and its corresponding value as 2nd
        for (Object item : data) {
            JSONArray jsonArray = (JSONArray) item;

            Wish wish = parseWish(jsonArray.getJSONObject(0));
            int count = jsonArray.getInt(1);

            inventory.put(wish, count);
        }

        return inventory;
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses banner from JSON object and returns it
    private Banner parseBanner(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        List<Wish> wishPool = parseWishes(jsonObject.getJSONArray("wish_pool"));

        if (jsonObject.has("rate_up_five_star")) {
            Wish rateUpFiveStar = parseWish(jsonObject.getJSONObject("rate_up_five_star"));
            List<Wish> rateUpFourStars = parseWishes(jsonObject.getJSONArray("rate_up_four_stars"));

            return new EventBanner(name, wishPool, rateUpFiveStar, rateUpFourStars);
        }

        return new Banner(name, wishPool);
    }

    // EFFECTS: parses a list of wishes from a JSON array and returns it
    private List<Wish> parseWishes(JSONArray jsonArray) {
        List<Wish> wishes = new ArrayList<>();

        for (Object json : jsonArray) {
            JSONObject wish = (JSONObject) json;
            wishes.add(parseWish(wish));
        }

        return wishes;
    }

    // EFFECTS: parses wish from JSON object and returns it
    private Wish parseWish(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        int rarity = jsonObject.getInt("rarity");

        if (jsonObject.getString("type").equals("character")) {
            Element element = Element.valueOf(jsonObject.getString("vision"));
            WeaponType weapon = WeaponType.valueOf(jsonObject.getString("weapon"));

            return new Character(rarity, name, element, weapon);
        } else {
            WeaponType weaponType = WeaponType.valueOf(jsonObject.getString("weapon_type"));

            return new Weapon(rarity, name, weaponType);
        }
    }
}