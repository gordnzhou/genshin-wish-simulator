package model;

import model.Weapon.WeaponType;

// represents a wish of type character
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
    private WeaponType preferredWeapon;

    // REQUIRES: rarity must be in the interval [3, 5]
    // EFFECTS: instantiates a character with given name, rarity and preferred weapon
    public Character(int rarity, String name, Element vision, WeaponType preferredWeapon) {
        super(rarity, name);
        this.vision = vision;
        this.preferredWeapon = preferredWeapon;
    }

    public Element getVision() {
        return vision;
    }

    public WeaponType getPreferredWeapon() {
        return preferredWeapon;
    }
}
