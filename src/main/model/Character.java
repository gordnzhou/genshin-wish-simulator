package model;

public class Character extends Wish {
    public enum Element {
        ANEMO,
        CRYO,
        DENDRO,
        ELECTRO,
        GEO,
        HYDRO,
        PYRO
    }

    private Element vision;

    // REQUIRES: rarity must be in the interval [3, 5]
    // EFFECTS: instantiates a character with given name and rarity
    public Character(int rarity, String name, Element vision) {
        super(rarity, name);
        this.vision = vision;
    }

    // EFFECTS: returns this character's vision element
    public Element getVision() {
        return vision;
    }
}
