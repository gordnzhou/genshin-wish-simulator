package model;

public interface InventoryObserver {
    // MODIFIES: this
    // EFFECTS: updates this class based on number of primogems
    void update(int primogems);
}

