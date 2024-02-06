package ui;

import model.Banner;
import model.Character;
import model.Weapon;
import model.Wish;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WishSim {
    private List<Wish> inventory; // change to hashmap in the future?
    private Banner banner;
    private Scanner scanner;

    // EFFECTS: parses wish data into banner and runs the wish simulator
    public WishSim() {
        List<Wish> wishPool = new ArrayList<Wish>(); // change to load from a JSON
        wishPool.add(new Character(5, "Jean", Character.Element.ANEMO));
        wishPool.add(new Character(4, "Amber", Character.Element.PYRO));
        wishPool.add(new Weapon(3, "Sword", Weapon.WeaponType.SWORD));
        wishPool.add(new Character(5, "Diluc", Character.Element.PYRO));
        wishPool.add(new Character(4, "Lisa", Character.Element.ELECTRO));
        wishPool.add(new Character(4, "Kaeya", Character.Element.CRYO));
        wishPool.add(new Weapon(3, "aa", Weapon.WeaponType.GREATSWORD));
        wishPool.add(new Weapon(3, "bb", Weapon.WeaponType.SPEAR));
        wishPool.add(new Weapon(3, "cc", Weapon.WeaponType.BOW));
        wishPool.add(new Weapon(5, "dd", Weapon.WeaponType.CATALYST));

        inventory = new ArrayList<Wish>();
        banner = new Banner("Banner 1", wishPool);
    }

    // REQUIRES: count >= 1
    // MODIFIES: this
    // EFFECTS: makes wish from banner 'count' times and adds all the results to inventory
    public void makeWish(int count) {
        List<Wish> wishes = banner.makeWish(count);
        inventory.addAll(wishes);

        int i = 1;
        for (Wish wish : wishes) {
            System.out.format("%d) You got a '%s' (%d stars)!\n", i, wish.getName(), wish.getRarity());
            i++;
        }
    }
}
