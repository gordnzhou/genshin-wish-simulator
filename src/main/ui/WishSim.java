package ui;

import model.*;
import model.Character;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

// Genshin Wish Simulator
public class WishSim {
    public static final int PRIMOGEMS_PER_WISH = 160;
    private static final String STANDARD_BANNER_JSON_PATH = "./data/standard_banner.json";
    private static final String EVENT_BANNER_JSON_PATH = "./data/event_banner.json";

    private Banner banner;
    private Scanner input;
    private Map<Wish, Integer> inventory;
    private int totalWishCount;
    private int primogems;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // REQUIRES: primogems >= 0
    // EFFECTS: instantiates WishSim with given primogems and an empty inventory
    public WishSim(int primogems) throws FileNotFoundException {
        this.inventory = new HashMap<>();
        this.totalWishCount = 0;
        jsonReader = new JsonReader(STANDARD_BANNER_JSON_PATH);
        this.primogems = primogems;
    }

    // MODIFIES: this
    // EFFECTS: initialize all input and banners and loads in their wish pool
    public void init() {
        loadBanner();
        input = new Scanner(System.in);
        input.useDelimiter("\n");
    }

    // MODIFIES: this
    // EFFECTS: loads banner from file
    private void loadBanner() {
        try {
            banner = jsonReader.readBanner();
            System.out.println("Loaded " + banner.getName() + " from " + STANDARD_BANNER_JSON_PATH);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + STANDARD_BANNER_JSON_PATH);
        }
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
            System.out.println("------- INVENTORY -------");
            for (Map.Entry<Wish, Integer> entry : inventory.entrySet()) {
                Wish wish = entry.getKey();
                int count = entry.getValue();
                String name = wish.getName();
                int rarity = wish.getRarity();
                if (wish instanceof Weapon) {
                    System.out.format("- '%s' (%d stars), Weapon Type: %s, Copies: %d\n",
                            name, rarity, ((Weapon) wish).getWeaponType(), count);
                } else if (wish instanceof Character) {
                    System.out.format("- '%s' (%d stars), Vision: %s, Weapon: %s, Constellation(s): %d\n",
                            name, rarity, ((Character) wish).getVision(),
                            ((Character) wish).getWeapon(), Math.min(count, 6));
                }
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

        addWishes(wishes);
        displayWishResults(wishes);
    }

    // MODIFIES: this
    // EFFECTS: adds all wishes to inventory
    private void addWishes(List<Wish> wishes) {
        for (Wish wish : wishes) {
            if (inventory.containsKey(wish)) {
                inventory.put(wish, inventory.get(wish) + 1);
            } else {
                inventory.put(wish, 1);
            }
        }
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

    public Map<Wish, Integer> getInventory() {
        return inventory;
    }

    public Banner getBanner() {
        return banner;
    }

    public int getPrimogems() {
        return primogems;
    }
}
