package gui.components;

import model.Inventory;
import model.wish.Character;
import model.wish.Weapon;
import model.wish.Wish;

import javax.swing.*;
import java.util.List;
import java.util.Objects;

public class InventoryEntry {

    private JLabel entryLabel;
    private Wish wish;
    int copies;

    // EFFECTS: constructs InventoryEntry from wish and creates label corresponding to wish
    public InventoryEntry(Wish wish, int copies) {
        this.wish = wish;
        this.copies = copies;
        this.entryLabel = new JLabel();

        String name = wish.getName();
        int rarity = wish.getRarity();

        String text = "";
        if (wish instanceof Weapon) {
            text = String.format("- '%s' (%d stars), Weapon Type: %s, Copies: %d\n",
                    name, rarity, ((Weapon) wish).getWeaponType(), copies);
        } else if (wish instanceof Character) {
            text = String.format("- '%s' (%d stars), Vision: %s, Weapon: %s, Constellation(s): %d\n",
                    name, rarity, ((Character) wish).getVision(),
                    ((Character) wish).getWeapon(), Math.min(copies, 6));
        }

        entryLabel.setText(text);
    }

    public int getRarity() {
        return wish.getRarity();
    }

    // MODIFIES: this
    // EFFECTS: unhides this entry's label
    public void showLabel() {
        this.entryLabel.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: hides this entry's label
    public void hideLabel() {
        this.entryLabel.setVisible(false);
    }

    // MODIFIES: wishEntries, inventoryPanel
    // EFFECTS: adds this to wishEntries and inventoryPanel
    public void addEntry(List<InventoryEntry> wishEntries, JPanel inventoryPanel) {
        wishEntries.add(this);
        inventoryPanel.add(this.entryLabel);
    }
}
