package model;

// represents a wish, with a name and rarity
public abstract class Wish {
    private final int rarity;
    private final String name;

    // REQUIRES: rarity must be in the interval [3, 5]
    // EFFECTS: instantiates wish with given rarity and name
    public Wish(int rarity, String name) {
        this.rarity = rarity;
        this.name = name;
    }

    // EFFECTS: returns this wish's rarity
    public int getRarity() {
        return rarity;
    }

    // EFFECTS: returns this wish's name
    public String getName() {
        return name;
    }
}
