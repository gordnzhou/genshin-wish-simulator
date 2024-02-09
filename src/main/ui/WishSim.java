package ui;

import model.Banner;
import model.Character;
import model.Weapon;
import model.Wish;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WishSim {
    public static final int PRIMOGEMS_PER_WISH = 160;

    private Banner banner;
    private Scanner input;

    private List<Wish> inventory; // change to hashmap in the future?
    private int totalWishCount;
    private int primogems;

    // EFFECTS: instantiates WishSim with given primogems and an empty inventory
    public WishSim(int primogems) {
        this.inventory = new ArrayList<>();
        this.totalWishCount = 0;
        this.primogems = primogems;
    }

    // MODIFIES: this
    // EFFECTS: initialize all banners and loads in their wish pool
    public void init() {
        List<Wish> wishPool = new ArrayList<>(); // change to load from a JSON
        wishPool.add(new Character(5, "Jean", Character.Element.ANEMO, Weapon.WeaponType.SWORD));
        wishPool.add(new Character(5, "Diluc", Character.Element.PYRO, Weapon.WeaponType.GREATSWORD));
        wishPool.add(new Character(5, "Mona", Character.Element.HYDRO, Weapon.WeaponType.CATALYST));
        wishPool.add(new Weapon(5, "Aquila Favonia", Weapon.WeaponType.SWORD));
        wishPool.add(new Character(4, "Amber", Character.Element.PYRO, Weapon.WeaponType.BOW));
        wishPool.add(new Character(4, "Lisa", Character.Element.ELECTRO, Weapon.WeaponType.CATALYST));
        wishPool.add(new Character(4, "Kaeya", Character.Element.CRYO, Weapon.WeaponType.SWORD));
        wishPool.add(new Character(4, "Xiangling", Character.Element.PYRO, Weapon.WeaponType.POLEARM));
        wishPool.add(new Character(4, "Ningguang", Character.Element.GEO, Weapon.WeaponType.CATALYST));
        wishPool.add(new Character(4, "Yaoyao", Character.Element.DENDRO, Weapon.WeaponType.POLEARM));
        wishPool.add(new Weapon(3, "Harbinger of Dawn", Weapon.WeaponType.SWORD));
        wishPool.add(new Weapon(3, "Ferrous Shadow", Weapon.WeaponType.GREATSWORD));
        wishPool.add(new Weapon(3, "Black Tassel", Weapon.WeaponType.POLEARM));
        wishPool.add(new Weapon(3, "Recurve Bow", Weapon.WeaponType.BOW));
        wishPool.add(new Weapon(5, "Magic Guide", Weapon.WeaponType.CATALYST));
        banner = new Banner("Banner 1", wishPool);

        input = new Scanner(System.in);
        input.useDelimiter("\n");
    }

    // MODIFIES: this
    // EFFECTS: runs the Wish Simulator
    public void runWishSim() {
        boolean keepGoing = true;

        init();

        while (keepGoing) {
            displayMenu();
            String command = input.nextLine();
            command = command.toLowerCase();
            keepGoing &= processCommand(command);
        }

        System.out.println("Ok bye");
    }

    // MODIFIES: this
    // EFFECTS: processes user command and returns false if user wants to quit; true otherwise
    private boolean processCommand(String command) {
        switch (command) {
            case "i":
                viewInventory();
                break;
            case "w":
                makeWish(askForPositiveInt());
                break;
            case "p":
                addPrimogems(askForPositiveInt());
                break;
            case "q":
                return false;
            default:
                return true;
        }
        pause();
        return true;
    }

    // EFFECTS: pauses console and waits for key to be pressed
    private void pause() {
        System.out.println("(Press any key to continue)");
        input.nextLine();
    }

    // EFFECTS: repeatedly prompts user for a valid positive integer and returns it
    private int askForPositiveInt() {
        while (true) {
            System.out.println("Enter a positive amount: ");
            int amount = input.nextInt();
            input.nextLine();
            if (amount <= 0) {
                System.out.println("Cannot enter a negative/zero amount.");
            } else {
                return amount;
            }
        }
    }

    // EFFECTS: prints out a list of possible commands
    private void displayMenu() {
        System.out.format("You Currently Have %d Primogems\n", primogems);
        System.out.println("----COMMAND LIST----");
        System.out.println("i => View Inventory");
        System.out.println("w => Make Wish");
        System.out.println("p => Add Primogems");
        System.out.println("q => Quit");
        System.out.print("Enter a command: ");
    }

    // REQUIRES: amount > 0
    // MODIFIES: this
    // EFFECTS: increments primogems by the given amount
    public void addPrimogems(int amount) {
        System.out.format("Successfully Added %d Primogems to Your Inventory.\n", amount);
        primogems += amount;
    }

    // EFFECTS: prints a list showing wishes in current inventory
    private void viewInventory() {
        if (inventory.isEmpty()) {
            System.out.println("Your Inventory is Empty...");
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
                    System.out.format("%d) '%s' (%d stars), Character Vision: %s, Preferred Weapon: %s\n",
                            i, name, rarity, ((Character) wish).getVision(), ((Character) wish).getPreferredWeapon());
                }
                i++;
            }
        }
    }

    // REQUIRES: count > 0
    // MODIFIES: this
    // EFFECTS: if user has enough primogems, makes wish(es) from banner, adds them all to inventory
    //           and prints the results; otherwise do nothing
    public void makeWish(int count) {
        int cost = PRIMOGEMS_PER_WISH * count;
        if (primogems < cost) {
            System.out.println("You do not have enough Primogems!");
            return;
        }

        List<Wish> wishes = banner.makeWish(count);
        totalWishCount += count;
        primogems -= cost;

        inventory.addAll(wishes);
        displayWishResults(wishes);
    }

    // REQUIRES: wishes is non-empty
    // EFFECTS: prints wish results
    private void displayWishResults(List<Wish> wishes) {
        int i = 1;
        for (Wish wish : wishes) {
            if (wish.getRarity() == 5) {
                System.out.format("%d) WOW!! Obtained '%s' (%d stars)!!!\n", i, wish.getName(), wish.getRarity());
            } else if (wish.getRarity() == 4) {
                System.out.format("%d) wow! Obtained '%s' (%d stars)!\n", i, wish.getName(), wish.getRarity());
            } else if (wish.getRarity() == 3) {
                System.out.format("%d) Obtained '%s' (%d stars)\n", i, wish.getName(), wish.getRarity());
            }
            i++;
            // pause() for dramatic effect
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

    public int getPrimogems() {
        return primogems;
    }
}
