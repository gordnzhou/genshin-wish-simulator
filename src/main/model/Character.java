package model;

public class Character implements Wish {
    public enum Element {
        ANEMO,
        CRYO,
        DENDRO,
        ELECTRO,
        GEO,
        HYDRO,
        PYRO
    }

    private int rarity;
    private String name;
    private Element vision;

    // REQUIRES: rarity must be in the interval [3, 5]
    // EFFECTS: instantiates a character with given
    // name, rarity and vision element
    public Character(int rarity, String name, Element vision) {
        
    }

    // EFFECTS: returns this character's rarity
    public int getRarity() {
        return rarity;
    }

    // EFFECTS: returns this character's name
    public String getName() {
        return name;
    }

    // EFFECTS: returns this character's vision element
    public Element getVision() {
        return vision;
    }
}
