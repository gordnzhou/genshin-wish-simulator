package ui;

import model.Banner;
import model.Character;
import model.Weapon;
import model.Wish;
import model.WeaponType;
import model.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Genshin Wish Simulator
public class WishSim {
    public static final int PRIMOGEMS_PER_WISH = 160;

    private Banner banner;
    private Scanner input;

    // change to hashmap in the future
    private List<Wish> inventory;
    private int totalWishCount;
    private int primogems;

    // REQUIRES: primogems >= 0
    // EFFECTS: instantiates WishSim with given primogems and an empty inventory
    public WishSim(int primogems) {
        this.inventory = new ArrayList<>();
        this.totalWishCount = 0;
        this.primogems = primogems;
    }

    // MODIFIES: this
    // EFFECTS: initialize all input and banners and loads in their wish pool
    public void init() {
        // change to load from a JSON
        List<Wish> wishPool = new ArrayList<>();
        wishPool.add(new Character(5, "Jean", Element.ANEMO, WeaponType.SWORD));
        wishPool.add(new Character(5, "Diluc", Element.PYRO, WeaponType.GREATSWORD));
        wishPool.add(new Character(5, "Mona", Element.HYDRO, WeaponType.CATALYST));
        wishPool.add(new Weapon(5, "Aquila Favonia", WeaponType.SWORD));
        wishPool.add(new Character(4, "Amber", Element.PYRO, WeaponType.BOW));
        wishPool.add(new Character(4, "Lisa", Element.ELECTRO, WeaponType.CATALYST));
        wishPool.add(new Character(4, "Kaeya", Element.CRYO, WeaponType.SWORD));
        wishPool.add(new Character(4, "Xiangling", Element.PYRO, WeaponType.POLEARM));
        wishPool.add(new Character(4, "Ningguang", Element.GEO, WeaponType.CATALYST));
        wishPool.add(new Character(4, "Yaoyao", Element.DENDRO, WeaponType.POLEARM));
        wishPool.add(new Weapon(3, "Harbinger of Dawn", WeaponType.SWORD));
        wishPool.add(new Weapon(3, "Ferrous Shadow", WeaponType.GREATSWORD));
        wishPool.add(new Weapon(3, "Black Tassel", WeaponType.POLEARM));
        wishPool.add(new Weapon(3, "Recurve Bow", WeaponType.BOW));
        wishPool.add(new Weapon(5, "Magic Guide", WeaponType.CATALYST));
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
    //          will also pause the terminal after a valid command
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

    // MODIFIES: this
    // EFFECTS: pauses console and waits for key to be pressed
    private void pause() {
        System.out.println("(Press any key to continue)");
        input.nextLine();
    }

    // MODIFIES: this
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
    // EFFECTS: increments primogems by the given amount and displays a message
    public void addPrimogems(int amount) {
        System.out.format("Successfully Added %d Primogems to Your Inventory.\n", amount);
        primogems += amount;
    }

    // EFFECTS: prints a list showing all wishes in current inventory
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
                            i, name, rarity, ((Character) wish).getVision(), ((Character) wish).getWeapon());
                }
                i++;
            }
        }
    }

    // REQUIRES: count > 0
    // MODIFIES: this
    // EFFECTS: if user has enough primogems, makes wish(es) from banner, adds them all to inventory
    //          and prints the results; otherwise do nothing
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
