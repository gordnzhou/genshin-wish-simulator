package model;

import org.json.JSONObject;
import persistence.Writable;

// represents a wish, with a name and rarity
public abstract class Wish implements Writable {
    private final int rarity;
    private final String name;

    // REQUIRES: rarity must be in the interval [3, 5]
    // EFFECTS: instantiates wish with given rarity and name
    public Wish(int rarity, String name) {
        this.rarity = rarity;
        this.name = name;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("rarity", rarity);
        return json;
    }

    public int getRarity() {
        return rarity;
    }

    public String getName() {
        return name;
    }
}
