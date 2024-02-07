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

    private int totalWishCount = 0;

    // EFFECTS: parses wish data into banner and runs the wish simulator
    public WishSim() {
        inventory = new ArrayList<Wish>();
    }

    // MODIFIES: this
    // EFFECTS: initialize all banners and loads in their wish pool
    public void init() {
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
        banner = new Banner("Banner 1", wishPool);
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    public void runWishSim() {

    }

    // EFFECTS: prints out a list of possible commands
    private void displayCommands() {

    }

    // MODIFIES: this
    // EFFECTS: processes user command
    public void processCommand(String command) {

    }

    // EFFECTS: prints a list showing wishes in current inventory
    public void viewInventory() {
        if (inventory.isEmpty()) {
            System.out.println("You inventory is empty...");
        } else {
            int i = 1;
            System.out.println("------- INVENTORY -------");
            for (Wish wish : inventory) {
                String name = wish.getName();
                int rarity = wish.getRarity();
                if (wish instanceof Weapon) {
                    System.out.format("%d) '%s' (%d stars), Weapon Type: %s\n",
                            i, name, rarity, ((Weapon) wish).getWeaponType());
                } else if (wish instanceof Character) {
                    System.out.format("%d) '%s' (%d stars), Character Vision: %s\n",
                            i, name, rarity, ((Character) wish).getVision());
                }
                i++;
            }
        }
    }

    // REQUIRES: count >= 1
    // MODIFIES: this
    // EFFECTS: makes wish from banner 'count' times, adds all the results to inventory
    //          and prints the results
    public void makeWish(int count) {
        List<Wish> wishes = banner.makeWish(count);
        inventory.addAll(wishes);
        totalWishCount += count;

        int i = 1;
        for (Wish wish : wishes) {
            if (wish.getRarity() == 5) {
                System.out.format("%d) WOW!! You got a '%s' (%d stars)!!!\n", i, wish.getName(), wish.getRarity());
            } else if (wish.getRarity() == 4) {
                System.out.format("%d) wow! You got a '%s' (%d stars)!\n", i, wish.getName(), wish.getRarity());
            } else if (wish.getRarity() == 3) {
                System.out.format("%d) You got a '%s' (%d stars)\n", i, wish.getName(), wish.getRarity());
            }
            i++;
        }
    }

    public int getTotalWishCount() {
        return totalWishCount;
    }

    public List<Wish> getInventory() {
        return inventory;
    }

    public Banner getBanner() {
        return banner;
    }
}
