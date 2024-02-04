package model;

public class Weapon implements Wish {
    public enum WeaponType {
        BOW,
        CATALYST,
        GREATSWORD,
        SPEAR,
        SWORD,
    }

    private int rarity;
    private String name;
    private WeaponType weaponType;

    // REQUIRES: rarity must be in the interval [3, 5]
    // EFFECTS: instantiates a weapon with given
    // name, rarity and weapon type
    public Weapon(int rarity, String name, WeaponType weaponType) {

    }

    // EFFECTS: returns this weapon's rarity
    public int getRarity() {
        return rarity;
    }

    // EFFECTS: returns this weapon's name
    public String getName() {
        return name;
    }

    // EFFECTS: returns this weapon's type
    public WeaponType getWeaponType() {
        return weaponType;
    }
}
