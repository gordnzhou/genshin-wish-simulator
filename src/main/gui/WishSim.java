package gui;

import exceptions.NotEnoughPrimosException;
import gui.pages.BannerMenu;
import gui.pages.Page;
import gui.pages.WishAnimation;
import gui.pages.WishResult;
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
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class WishSim extends JFrame {
    private static final int STARTING_PRIMOGEMS = 200000;

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
    public static JLabel loadImageFromPath(String path, double scale) {
        try {
            BufferedImage image = ImageIO.read(new File(path));
            int scaledWidth = (int) (image.getWidth() * scale);
            int scaledHeight = (int) (image.getHeight() * scale);
            Image scaledImage = image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
            return new JLabel(new ImageIcon(scaledImage));
        } catch (IOException e) {
            System.out.println("Error Reading Image: " + e.getMessage());
            return new JLabel();
        }
    }

    // MODIFIES: this
    // EFFECTS: if user has enough primogems, makes wish(es) from the chosen banner,
    //          adds them all to inventory, then displays the results
    public void makeWish(int count) {
        int cost = PRIMOGEMS_PER_WISH * count;
        removePrimogems(cost);

        List<Wish> wishes = this.currentBanner.makeWish(count);
        for (Wish wish : wishes) {
            this.inventory.addWish(wish);
        }

        switchToWishAnimation(wishes);
    }

    private boolean removePrimogems(int cost) {
        boolean success = true;
        try {
            this.inventory.removePrimogems(cost);
        } catch (NotEnoughPrimosException e) {
            // TODO: show popup saying "not enough primogems"!
            success = false;
        }

        if (success) {
            this.bannerMenu.updatePrimogems(this.inventory.getPrimogems());
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
    // EFFECTS: switches page to nextPage and displays it
    private void switchPage(Page nextPage) {
        currentPage = nextPage;
        cards.show(wishSim, nextPage.getPageId());
        repaint();
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
