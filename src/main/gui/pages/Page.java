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
    // EFFECTS: handles mouse press; may switch the page
    public void handleMousePressed() {

    }
}
