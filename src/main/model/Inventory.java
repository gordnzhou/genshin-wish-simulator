package model;

import exceptions.NotEnoughPrimosException;
import model.wish.Wish;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.Map;
import java.util.Set;

// represents a user's inventory of wishes and primogems.
public class Inventory implements Writable {
    private Map<Wish, Integer> wishes;
    private int primogems;

    // REQUIRES: wishes >= 0
    // EFFECTS: creates inventory with given wishes and primogems
    public Inventory(Map<Wish, Integer> wishes, int primogems) {
        this.wishes = wishes;
        this.primogems = primogems;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        JSONArray data = new JSONArray();

        // inventory is converted to JSON as an array of 2-long arrays
        // with key as 1st element and its corresponding value as 2nd
        for (Map.Entry<Wish, Integer> entry : wishes.entrySet()) {
            JSONArray entryArray = new JSONArray();
            Wish wish = entry.getKey();
            int count = entry.getValue();

            entryArray.put(wish.toJson());
            entryArray.put(count);

            data.put(entryArray);
        }

        json.put("primogems", primogems);
        json.put("wishes", data);
        return json;
    }

    // MODIFIES: this
    // EFFECTS: adds wish to inventory
    public void addWish(Wish wish) {
        if (wishes.containsKey(wish)) {
            wishes.put(wish, wishes.get(wish) + 1);
        } else {
            wishes.put(wish, 1);
        }
    }

    // EFFECTS: adds the given number of primogems to inventory only IF count > 0
    public void addPrimogems(int count) {
        primogems += Math.max(0, count);
    }

    // EFFECTS: removes the given number of primogems from inventory only IF count > 0.
    //          if count > this.primogems, throws a NotEnoughPrimosException.
    public void removePrimogems(int count) throws NotEnoughPrimosException {
        if (count > primogems) {
            throw new NotEnoughPrimosException();
        }

        primogems -= Math.max(0, count);
    }

    // EFFECTS: returns the number of copies obtained for the given wish
    public int getWishCopies(Wish wish) {
        return wishes.containsKey(wish) ? wishes.get(wish) : 0;
    }

    // EFFECTS: returns the wishes obtained in inventory
    public Set<Wish> getWishes() {
        return wishes.keySet();
    }

    public int getPrimogems() {
        return primogems;
    }
}
