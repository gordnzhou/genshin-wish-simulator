package gui.pages;

import gui.WishSim;
import model.wish.Wish;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class WishResult extends Page {

    public static final String PAGE_ID = "wishResu;lt";

    public WishResult(WishSim wishSim) {
        super(wishSim, PAGE_ID);
        this.wishSim = wishSim;
        super.page.setBackground(Color.GREEN);
        page.add(new JLabel(PAGE_ID));
    }

    @Override
    public void handleMousePressed() {
        super.wishSim.switchToBannerMenu();
    }

    // MODIFIES: this
    // EFFECTS: displays wishes on screen
    public void onPageSwitch(List<Wish> wishes) {
        int i = 1;
        for (Wish wish : wishes) {
            if (wish.getRarity() == 5) {
                System.out.format("%d) WOW!! Obtained '%s' (%d stars)!!!\n", i, wish.getName(), wish.getRarity());
            } else if (wish.getRarity() == 4) {
                System.out.format("%d) wow! Obtained '%s' (%d stars)!\n", i, wish.getName(), wish.getRarity());
            } else if (wish.getRarity() == 3) {
                System.out.format("%d) Obtained '%s' (%d stars)\n", i, wish.getName(), wish.getRarity());
            }
            i++;
        }
    }
}
