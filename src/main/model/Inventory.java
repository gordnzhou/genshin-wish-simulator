package model;

import exceptions.NotEnoughPrimosException;
import model.wish.Wish;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

// represents a user's inventory of wishes and primogems.
public class Inventory implements Writable {
    private Map<Wish, Integer> wishes;
    private int primogems;

    private List<InventoryObserver> inventoryObservers;

    // REQUIRES: wishes >= 0
    // EFFECTS: creates inventory with given wishes and primogems
    public Inventory(Map<Wish, Integer> wishes, int primogems) {
        this.wishes = wishes;
        this.primogems = primogems;
        this.inventoryObservers = new ArrayList<>();
        updateObservers();
    }

    // MODIFIES: this
    // EFFECTS: adds inventoryObserver to inventoryObservers;
    public void addObserver(InventoryObserver inventoryObserver) {
        inventoryObservers.add(inventoryObserver);
    }

    // EFFECTS: called update() on inventoryObservers with current number of primogems
    private void updateObservers() {
        for (InventoryObserver observer : inventoryObservers) {
            observer.update(this.primogems);
        }
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
        String eventString = String.format("Obtained a '%s'", wish.getName());

        if (wishes.containsKey(wish)) {
            wishes.put(wish, wishes.get(wish) + 1);
        } else {
            eventString = eventString + " (NEW)";
            wishes.put(wish, 1);
        }

        EventLog.getInstance().logEvent(new Event(eventString));
    }

    // EFFECTS: adds the given number of primogems to inventory only IF count > 0
    public void addPrimogems(int count) {
        primogems += Math.max(0, count);
        EventLog.getInstance().logEvent(new Event("Added " + count + " primogems to inventory"));
        updateObservers();
    }

    // EFFECTS: removes the given number of primogems from inventory only IF count > 0.
    //          if count > this.primogems, throws a NotEnoughPrimosException.
    public void removePrimogems(int count) throws NotEnoughPrimosException {
        if (count > primogems) {
            throw new NotEnoughPrimosException();
        }

        primogems -= Math.max(0, count);
        updateObservers();
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
