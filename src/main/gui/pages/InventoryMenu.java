package gui.pages;

import gui.WishSim;
import gui.components.InventoryEntry;
import gui.components.StyledButton;
import model.Inventory;
import model.wish.Character;
import model.wish.Wish;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.*;

import static gui.WishSim.*;
import static gui.components.InventoryEntry.ENTRY_SIZE;

public class InventoryMenu extends Page implements ActionListener {
    private static InventoryMenu inventoryMenu;
    private static final String PAGE_ID = "InventoryMenu";

    private JPanel inventoryPanel;
    private JButton exitButton;
    private JButton filterButton;

    private int minRarity;
    private final Map<String, InventoryEntry> wishEntries;

    private InventoryMenu() {
        super(PAGE_ID, MENU_BACKGROUND_PATH);
        super.page.setLayout(new BorderLayout());
        this.minRarity = 3;
        this.wishEntries = new HashMap<>();
        initInventoryPanel();
        initButtonPanel();
    }

    // EFFECTS: returns this wishSim instance
    public static InventoryMenu getInstance() {
        if (inventoryMenu == null) {
            inventoryMenu = new InventoryMenu();
        }
        return inventoryMenu;
    }

    // MODIFIES: this
    // EFFECTS: creates scrollable inventory panel in the middle
    private void initInventoryPanel() {
        inventoryPanel = new JPanel();
        inventoryPanel.setBackground(new Color(226, 224, 214));
        inventoryPanel.setPreferredSize(new Dimension(WIDTH - 20, 5000));

        JScrollPane scrollPane = new JScrollPane(inventoryPanel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);

        JLabel titleLabel = new JLabel(
                "<html><center><font color='#FFFFFF' size='20'><u>Your Inventory</u></font></center></html>");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        super.page.add(scrollPane, BorderLayout.CENTER);
        super.page.add(titleLabel, BorderLayout.NORTH);
    }


    // MODIFIES: this
    // EFFECTS: Displays wishes in inventory on GUI
    public void onPageSwitch(Inventory inventory) {
        for (Wish wish : inventory.getWishes()) {
            String entryKey = wish.getName();
            int copies = inventory.getWishCopies(wish);

            if (wishEntries.containsKey(entryKey)) {
                wishEntries.get(entryKey).updateCopies(copies);
            } else {
                ImageIcon wishIcon = createWishIcon(wish);
                InventoryEntry entry = new InventoryEntry(wish, copies, wishIcon);
                wishEntries.put(entryKey, entry);
                inventoryPanel.add(entry.getEntryPanel());
            }
        }
        filterByRarity();
    }

    // MODIFIES: this
    // EFFECTS: returns wish icon for given wish's entry
    private ImageIcon createWishIcon(Wish wish) {
        if (wish instanceof Character) {
            return WishSim.getInstance().getFaceImage(wish);
        } else {
            Image gachaImage = WishSim.getInstance().getGachaImage(wish).getImage();
            gachaImage = gachaImage.getScaledInstance(gachaImage.getWidth(null) / 2, gachaImage.getHeight(null) / 2,
                    Image.SCALE_SMOOTH);
            BufferedImage croppedImage = new BufferedImage(ENTRY_SIZE, ENTRY_SIZE, BufferedImage.TYPE_INT_ARGB);
            int x = (gachaImage.getWidth(null) - ENTRY_SIZE) / 2;
            int y = (gachaImage.getHeight(null) - ENTRY_SIZE) / 2;
            Graphics2D g2d = croppedImage.createGraphics();
            g2d.drawImage(gachaImage, 0, 0, ENTRY_SIZE, ENTRY_SIZE, x, y,
                    x + ENTRY_SIZE, y + ENTRY_SIZE, null);
            return new ImageIcon(croppedImage);
        }
    }

    // MODIFIES: this
    // EFFECTS: creates button panel in the bottom
    private void initButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);

        exitButton = new StyledButton("Back to Banners");
        buttonPanel.add(exitButton);
        exitButton.addActionListener(this);

        filterButton = new StyledButton("Filter by Lowest Rarity: " + minRarity);
        buttonPanel.add(filterButton);
        filterButton.addActionListener(this);

        super.page.add(buttonPanel, BorderLayout.SOUTH);
    }

    // MODIFIES: this
    // EFFECTS: hides all wish entries have rarity < minRarity in GUI
    private void filterByRarity() {
        for (InventoryEntry entry : wishEntries.values()) {
            if (entry.getRarity() < minRarity) {
                entry.hideLabel();
            } else {
                entry.showLabel();
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: updates minRarity button in GUI
    private void updateMinRarity() {
        minRarity += 1;
        if (minRarity > 5) {
            minRarity = 3;
        }

        filterByRarity();
        filterButton.setText("Filter by Lowest Rarity: " + minRarity);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exitButton) {
            WishSim.getInstance().switchToBannerMenu();
        } else if (e.getSource() == filterButton) {
            updateMinRarity();
        }
    }
}
