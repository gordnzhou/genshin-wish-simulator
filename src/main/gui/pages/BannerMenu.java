package gui.pages;

import gui.WishSim;
import gui.components.BannerDisplay;
import gui.components.PrimogemCounter;
import gui.components.StyledButton;
import gui.components.WishButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static gui.WishSim.*;

public class BannerMenu extends Page implements ActionListener {
    private static final String PAGE_ID = "bannerMenu";

    private static final String WISH_LOGO_PATH = "data/static/images/wish-logo.png";

    BannerDisplay bannerDisplay;
    WishButton singleWishButton;
    WishButton multipleWishButton;
    PrimogemCounter primogemCounter;
    StyledButton inventoryButton;
    StyledButton saveButton;
    StyledButton loadButton;
    StyledButton standardBannerButton;
    StyledButton eventBannerButton;

    public BannerMenu(WishSim wishSim, int primogems) {
        super(wishSim, PAGE_ID, MENU_BACKGROUND_PATH);
        super.page.setLayout(new BorderLayout());
        bannerDisplay = new BannerDisplay(super.page);
        initNorthPanel(primogems);
        initBottomPanel();
    }

    // MODIFIES: this
    // EFFECTS: initializes JPanel to be displayed at the top
    private void initNorthPanel(int primogems) {
        JPanel northPanel = new JPanel();
        northPanel.setOpaque(false);
        northPanel.setLayout(new BorderLayout());

        JPanel topLeftPanel = new JPanel();
        topLeftPanel.setOpaque(false);
        JLabel wishLogoLabel = new JLabel();
        wishLogoLabel.setIcon(loadImageFromPath(WISH_LOGO_PATH, 0.7));
        topLeftPanel.add(wishLogoLabel);
        topLeftPanel.add(new JLabel("<html><font color='#FFFFFF'>Genshin Wish Simulator<</font></html>"));
        northPanel.add(topLeftPanel, BorderLayout.WEST);

        initBannerButtons(northPanel);
        initPrimogemCounter(northPanel, primogems);
        super.page.add(northPanel, BorderLayout.NORTH);
    }

    // MODIFIES: this
    // EFFECTS: initializes Banner Buttons and adds them to parent
    private void initPrimogemCounter(JPanel parent, int primogems) {
        JPanel primogemCounterPanel = new JPanel();
        primogemCounterPanel.setOpaque(false);
        primogemCounter = new PrimogemCounter(primogemCounterPanel, super.wishSim);
        primogemCounter.updateCount(primogems);
        parent.add(primogemCounterPanel, BorderLayout.EAST);
    }

    // MODIFIES: this
    // EFFECTS: initializes Banner Buttons and adds them to parent
    private void initBannerButtons(JPanel parent) {
        JPanel bannerButtonArea = new JPanel();
        bannerButtonArea.setOpaque(false);
        bannerButtonArea.setBorder(new EmptyBorder(0, 40, 10, 40));

        eventBannerButton = new StyledButton("Event Banner");
        bannerButtonArea.add(eventBannerButton);
        eventBannerButton.addActionListener(this);

        standardBannerButton = new StyledButton("Standard Banner");
        bannerButtonArea.add(standardBannerButton);
        standardBannerButton.addActionListener(this);

        parent.add(bannerButtonArea, BorderLayout.CENTER);
    }

    // MODIFIES: this
    // EFFECTS: creates bottom panel and adds it to this page
    private void initBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        initWishButtons(bottomPanel);
        initBottomLeftPanel(bottomPanel);
        super.page.add(bottomPanel, BorderLayout.SOUTH);
    }

    // MODIFIES: this
    // EFFECTS: creates wish buttons and adds it to parent at the right
    private void initWishButtons(JPanel parent) {
        JPanel wishButtonArea = new JPanel();
        wishButtonArea.setOpaque(false);
        wishButtonArea.setBorder(new EmptyBorder(0, 40, 10, 40));

        singleWishButton = new WishButton(1);
        wishButtonArea.add(singleWishButton);
        singleWishButton.addActionListener(this);

        multipleWishButton = new WishButton(10);
        wishButtonArea.add(multipleWishButton);
        multipleWishButton.addActionListener(this);

        parent.add(wishButtonArea, BorderLayout.EAST);
    }

    // MODIFIES: this
    // EFFECTS: initializes inventory buttons and adds it to left of parent panel
    private void initBottomLeftPanel(JPanel parent) {
        JPanel bottomLeftPanel = new JPanel();
        bottomLeftPanel.setBorder(new EmptyBorder(50, 0, 0, 0));
        bottomLeftPanel.setOpaque(false);

        inventoryButton = new StyledButton("View Inventory");
        bottomLeftPanel.add(inventoryButton);
        inventoryButton.addActionListener(this);

        saveButton = new StyledButton("Save Inventory");
        bottomLeftPanel.add(saveButton);
        saveButton.addActionListener(this);

        loadButton = new StyledButton("Load Inventory");
        bottomLeftPanel.add(loadButton);
        loadButton.addActionListener(this);

        parent.add(bottomLeftPanel, BorderLayout.WEST);
    }

    // MODIFIES: this
    // EFFECTS: updates PrimogemCounter to reflect primogems
    public void updatePrimogems(int primogems) {
        singleWishButton.updatePrimogems(primogems);
        multipleWishButton.updatePrimogems(primogems);
        primogemCounter.updateCount(primogems);
    }

    // MODIFIES: this
    // EFFECTS: switches banner to standardBanner
    public void switchToStandardBanner() {
        bannerDisplay.switchToStandardBanner();
        super.wishSim.switchToStandardBanner();
    }

    // MODIFIES: this
    // EFFECTS: switches banner to eventBanner and displays it
    public void switchToEventBanner() {
        bannerDisplay.switchToEventBanner();
        super.wishSim.switchToEventBanner();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == multipleWishButton) {
            super.wishSim.makeWish(10);
        } else if (e.getSource() == singleWishButton) {
            super.wishSim.makeWish(1);
        } else if (e.getSource() == saveButton) {
            super.wishSim.saveInventory();
        } else if (e.getSource() == loadButton) {
            super.wishSim.loadInventory();
        } else if (e.getSource() == inventoryButton) {
            super.wishSim.switchToInventoryMenu();
        } else if (e.getSource() == standardBannerButton) {
            switchToStandardBanner();
        } else if (e.getSource() == eventBannerButton) {
            switchToEventBanner();
        }
    }
}
