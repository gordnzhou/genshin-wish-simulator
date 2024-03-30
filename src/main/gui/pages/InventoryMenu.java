package gui.pages;

import gui.WishSim;
import gui.components.InventoryEntry;
import gui.components.StyledButton;
import model.Inventory;
import model.wish.Wish;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class InventoryMenu extends Page implements ActionListener {

    private static final String PAGE_ID = "InventoryMenu";

    private JPanel inventoryPanel;
    private JPanel buttonPanel;
    private JButton exitButton;
    private JButton filterButton;

    private int minRarity;
    private List<InventoryEntry> wishEntries;

    public InventoryMenu(WishSim wishSim) {
        super(wishSim, PAGE_ID, MENU_BACKGROUND_PATH);
        super.page.setLayout(new BorderLayout());
        this.minRarity = 3;
        this.wishEntries = new ArrayList<>();
        initInventoryPanel();
        initButtonPanel();
    }

    // MODIFIES: this
    // EFFECTS: Displays wishes in inventory on GUI
    public void onPageSwitch(Inventory inventory) {
        inventoryPanel.removeAll();
        wishEntries = new ArrayList<>();

        for (Wish wish : inventory.getWishes()) {
            int copies = inventory.getWishCopies(wish);
            InventoryEntry entry = new InventoryEntry(wish, copies);
            entry.addEntry(wishEntries, inventoryPanel);
        }
        filterByRarity();
    }

    // MODIFIES: this
    // EFFECTS: creates inventory panel in the middle
    private void initInventoryPanel() {
        inventoryPanel = new JPanel();
        inventoryPanel.setLayout(new BoxLayout(inventoryPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("<html><u><b><font size=+3>Your Inventory</b></u></html>\"");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        super.page.add(titleLabel, BorderLayout.NORTH);
        super.page.add(inventoryPanel, BorderLayout.CENTER);
    }

    // MODIFIES: this
    // EFFECTS: creates button panel in the bottom
    private void initButtonPanel() {
        buttonPanel = new JPanel();
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
        for (InventoryEntry entry : wishEntries) {
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
        this.minRarity += 1;
        if (this.minRarity > 5) {
            this.minRarity = 3;
        }

        filterByRarity();
        filterButton.setText("Filter by Lowest Rarity: " + minRarity);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exitButton) {
            super.wishSim.switchToBannerMenu();
        } else if (e.getSource() == filterButton) {
            updateMinRarity();
        }
    }
}
