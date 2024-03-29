package gui.pages;

import gui.WishSim;
import gui.components.WishButton;
import model.wish.Wish;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import static gui.WishSim.*;

public class BannerMenu extends Page implements ActionListener {

    public static final String BANNER_IMAGE_PATH = "data/static/images/wanderlust-invocation.png";
    public static final String PAGE_ID = "bannerMenu";

    WishButton singleWishButton;
    WishButton multipleWishButton;

    public BannerMenu(WishSim wishSim) {
        super(wishSim, PAGE_ID);
        super.page.setLayout(new BorderLayout());
        displayBannerMenu();
    }

    @Override
    public void handleMousePressed() {

    }

    @Override
    public void onPageSwitch(List<Wish> wishes) {

    }

    // MODIFIES: this
    // EFFECTS: changes window to display banner menu
    private void displayBannerMenu() {
        initWishButtons();
        initBannerButtons();
        initBannerImage();
    }

    private void initBannerButtons() {
        JPanel bannerButtonArea = new JPanel();
        bannerButtonArea.setLayout(new GridLayout(0, 2));
        bannerButtonArea.setBorder(new EmptyBorder(0, 40, 10, 40));
        bannerButtonArea.setSize(WIDTH / 2, HEIGHT / 5);

        WishButton eventBannerButton = new WishButton("Event");
        bannerButtonArea.add(eventBannerButton);

        WishButton standardBannerButton = new WishButton("Standard");
        bannerButtonArea.add(standardBannerButton);

        super.page.add(bannerButtonArea, BorderLayout.NORTH);
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
}
