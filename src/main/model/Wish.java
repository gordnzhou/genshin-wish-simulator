package model;

public abstract class Wish {
    private int rarity;
    private String name;

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
