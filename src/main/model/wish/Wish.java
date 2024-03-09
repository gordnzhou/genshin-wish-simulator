package model.wish;

import org.json.JSONObject;
import persistence.Writable;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Wish wish = (Wish) o;
        return rarity == wish.rarity && Objects.equals(name, wish.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rarity, name);
    }

    public int getRarity() {
        return rarity;
    }

    public String getName() {
        return name;
    }
}
