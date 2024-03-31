package gui.pages;

import gui.WishSim;
import model.WeaponType;
import model.wish.Character;
import model.wish.Weapon;
import model.wish.Wish;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

import static gui.WishSim.loadImageFromPath;

public class WishResult extends Page implements ActionListener {
    private static final String GACHA_SPLASH_PATH = "data/static/images/gacha-splashes/";
    private static final String WEAPON_BG_PATH = "data/static/images/backgrounds/";
    private static final String BACKGROUND_IMAGE_PATH = "data/static/images/backgrounds/wish-background.jpeg";
    private static final String RESULT_CARD_PATH = "data/static/images/backgrounds/resultcard-bg.png";
    private static final double GACHA_SPLASH_SCALE = 0.25;

    public static final String PAGE_ID = "wishResult";

    private Map<String, ImageIcon> imageCache;
    private Map<WeaponType, ImageIcon> weaponBgCache;
    private ImageIcon resultCardIcon;
    private List<Wish> displayWishes;
    private CardLayout wishCards;
    private JPanel displayPanel;
    private JButton skipButton;
    private GridBagConstraints imageConstraints;
    private int currentPanelIndex;
    private int lastPanelIndex;

    public WishResult(WishSim wishSim, Set<Wish> allWishes) {
        super(wishSim, PAGE_ID, BACKGROUND_IMAGE_PATH);
        this.wishSim = wishSim;
        this.imageCache = new HashMap<>();
        this.weaponBgCache = new HashMap<>();
        this.displayWishes = new ArrayList<>();
        this.currentPanelIndex = 0;
        this.lastPanelIndex = 0;
        this.resultCardIcon = loadImageFromPath(RESULT_CARD_PATH, 0.6);
        this.imageConstraints = new GridBagConstraints();
        this.imageConstraints.gridx = 0;
        this.imageConstraints.gridy = 0;
        this.imageConstraints.anchor = GridBagConstraints.CENTER;
        this.imageConstraints.insets = new Insets(10, 10, 10, 10);
        this.loadAllWeaponBgImages();
        this.loadAllWishImages(allWishes);
        this.initDisplay();
    }

    // MODIFIES: this
    // EFFECTS: initializes GUI display and buttons
    private void initDisplay() {
        this.wishCards = new CardLayout();
        this.displayPanel = new JPanel(this.wishCards);
        displayPanel.setOpaque(false);

        JPanel skipButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        skipButtonPanel.setOpaque(false);
        skipButton = new JButton("<html><span style='font-size:16pt;'>Skip</span></html>");
        skipButton.setForeground(Color.WHITE);
        skipButton.setContentAreaFilled(false);
        skipButton.setBorderPainted(false);
        skipButton.setFocusPainted(false);
        skipButton.setOpaque(false);
        skipButton.addActionListener(this);
        skipButtonPanel.add(skipButton);

        super.page.setLayout(new BorderLayout());
        super.page.add(displayPanel, BorderLayout.CENTER);
        super.page.add(skipButtonPanel, BorderLayout.NORTH);
    }

    // MODIFIES: this
    // EFFECTS: loads every wish's image file and adds it to imageCache
    private void loadAllWishImages(Set<Wish> allWishes) {
        for (Wish wish : allWishes) {
            String cacheKey = wish.getName();
            String wishImagePath = GACHA_SPLASH_PATH + wish.getName()
                    .toLowerCase()
                    .replaceAll("\\s", "-")
                    .replaceAll("'", "_") + ".png";

            ImageIcon wishImageIcon;
            if (wish instanceof Character) {
                wishImageIcon = loadImageFromPath(wishImagePath, 800, 800);
            } else {
                wishImageIcon = loadImageFromPath(wishImagePath, 0.77);
            }

            System.out.format("Loaded image for %s with width: %d height: %d\n",
                    cacheKey, wishImageIcon.getIconWidth(), wishImageIcon.getIconHeight());
            imageCache.put(cacheKey, wishImageIcon);
        }
    }


    // MODIFIES: this
    // EFFECTS: loads all weapon background image files to weaponBgCache
    private void loadAllWeaponBgImages() {
        for (WeaponType weaponType : WeaponType.values()) {
            String weaponBgPath = WEAPON_BG_PATH + String.format("bg-%s.png", weaponType.name().toLowerCase());
            ImageIcon weaponBgIcon = loadImageFromPath(weaponBgPath, 0.7);

            System.out.format("Loaded image for %s with width: %d height: %d\n",
                    weaponType.name(), weaponBgIcon.getIconWidth(), weaponBgIcon.getIconHeight());
            weaponBgCache.put(weaponType, weaponBgIcon);
        }
    }

