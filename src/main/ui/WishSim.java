package ui;

import exceptions.NotEnoughPrimosException;
import model.Inventory;
import model.banner.Banner;
import model.wish.Character;
import model.wish.Weapon;
import model.wish.Wish;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

// Genshin Wish Simulator
public class WishSim {
    public static final int PRIMOGEMS_PER_WISH = 160;
    private static final String JSON_STORE = "./data/inventory.json";
    private static final String STANDARD_BANNER_JSON_PATH = "./data/static/standard_banner.json";
    private static final String EVENT_BANNER_JSON_PATH = "./data/static/event_banner.json";

    private Banner banner;
    private Banner eventBanner;
    private Inventory inventory;
    private int totalWishCount;

    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // REQUIRES: primogems >= 0
    // EFFECTS: instantiates WishSim with given primogems and an empty inventory;
    //          throws FileNotFoundException if JSON_STORE cannot be opened for writing
    public WishSim(int primogems) throws FileNotFoundException {
        this.inventory = new Inventory(new HashMap<>(), primogems);
        this.totalWishCount = 0;
        this.jsonReader = new JsonReader(JSON_STORE);
        this.jsonWriter = new JsonWriter(JSON_STORE);
    }

    // MODIFIES: this
    // EFFECTS: initialize input and loads in banner data
    public void init() {
        banner = loadBannerFromPath(STANDARD_BANNER_JSON_PATH);
        eventBanner = loadBannerFromPath(EVENT_BANNER_JSON_PATH);
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

    // REQUIRES: path is a valid path to a json file
    // EFFECTS: loads banners from the JSON file at the given path and returns it
    private Banner loadBannerFromPath(String path) {
        try {
            JsonReader bannerReader = new JsonReader(path);
            Banner banner = bannerReader.readBanner();
            System.out.println("Loaded " + banner.getName() + " from " + path);
            return banner;
        } catch (IOException e) {
            System.out.println("Unable to banner read from path: " + path);
            return null;
        }
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
                makeWish(askForBanner(), askForPositiveInt());
                break;
            case "p":
                addPrimogems(askForPositiveInt());
                break;
            case "l":
                loadInventory();
                break;
            case "s":
                saveInventory();
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
    // EFFECTS: loads inventory from JSON
    private void loadInventory() {
        try {
            inventory = jsonReader.readInventory();
            System.out.println("Loaded inventory from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to banner read from path: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: saves current inventory to file as JSON
    private void saveInventory() {
        try {
            jsonWriter.open();
            jsonWriter.writeWritable(inventory);
            jsonWriter.close();
            System.out.println("Saved inventory to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
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

    // MODIFIES: this
    // EFFECTS: repeatedly prompts user to select either banner or eventBanner
    private Banner askForBanner() {
        while (true) {
            System.out.format("Enter '0' to wish from standard banner: '%s', '1' for current Event Banner: '%s'\n",
                    banner.getName(), eventBanner.getName());
            int response = input.nextInt();
            input.nextLine();

            if (response == 0) {
                return banner;
            } else if (response == 1) {
                return eventBanner;
            } else {
                System.out.println("Invalid input.");
            }
        }
    }

    // EFFECTS: prints out a list of possible commands
    private void displayMenu() {
        System.out.format("You Currently Have %d Primogems\n", inventory.getPrimogems());
        System.out.println("----COMMAND LIST----");
        System.out.println("i => View Inventory");
        System.out.println("w => Make Wish");
        System.out.println("p => Add Primogems");
        System.out.println("l => Load Inventory From File");
        System.out.println("s => Save Current Inventory To File");
        System.out.println("q => Quit");
        System.out.print("Enter a command: ");
    }

    // REQUIRES: amount > 0
    // MODIFIES: this
    // EFFECTS: increments primogems by the given amount and displays a message
    public void addPrimogems(int amount) {
        System.out.format("Successfully Added %d Primogems to Your Inventory.\n", amount);
        inventory.addPrimogems(amount);
    }

    // EFFECTS: prints a list showing all wishes in current inventory
    private void viewInventory() {
        if (inventory.getWishes().isEmpty()) {
            System.out.println("Your Inventory is Empty...");
        } else {
            System.out.println("------- INVENTORY -------");
            for (Wish wish : inventory.getWishes()) {
                int count = inventory.getWishCopies(wish);
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
    // EFFECTS: if user has enough primogems, makes wish(es) from the chosen banner,
    //          adds them all to inventory and prints the results; otherwise do nothing
    public void makeWish(Banner banner, int count) {
        int cost = PRIMOGEMS_PER_WISH * count;

        try {
            this.inventory.removePrimogems(cost);
        } catch (NotEnoughPrimosException e) {
            System.out.println("You do not have enough Primogems!");
            return;
        }

        int start = banner.getFiveStarPity();

        List<Wish> wishes = banner.makeWish(count);
        totalWishCount += count;

        addWishes(wishes);
        displayWishResults(wishes, start);
    }

    // MODIFIES: this
    // EFFECTS: adds all wishes to inventory
    private void addWishes(List<Wish> wishes) {
        for (Wish wish : wishes) {
            inventory.addWish(wish);
        }
    }

    // REQUIRES: wishes is non-empty
    // EFFECTS: prints wish results with numbering based on current five-star pity
    private void displayWishResults(List<Wish> wishes, int start) {
        int i = start + 1;
        for (Wish wish : wishes) {
            if (wish.getRarity() == 5) {
                System.out.format("%d) WOW!! Obtained '%s' (%d stars)!!!\n", i, wish.getName(), wish.getRarity());
                i = 0;
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

    public Set<Wish> getInventoryWishes() {
        return inventory.getWishes();
    }

    public Banner getBanner() {
        return banner;
    }

    public int getPrimogems() {
        return inventory.getPrimogems();
    }
}
