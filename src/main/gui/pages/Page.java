package gui.pages;

import gui.WishSim;
import model.wish.Wish;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Page {
    protected JPanel page;
    protected WishSim wishSim;
    private String pageId;

    public Page(WishSim wishSim, String pageId) {
        this.page = new JPanel();
        this.wishSim = wishSim;
        this.pageId = pageId;
        wishSim.add(page, pageId);
    }

    public String getPageId() {
        return pageId;
    }

    // MODIFIES: wishSim
    // EFFECTS: switches current page to newPage
    protected void switchPage(Page newPage, List<Wish> wishes) {
        wishSim.switchPage(newPage, wishes);
    }

    // MODIFIES: wishSim
    // EFFECTS: handles mouse press; may switch the page
    public abstract void handleMousePressed();

    // MODIFIES: this
    // EFFECTS: runs when this page instance is switch to
    public abstract void onPageSwitch(List<Wish> wishes);
}
