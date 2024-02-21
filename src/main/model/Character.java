package model;

import org.json.JSONObject;

// represents a wish of type character
public class Character extends Wish {

    private Element vision;
    private WeaponType weapon;

    // REQUIRES: rarity must be in the interval [3, 5]
    // EFFECTS: instantiates a character with given name, rarity and preferred weapon
    public Character(int rarity, String name, Element vision, WeaponType weapon) {
        super(rarity, name);
        this.vision = vision;
        this.weapon = weapon;
    }

    public Element getVision() {
        return vision;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();
        json.put("type", "character");
        json.put("vision", vision);
        json.put("weapon", weapon);
        return json;
    }

    public WeaponType getWeapon() {
        return weapon;
    }
}
