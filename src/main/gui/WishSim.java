package gui;

import exceptions.NotEnoughPrimosException;
import gui.pages.*;
import model.Event;
import model.EventLog;
import model.Inventory;
import model.banner.Banner;
import model.wish.Character;
import model.wish.Wish;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gui.components.InventoryEntry.ENTRY_SIZE;

public class WishSim extends JFrame {
    public static final int STARTING_PRIMOGEMS = 5000;
    public static final int WIDTH = 1200;
    public static final int HEIGHT = 800;
    public static final int PRIMOGEMS_PER_WISH = 160;

    private static final String JSON_STORE = "./data/inventory.json";
    private static final String STANDARD_BANNER_JSON_PATH = "./data/static/standard_banner.json";
    private static final String EVENT_BANNER_JSON_PATH = "./data/static/event_banner.json";
    private static final String FONT_PATH = "data/static/fonts/genshin-font.ttf";
    private static final String GACHA_SPLASH_PATH = "data/static/images/gacha-splashes/";
    private static final String CHARACTER_FACES_PATH = "data/static/images/character-faces/";

    private static WishSim wishSim;
    private Banner standardBanner;
    private Banner eventBanner;
    private Banner currentBanner;
    private Inventory inventory;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private CardLayout cards;
    private JPanel wishSimPanel;
    private Page currentPage;

    private Map<String, ImageIcon> gachaImageCache;
    private Map<String, ImageIcon> faceImageCache;

    public static void main(String[] args) {
        wishSim = new WishSim();
        wishSim.initGui();
    }

    // EFFECTS: returns this wishSim instance
    public static WishSim getInstance() {
        return wishSim;
    }

    // EFFECTS: initializes field and font for wishSim
    private WishSim() {
        super("Genshin Wish Simulator");
        initializeFields();
        initializeFont();
    }

    // MODIFIES: this
    // EFFECTS: initializes internal field values
    private void initializeFields() {
        this.standardBanner = loadBannerFromPath(STANDARD_BANNER_JSON_PATH);
        this.eventBanner = loadBannerFromPath(EVENT_BANNER_JSON_PATH);
        this.currentBanner = this.eventBanner;
        this.inventory = new Inventory(new HashMap<>(), STARTING_PRIMOGEMS);
        this.jsonReader = new JsonReader(JSON_STORE);
        this.jsonWriter = new JsonWriter(JSON_STORE);
        this.gachaImageCache = new HashMap<>();
        this.faceImageCache = new HashMap<>();
    }

