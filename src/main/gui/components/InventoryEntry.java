package gui.components;

import model.wish.Weapon;
import model.wish.Wish;

import javax.swing.*;

import java.awt.*;

import static gui.WishSim.loadImageFromPath;

public class InventoryEntry {
    private static final String STAR_BG_PATH = "data/static/images/backgrounds/";
    private static final String STAR_ICON_PATH = "data/static/images/star-icon.png";

    public static final int ENTRY_SIZE = 192;

    private JPanel entryPanel;
    private final Wish wish;
    private int copies;

    // MODIFIES: this, wishEntries, inventoryPanel
    // EFFECTS: constructs InventoryEntry from wish and creates panel card for wish;
    //          adds this panel to inventoryPanel and this to wishEntries
    public InventoryEntry(Wish wish, int copies, ImageIcon wishIcon) {
        this.wish = wish;
        this.copies = copies;
        initEntryPanel(wishIcon);
    }

    // MODIFIES: this
    // EFFECTS: initializes entryPanel
    private void initEntryPanel(ImageIcon wishIcon) {
        String backgroundImagePath = STAR_BG_PATH + wish.getRarity() + "star-bg.png";
        Image backgroundImage = loadImageFromPath(backgroundImagePath, ENTRY_SIZE, ENTRY_SIZE).getImage();
        entryPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, ENTRY_SIZE, ENTRY_SIZE, this);
                }
            }
        };
        entryPanel.setPreferredSize(new Dimension(ENTRY_SIZE, ENTRY_SIZE));
        entryPanel.setMaximumSize(new Dimension(ENTRY_SIZE, ENTRY_SIZE));
        entryPanel.setLayout(null);
        addPanelFooter();
        JLabel wishIconLabel = new JLabel(wishIcon);
        wishIconLabel.setBounds(0, 0, ENTRY_SIZE, ENTRY_SIZE);
        entryPanel.add(wishIconLabel);
    }

    // MODIFIES: this
    // EFFECTS: adds footer panel to this entryPanel
    private void addPanelFooter() {
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BorderLayout());

        String copiesString = "C" + Math.min(copies - 1, 6);
        if (wish instanceof Weapon) {
            copiesString = "R" + Math.min(copies, 5);
        }
        JLabel label = new JLabel(String.format("<html><font color='#54525E' size='4'><b>%s | %s</b></font></html>",
                wish.getName(), copiesString));
        label.setHorizontalTextPosition(SwingConstants.CENTER);
        footerPanel.add(label, BorderLayout.NORTH);

        JPanel rarityPanel = new JPanel();
        rarityPanel.setBorder(BorderFactory.createEmptyBorder(-5, 0, 0, 0));
        ImageIcon starIcon = loadImageFromPath(STAR_ICON_PATH, 0.6);
        for (int i = 0; i < wish.getRarity(); i++) {
            rarityPanel.add(new JLabel(starIcon));
        }
        footerPanel.add(rarityPanel, BorderLayout.SOUTH);

        footerPanel.setBounds(0, ENTRY_SIZE - 50, ENTRY_SIZE, 50);
        entryPanel.add(footerPanel, BorderLayout.SOUTH);
    }

    public void updateCopies(int copies) {
        this.copies = copies;
    }

    public int getRarity() {
        return wish.getRarity();
    }

    // MODIFIES: this
    // EFFECTS: shows this entry's label
    public void showLabel() {
        entryPanel.setVisible(true);
    }

    public JPanel getEntryPanel() {
        return entryPanel;
    }

    // MODIFIES: this
    // EFFECTS: hides this entry's label
    public void hideLabel() {
        entryPanel.setVisible(false);
    }
}
