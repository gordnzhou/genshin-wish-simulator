package model;

// represents a wish of type weapon
public class Weapon extends Wish {
    public enum WeaponType {
        BOW,
        CATALYST,
        GREATSWORD,
        POLEARM,
        SWORD,
    }

    private WeaponType weaponType;

    // REQUIRES: rarity must be in the interval [3, 5]
    // EFFECTS: instantiates a weapon with given
    // name, rarity and weapon type
    public Weapon(int rarity, String name, WeaponType weaponType) {
        super(rarity, name);
        this.weaponType = weaponType;
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }
}
