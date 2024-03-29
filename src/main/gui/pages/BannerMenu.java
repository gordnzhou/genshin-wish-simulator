package gui.pages;

import gui.WishSim;
import gui.components.PrimogemCounter;
import gui.components.WishButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static gui.WishSim.*;

public class BannerMenu extends Page implements ActionListener {

    public static final String BANNER_IMAGE_PATH = "data/static/images/wanderlust-invocation.png";
    public static final String PAGE_ID = "bannerMenu";

    WishButton singleWishButton;
    WishButton multipleWishButton;
    PrimogemCounter primogemCounter;

    public BannerMenu(WishSim wishSim, int primogems) {
        super(wishSim, PAGE_ID);
        super.page.setLayout(new BorderLayout());
        initWishButtons();
        initNorthPanel(primogems);
        initBannerImage();
    }


    // MODIFIES: this
    // EFFECTS: initializes JPanel to be displayed at the top
    private void initNorthPanel(int primogems) {
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BorderLayout());
        initBannerButtons(northPanel);
        initPrimogemCounter(northPanel, primogems);
        super.page.add(northPanel, BorderLayout.NORTH);
    }

    // MODIFIES: this
    // EFFECTS: initializes Banner Buttons and adds them to parent
    private void initPrimogemCounter(JPanel parent, int primogems) {
        JPanel primogemCounterPanel = new JPanel();
        primogemCounter = new PrimogemCounter(primogemCounterPanel);
        primogemCounter.updateCount(primogems);
        parent.add(primogemCounterPanel, BorderLayout.EAST);
    }

    // MODIFIES: this
    // EFFECTS: initializes Banner Buttons and adds them to parent
    private void initBannerButtons(JPanel parent) {
        JPanel bannerButtonArea = new JPanel();
        bannerButtonArea.setLayout(new GridLayout(1, 2));
        bannerButtonArea.setBorder(new EmptyBorder(0, 40, 10, 40));
        bannerButtonArea.setSize(WIDTH / 2, HEIGHT / 5);

        WishButton eventBannerButton = new WishButton("Event");
        bannerButtonArea.add(eventBannerButton);

        WishButton standardBannerButton = new WishButton("Standard");
        bannerButtonArea.add(standardBannerButton);

        parent.add(bannerButtonArea, BorderLayout.CENTER);
    }

    // MODIFIES: this
    // EFFECTS: loads bannerImage from file and adds it to this page
    private void initBannerImage() {
        JPanel bannerImageArea = new JPanel();
        bannerImageArea.setBorder(new EmptyBorder(10, 20, 0, 20));
        JLabel bannerLabel = loadImageFromPath(BANNER_IMAGE_PATH, 0.8);
        bannerImageArea.add(bannerLabel);
        super.page.add(bannerImageArea, BorderLayout.CENTER);
    }

    // MODIFIES: this
    // EFFECTS: instantiates all tools to the bottom of the page
    private void initWishButtons() {
        JPanel wishButtonArea = new JPanel();
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == multipleWishButton) {
            super.wishSim.makeWish(10);
        } else if (e.getSource() == singleWishButton) {
            super.wishSim.makeWish(1);
        }
    }

    // MODIFIES: this
    // EFFECTS: updates PrimogemCounter to reflect primogems
    public void updatePrimogems(int primogems) {
        primogemCounter.updateCount(primogems);
    }
}