    @Override
    public void handleMousePressed() {
        if (currentPanelIndex == lastPanelIndex) {
            super.wishSim.switchToBannerMenu();
        } else {
            currentPanelIndex += 1;
            wishCards.next(displayPanel);

            if (currentPanelIndex == lastPanelIndex) {
                skipButton.setVisible(false);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (currentPanelIndex == lastPanelIndex) {
            super.wishSim.switchToBannerMenu();
        } else {
            skipButton.setVisible(false);
            currentPanelIndex = lastPanelIndex;
            wishCards.last(displayPanel);
        }
    }

    // MODIFIES: this
    // EFFECTS: displays wishes on screen
    public void onPageSwitch(List<Wish> wishes) {
        displayPanel.removeAll();
        displayWishes = wishes;
        skipButton.setVisible(wishes.size() != 1);
        currentPanelIndex = 0;
        lastPanelIndex = wishes.size() - 1;

        for (Wish wish : wishes) {
            addSinglePanel(wish);
        }

        if (wishes.size() != 1) {
            addSummaryPanel();
        }

        displayPanel.revalidate();
        displayPanel.repaint();
    }

    // MODIFIES: this
    // EFFECTS: creates a single panel for the given wish and adds it to the GUI
    private void addSinglePanel(Wish wish) {
        JPanel individualWishPanel = gradientPanel(wish.getRarity());
        individualWishPanel.setOpaque(false);
        individualWishPanel.setLayout(new GridBagLayout());

        ImageIcon wishIcon = imageCache.get(wish.getName());
        JLabel wishLabel = new JLabel(wishIcon);
        individualWishPanel.add(wishLabel, imageConstraints);

        if (wish instanceof Weapon) {
            ImageIcon weaponBgIcon = weaponBgCache.get(((Weapon) wish).getWeaponType());
            JLabel weaponBgLabel = new JLabel(weaponBgIcon);
            individualWishPanel.add(weaponBgLabel, imageConstraints);
        }

        displayPanel.add(individualWishPanel, BorderLayout.CENTER);
    }

    // MODIFIES: this
    // EFFECTS: creates a JPanel with circle gradient coloured based on rarity
    private JPanel gradientPanel(int rarity) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                float width = getWidth();
                float height = getHeight();

                Color centerColor = new Color(41, 75, 160);
                if (rarity == 4) {
                    centerColor = new Color(106, 53, 175);
                } else if (rarity == 5) {
                    centerColor = new Color(255, 216, 118);
                }

                RadialGradientPaint gradient = new RadialGradientPaint(
                        new Point2D.Float(width / 2, height / 2),
                        Math.min(width, height) / (float) 1.9,
                        new float[]{0.0f, 1.0f},
                        new Color[]{centerColor, new Color(0, 0, 0, 0)});
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, (int) width, (int) height);
            }
        };
    }

    // MODIFIES: this
    // EFFECTS: creates a summary panel of all wishes and adds it to the GUI
    private void addSummaryPanel() {
        JPanel outerPanel = new JPanel();
        outerPanel.setOpaque(false);
        outerPanel.setLayout(new BorderLayout());

        JPanel finalDisplayPanel = new JPanel();
        finalDisplayPanel.setOpaque(false);
        finalDisplayPanel.setLayout(new GridLayout(1, displayWishes.size()));
        for (Wish wish : displayWishes) {
            JPanel wishPanel = new JPanel(new GridBagLayout());
            wishPanel.setOpaque(false);

            ImageIcon wishIcon = imageCache.get(wish.getName());
            int scaledWidth = (int) (wishIcon.getIconWidth() * GACHA_SPLASH_SCALE);
            int scaledHeight = (int) (wishIcon.getIconHeight() * GACHA_SPLASH_SCALE);
            Image wishImage = wishIcon.getImage().getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
            JLabel wishImageLabel = new JLabel(new ImageIcon(wishImage));
            wishPanel.add(wishImageLabel, imageConstraints);

            JLabel resultCardLabel = new JLabel(resultCardIcon);
            wishPanel.add(resultCardLabel, imageConstraints);

            finalDisplayPanel.add(wishPanel);
        }

        outerPanel.add(finalDisplayPanel, BorderLayout.CENTER);
        displayPanel.add(outerPanel);
        lastPanelIndex += 1;
    }
}
