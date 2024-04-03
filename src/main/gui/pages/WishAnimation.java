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

    private static WishAnimation wishAnimation;
    private static final String PAGE_ID = "wishAnimation";

    private final JLabel imageLabel;
    private List<Wish> wishes;
    private Thread timeoutThread;

    private WishAnimation() {
        super(PAGE_ID, MENU_BACKGROUND_PATH);
        wishes = new ArrayList<>();
        imageLabel = new JLabel();
        super.page.setLayout(new GridLayout(1, 1));
        super.page.add(imageLabel);
    }

    // EFFECTS: returns this wishSim instance
    public static WishAnimation getInstance() {
        if (wishAnimation == null) {
            wishAnimation = new WishAnimation();
        }
        return wishAnimation;
    }

    @Override
    public void handleMousePressed() {
        WishSim.getInstance().switchToWishResult(wishes);
        timeoutThread.interrupt();
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
        for (Wish wish : wishes) {
            if (wish.getRarity() > result) {
                result = wish.getRarity();
            }
        }
        return result;
    }

    // EFFECTS: returns the file path for wishes' corresponding animation
    //          based on the number of wishes made and the highest rarity wish
    private String getWishAnimationPath() {
        switch (getMaxRarity()) {
            case 3:
                return SINGLE_3_WISH_PATH;
            case 4:
                if (wishes.size() == 1) {
                    return SINGLE_4_WISH_PATH;
                } else {
                    return MULTI_4_WISH_PATH;
                }
            case 5:
                if (wishes.size() == 1) {
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

        timeoutThread = new Thread(() -> {
            try {
                sleep(WISH_ANIMATION_MILLIS);
            } catch (InterruptedException e) {
                return;
            }
            WishSim.getInstance().switchToWishResult(wishes);
        });
        timeoutThread.start();
    }
}
