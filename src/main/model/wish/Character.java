package model.wish;

import model.Element;
import model.WeaponType;
import org.json.JSONObject;

import java.util.Objects;

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

    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();
        json.put("type", "character");
        json.put("vision", vision);
        json.put("weapon", weapon);
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
        Character character = (Character) o;
        return vision == character.vision && weapon == character.weapon;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), vision, weapon);
    }

    public Element getVision() {
        return vision;
    }

    public WeaponType getWeapon() {
        return weapon;
    }
}
