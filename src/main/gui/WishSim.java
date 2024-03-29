package gui;

import exceptions.NotEnoughPrimosException;
import gui.pages.*;
import model.Inventory;
import model.banner.Banner;
import model.wish.Wish;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class WishSim extends JFrame {
    private static final int STARTING_PRIMOGEMS = 5000;

    private static final String JSON_STORE = "./data/inventory.json";
    private static final String STANDARD_BANNER_JSON_PATH = "./data/static/standard_banner.json";
    private static final String EVENT_BANNER_JSON_PATH = "./data/static/event_banner.json";

    public static final int WIDTH = 1200;
    public static final int HEIGHT = 800;
    public static final int PRIMOGEMS_PER_WISH = 160;

    private Banner banner;
    private Banner eventBanner;
    private Banner currentBanner;
    private Inventory inventory;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private CardLayout cards;
    private JPanel wishSim;

    private Page currentPage;
    private BannerMenu bannerMenu;
    private InventoryMenu inventoryMenu;
    private WishAnimation wishAnimation;
    private WishResult wishResult;

    public static void main(String[] args) {
        WishSim wishSim = new WishSim();
    }

    public WishSim() {
        super("Genshin Wish Simulator");
        this.initializeFields();
        this.initializeDisplay();
        this.initializePages();

        WishMouseListener wml = new WishMouseListener();
        addMouseListener(wml);
        setVisible(true);
    }

    private void initializePages() {
        cards = new CardLayout();
        wishSim = new JPanel(cards);
        setContentPane(wishSim);

        bannerMenu = new BannerMenu(this, STARTING_PRIMOGEMS);
        inventoryMenu = new InventoryMenu(this);
        wishAnimation = new WishAnimation(this);
        wishResult = new WishResult(this);
        currentPage = bannerMenu;
    }

    // MODIFIES: this
    // EFFECTS: initializes internal field values
    private void initializeFields() {
        this.banner = loadBannerFromPath(STANDARD_BANNER_JSON_PATH);
        this.eventBanner = loadBannerFromPath(EVENT_BANNER_JSON_PATH);
        this.currentBanner = this.banner;
        this.inventory = new Inventory(new HashMap<>(), STARTING_PRIMOGEMS);
        this.jsonReader = new JsonReader(JSON_STORE);
        this.jsonWriter = new JsonWriter(JSON_STORE);
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
    // EFFECTS: initializes JFrame display elements
    private void initializeDisplay() {
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(false);
    }

    // EFFECTS: loads path's image and scales it; returns a JLabel containing the scaled image
    // or an empty JLabel if there was an error reading the image
    public static void loadImageFromPath(JLabel label, String path, double scale) {
        try {
            BufferedImage image = ImageIO.read(new File(path));
            int scaledWidth = (int) (image.getWidth() * scale);
            int scaledHeight = (int) (image.getHeight() * scale);
            Image scaledImage = image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(scaledImage));
        } catch (IOException e) {
            System.out.println("Error Reading Image: " + e.getMessage());
        }
    }

    // MODIFIES: this
    // EFFECTS: if user has enough primogems, makes wish(es) from the chosen banner,
    //          adds them all to inventory, then displays the results
    public void makeWish(int count) {
        int cost = PRIMOGEMS_PER_WISH * count;

        if (!removePrimogems(cost)) {
            return;
        }

        List<Wish> wishes = this.currentBanner.makeWish(count);
        for (Wish wish : wishes) {
            this.inventory.addWish(wish);
        }
        this.bannerMenu.updatePrimogems(this.inventory.getPrimogems());

        switchToWishAnimation(wishes);
    }

    // MODIFIES: this
    // EFFECTS: deducts cost from inventory primogems and returns true;
    //          if not enough primogems, return false and show error popup
    private boolean removePrimogems(int cost) {
        boolean success = true;
        try {
            this.inventory.removePrimogems(cost);
        } catch (NotEnoughPrimosException e) {
            success = false;
            JOptionPane.showMessageDialog(null,
                    "Not enough Primogems in your inventory!", "Warning:", JOptionPane.WARNING_MESSAGE);
        }

        return success;
    }

    // MODIFIES: this
    // EFFECTS: sets currentPage to bannerMenu
    public void switchToBannerMenu() {
        switchPage(bannerMenu);
    }

    // MODIFIES: this
    // EFFECTS: switches currentPage to wishAnimation after
    //          making a wish in bannerMenu
    private void switchToWishAnimation(List<Wish> wishes) {
        switchPage(wishAnimation);
        wishAnimation.onPageSwitch(wishes);
    }

    // MODIFIES: this
    // EFFECTS: switches to wishResult after wishAnimation
    public void switchToWishResult(List<Wish> wishes) {
        switchPage(wishResult);
        wishResult.onPageSwitch(wishes);
    }

    // MODIFIES: this
    // EFFECTS: switches currentPage to InventoryMenu
    public void switchToInventoryMenu() {
        switchPage(inventoryMenu);
        inventoryMenu.onPageSwitch(inventory);
    }

    // MODIFIES: this
    // EFFECTS: switches currentBanner to banner
    public void switchToStandardBanner() {
        if (currentBanner == banner) {
            return;
        }
        currentBanner = banner;
    }

    // MODIFIES: this
    // EFFECTS: switches currentBanner to eventBanner
    public void switchToEventBanner() {
        if (currentBanner == eventBanner) {
            return;
        }
        currentBanner = eventBanner;
    }

    // MODIFIES: this
    // EFFECTS: switches page to nextPage and displays it
    private void switchPage(Page nextPage) {
        currentPage = nextPage;
        cards.show(wishSim, nextPage.getPageId());
        repaint();
    }

    // REQUIRES: count > 0
    // MODIFIES: this
    // EFFECTS: adds primogems to inventory and updates display
    public void addPrimogems(int count) {
        if ((long)inventory.getPrimogems() + (long) count > Integer.MAX_VALUE) {
            return;
        }
        inventory.addPrimogems(count);
        this.bannerMenu.updatePrimogems(inventory.getPrimogems());
    }

    // MODIFIES: this
    // EFFECTS: loads inventory from JSON
    public void loadInventory() {
        try {
            inventory = jsonReader.readInventory();
            this.bannerMenu.updatePrimogems(inventory.getPrimogems());
            System.out.println("Loaded inventory from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to banner read from path: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: saves current inventory to file as JSON
    public void saveInventory() {
        try {
            jsonWriter.open();
            jsonWriter.writeWritable(inventory);
            jsonWriter.close();
            System.out.println("Saved inventory to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    private void handleMousePressed(MouseEvent e) {
        currentPage.handleMousePressed();
    }

    private class WishMouseListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            handleMousePressed(e);
        }
    }
}
