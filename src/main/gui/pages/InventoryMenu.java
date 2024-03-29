package gui.pages;

import gui.WishSim;
import gui.components.StyledButton;
import model.Inventory;
import model.wish.Character;
import model.wish.Weapon;
import model.wish.Wish;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InventoryMenu extends Page implements ActionListener {

    private static final String PAGE_ID = "InventoryMenu";

    private JPanel inventoryPanel;
    private JPanel buttonPanel;

    private JButton exitButton;

    public InventoryMenu(WishSim wishSim) {
        super(wishSim, PAGE_ID);
        super.page.setLayout(new BorderLayout());
        initInventoryPanel();
        initButtonPanel();
    }

    // TODO:
    // MODIFIES: this
    // EFFECTS: Displays wishes in inventory on GUI
    public void onPageSwitch(Inventory inventory) {
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

    private void initInventoryPanel() {
        inventoryPanel = new JPanel();
        super.page.add(inventoryPanel, BorderLayout.CENTER);
    }

    private void initButtonPanel() {
        buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);

        exitButton = new StyledButton("Back to Banners");
        buttonPanel.add(exitButton);
        exitButton.addActionListener(this);

        super.page.add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exitButton) {
            super.wishSim.switchToBannerMenu();
        }
    }
}
