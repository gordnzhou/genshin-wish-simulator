package model.wish;

import model.WeaponType;
import org.json.JSONObject;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Weapon weapon = (Weapon) o;
        return weaponType == weapon.weaponType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), weaponType);
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }
}
