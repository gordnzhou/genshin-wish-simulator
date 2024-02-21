package model;

import org.json.JSONObject;

// represents a wish of type weapon
public class Weapon extends Wish {

    private WeaponType weaponType;

    // REQUIRES: rarity must be in the interval [3, 5]
    // EFFECTS: instantiates a weapon with given
    // name, rarity and weapon type
    public Weapon(int rarity, String name, WeaponType weaponType) {
        super(rarity, name);
        this.weaponType = weaponType;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();
        json.put("type", "weapon");
        json.put("weapon_type", weaponType);
        return json;
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }
}
