package gui.pages;


import gui.WishSim;
import model.wish.Wish;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static gui.WishSim.HEIGHT;
import static gui.WishSim.WIDTH;
import static java.lang.Thread.sleep;

public class WishAnimation extends Page {
    private static final String SINGLE_5_WISH_PATH = "data/static/animations/wish-5-single.gif";
    private static final String MULTI_5_WISH_PATH = "data/static/animations/wish-5-multi.gif";
    private static final String SINGLE_4_WISH_PATH = "data/static/animations/wish-4-single.gif";
    private static final String MULTI_4_WISH_PATH = "data/static/animations/wish-4-multi.gif";
    private static final String SINGLE_3_WISH_PATH = "data/static/animations/wish-3-single.gif";

    public static final int WISH_ANIMATION_MILLIS = 7000;
    private static final String PAGE_ID = "wishAnimation";

    private List<Wish> wishes;
    private Thread timeoutThread;
    private JLabel imageLabel;

    public WishAnimation(WishSim wishSim) {
        super(wishSim, PAGE_ID);
        wishes = new ArrayList<>();
        imageLabel = new JLabel();
        super.page.add(imageLabel);
    }

    @Override
    public void handleMousePressed() {
        super.wishSim.switchToWishResult(wishes);
        timeoutThread.stop();
    }

    // MODIFIES: this
    // EFFECTS: starts wish animation corresponding to wishes
    public void onPageSwitch(List<Wish> wishes) {
        this.wishes = wishes;
        String wishAnimationPath = getWishAnimationPath();
        doWishAnimation(wishAnimationPath);
    }

    // EFFECTS: returns the highest star rarity in wishes
    private int getMaxRarity() {
        int result = 3;
        for (Wish wish : this.wishes) {
            if (wish.getRarity() > result) {
                result = wish.getRarity();
            }
        }
        return result;
    }

    // EFFECTS: returns the file path for wishes' corresponding animation
    //          based on the number of wishes made and the highest rarity wish
    private String getWishAnimationPath() {
        int wishCount = this.wishes.size();
        int maxRarity = this.getMaxRarity();

        switch (maxRarity) {
            case 3:
                return SINGLE_3_WISH_PATH;
            case 4:
                if (wishCount == 1) {
                    return SINGLE_4_WISH_PATH;
                } else {
                    return MULTI_4_WISH_PATH;
                }
            case 5:
                if (wishCount == 1) {
                    return SINGLE_5_WISH_PATH;
                } else {
                    return MULTI_5_WISH_PATH;
                }
        }

        return "";
    }

    // MODIFIES: this
    // EFFECTS: displays GIF from wishAnimationPath; automatically
    //          switches to wishResult after WISH_ANIMATION_MILLIS has passed
    private void doWishAnimation(String wishAnimationPath) {
        ImageIcon icon = new ImageIcon(wishAnimationPath);
        icon.setImage(icon.getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT));
        imageLabel.setIcon(icon);
        System.out.println(wishAnimationPath);

        timeoutThread = new Thread(() -> {
            try {
                sleep(WISH_ANIMATION_MILLIS);
            } catch (InterruptedException e) {
                System.out.println("Error Sleeping: " + e.getMessage());
            }

            super.wishSim.switchToWishResult(wishes);
        });
        timeoutThread.start();
    }
}
