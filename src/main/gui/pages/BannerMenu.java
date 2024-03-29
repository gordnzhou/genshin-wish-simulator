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
        super(wishSim, PAGE_ID);
        super.page.setLayout(new BorderLayout());
        bannerDisplay = new BannerDisplay(super.page);
        initWishButtons();
        initWestPanel();
        initNorthPanel(primogems);
    }

    // MODIFIES: this
    // EFFECTS: initializes buttons in the left panel
    private void initWestPanel() {
        JPanel westPanel = new JPanel();
        westPanel.setOpaque(false);
        westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));

        inventoryButton = new StyledButton("View Inventory");
        westPanel.add(inventoryButton);
        inventoryButton.addActionListener(this);

        saveButton = new StyledButton("Save Inventory");
        westPanel.add(saveButton);
        saveButton.addActionListener(this);

        loadButton = new StyledButton("Load Inventory");
        westPanel.add(loadButton);
        loadButton.addActionListener(this);

        super.page.add(westPanel, BorderLayout.WEST);
    }

    // MODIFIES: this
    // EFFECTS: initializes JPanel to be displayed at the top
    private void initNorthPanel(int primogems) {
        JPanel northPanel = new JPanel();
        northPanel.setOpaque(false);
        northPanel.setLayout(new BorderLayout());
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
        bannerButtonArea.setLayout(new GridLayout(1, 2));
        bannerButtonArea.setBorder(new EmptyBorder(0, 40, 10, 40));
        bannerButtonArea.setSize(WIDTH / 2, HEIGHT / 5);

        eventBannerButton = new StyledButton("Event Banner");
        bannerButtonArea.add(eventBannerButton);
        eventBannerButton.addActionListener(this);

        standardBannerButton = new StyledButton("Standard Banner");
        bannerButtonArea.add(standardBannerButton);
        standardBannerButton.addActionListener(this);

        parent.add(bannerButtonArea, BorderLayout.CENTER);
    }

    // MODIFIES: this
    // EFFECTS: instantiates all tools to the bottom of the page
    private void initWishButtons() {
        JPanel wishButtonArea = new JPanel();
        wishButtonArea.setOpaque(false);
        wishButtonArea.setLayout(new GridLayout(0, 2));
        wishButtonArea.setBorder(new EmptyBorder(0, 40, 10, 40));
        wishButtonArea.setSize(WIDTH / 2, HEIGHT / 5);

        singleWishButton = new WishButton("Wish x1");
        wishButtonArea.add(singleWishButton);
        singleWishButton.addActionListener(this);

        multipleWishButton = new WishButton("Wish x10");
        wishButtonArea.add(multipleWishButton);
        multipleWishButton.addActionListener(this);

        super.page.add(wishButtonArea, BorderLayout.SOUTH);
    }

    // MODIFIES: this
    // EFFECTS: updates PrimogemCounter to reflect primogems
    public void updatePrimogems(int primogems) {
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
