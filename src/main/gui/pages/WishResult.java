package gui.pages;

import gui.WishSim;
import model.wish.Wish;

import javax.swing.*;
import java.util.List;

import static gui.WishSim.loadImageFromPath;

public class WishResult extends Page {

    private static final String IMG_PATH = "data/static/images/gacha-splash.png";

    public static final String PAGE_ID = "wishResult";

    public WishResult(WishSim wishSim) {
        super(wishSim, PAGE_ID);
        this.wishSim = wishSim;
    }

    @Override
    public void handleMousePressed() {
        super.wishSim.switchToBannerMenu();
    }

    // MODIFIES: this
    // EFFECTS: displays wishes on screen
    public void onPageSwitch(List<Wish> wishes) {
        super.page.removeAll();
        super.page.setLayout(new BoxLayout(super.page, BoxLayout.Y_AXIS));

        int i = 1;
        for (Wish wish : wishes) {
            String text = "";

            if (wish.getRarity() == 5) {
                text = String.format("%d) WOW!! Obtained '%s' (%d stars)!!!\n", i, wish.getName(), wish.getRarity());
            } else if (wish.getRarity() == 4) {
                text = String.format("%d) wow! Obtained '%s' (%d stars)!\n", i, wish.getName(), wish.getRarity());
            } else if (wish.getRarity() == 3) {
                text = String.format("%d) Obtained '%s' (%d stars)\n", i, wish.getName(), wish.getRarity());
            }
            i++;

            JLabel entryLabel = new JLabel(text);
            super.page.add(entryLabel);
        }
    }
}