    // MODIFIES: this
    // EFFECTS: initializes Genshin font and sets it as default font
    private void initializeFont() {
        try {
            Font genshinFont = Font.createFont(Font.TRUETYPE_FONT, new File(FONT_PATH));
            genshinFont = genshinFont.deriveFont(Font.PLAIN, 14);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(genshinFont);
            UIManager.put("Button.font", genshinFont);
            UIManager.put("Label.font", genshinFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    // MODIFIES: this
    // EFFECTS: starts GUI for WishSim
    private void initGui() {
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                for (Event event : EventLog.getInstance()) {
                    System.out.println(event.toString() + "\n");
                }
                EventLog.getInstance().clear();
                dispose();
            }
        });
        setUndecorated(false);
        initPages();
        WishMouseListener wml = new WishMouseListener();
        addMouseListener(wml);
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: initializes all GUI pages
    private void initPages() {
        cards = new CardLayout();
        wishSimPanel = new JPanel(cards);
        setContentPane(wishSimPanel);
        inventory.addObserver(BannerMenu.getInstance().getMultipleWishButton());
        inventory.addObserver(BannerMenu.getInstance().getSingleWishButton());
        inventory.addObserver(BannerMenu.getInstance().getPrimogemCounter());
        inventory.addPrimogems(0);
        currentPage = BannerMenu.getInstance();
    }

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
    // EFFECTS: if user has enough primogems, makes wish(es) from the chosen banner,
    //          adds them all to inventory, then displays the results
    public void makeWish(int count) {
        int cost = PRIMOGEMS_PER_WISH * count;

        if (!removePrimogems(cost)) {
            return;
        }

        List<Wish> wishes = currentBanner.makeWish(count);
        for (Wish wish : wishes) {
            inventory.addWish(wish);
        }

        switchToWishAnimation(wishes);
    }

    // MODIFIES: this
    // EFFECTS: deducts cost from inventory primogems and returns true;
    //          if not enough primogems, return false and show error popup
    private boolean removePrimogems(int cost) {
        boolean success = true;
        try {
            inventory.removePrimogems(cost);
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
        switchPage(BannerMenu.getInstance());
    }

    // MODIFIES: this
    // EFFECTS: switches currentPage to wishAnimation after
    //          making a wish in bannerMenu
    private void switchToWishAnimation(List<Wish> wishes) {
        switchPage(WishAnimation.getInstance());
        WishAnimation.getInstance().onPageSwitch(wishes);
    }

    // MODIFIES: this
    // EFFECTS: switches to wishResult after wishAnimation
    public void switchToWishResult(List<Wish> wishes) {
        switchPage(WishResult.getInstance());
        WishResult.getInstance().onPageSwitch(wishes);
    }

    // MODIFIES: this
    // EFFECTS: switches currentPage to InventoryMenu
    public void switchToInventoryMenu() {
        switchPage(InventoryMenu.getInstance());
        InventoryMenu.getInstance().onPageSwitch(inventory);
    }

    // MODIFIES: this
    // EFFECTS: switches currentBanner to banner
    public void switchToStandardBanner() {
        if (currentBanner == standardBanner) {
            return;
        }
        currentBanner = standardBanner;
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
        cards.show(wishSimPanel, nextPage.getPageId());
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
    }

    // MODIFIES: this
    // EFFECTS: loads inventory from JSON
    public void loadInventory() {
        try {
            inventory = jsonReader.readInventory();
            inventory.addObserver(BannerMenu.getInstance().getMultipleWishButton());
            inventory.addObserver(BannerMenu.getInstance().getSingleWishButton());
            inventory.addObserver(BannerMenu.getInstance().getPrimogemCounter());
            inventory.addPrimogems(0);
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

    // MODIFIES: this
    // EFFECTS: returns the cached gacha image for wish; if not cached,
    //          generates a new gacha image and adds it to cache
    public ImageIcon getGachaImage(Wish wish) {
        String wishName = wish.getName();
        if (gachaImageCache.containsKey(wishName)) {
            return gachaImageCache.get(wishName);
        }

        String wishImagePath = GACHA_SPLASH_PATH + wish.getName()
                .toLowerCase()
                .replaceAll("\\s", "-")
                .replaceAll("'", "_") + ".png";

        ImageIcon wishImageIcon;
        if (wish instanceof Character) {
            wishImageIcon = loadImageFromPath(wishImagePath, 800, 800);
        } else {
            wishImageIcon = loadImageFromPath(wishImagePath, 0.77);
            assert wishImageIcon != null;
            if (wishImageIcon.getIconHeight() < 300 & wishImageIcon.getIconWidth() < 300) {
                wishImageIcon = loadImageFromPath(wishImagePath, 1.5);
            }
        }
        gachaImageCache.put(wishName, wishImageIcon);

        return wishImageIcon;
    }

    // REQUIRES: wishName is a name of a CHARACTER
    // MODIFIES: this
    // EFFECTS: returns the cached face image for wish; if not cached,
    //          generates a new face image and adds it to cache
    public ImageIcon getFaceImage(Wish wish) {
        String wishName = wish.getName();
        if (faceImageCache.containsKey(wishName)) {
            return faceImageCache.get(wishName);
        }

        String faceImagePath = CHARACTER_FACES_PATH + wish.getName()
                .toLowerCase()
                .replaceAll("\\s", "-")
                .replaceAll("'", "_") + ".png";
        ImageIcon faceImageIcon = loadImageFromPath(faceImagePath, ENTRY_SIZE, ENTRY_SIZE);
        faceImageCache.put(wishName, faceImageIcon);
        return faceImageIcon;
    }


    // EFFECTS: loads path's image with given width and height; returns a JLabel containing the scaled image
    // or an empty JLabel if there was an error reading the image
    public static ImageIcon loadImageFromPath(String path, int width, int height) {
        try {
            BufferedImage image = ImageIO.read(new File(path));
            Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (IOException e) {
            System.out.println("Error Reading Image from '" + path + "' : " + e.getMessage());
            return null;
        }
    }

    // EFFECTS: loads path's image and scales it; returns a JLabel containing the scaled image
    // or an empty JLabel if there was an error reading the image
    public static ImageIcon loadImageFromPath(String path, double scale) {
        try {
            BufferedImage image = ImageIO.read(new File(path));
            int scaledWidth = (int) (image.getWidth() * scale);
            int scaledHeight = (int) (image.getHeight() * scale);
            Image scaledImage = image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (IOException e) {
            System.out.println("Error Reading Image from '" + path + "' : " + e.getMessage());
            return null;
        }
    }

    private void handleMousePressed() {
        currentPage.handleMousePressed();
    }

    private class WishMouseListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            handleMousePressed();
        }
    }
}
