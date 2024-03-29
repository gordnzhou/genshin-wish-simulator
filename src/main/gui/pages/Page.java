package gui.pages;

import gui.WishSim;

import javax.swing.*;
import java.awt.*;

public abstract class Page {
    private static final String MENU_BACKGROUND_PATH = "data/static/images/menu_background.png";

    private final String pageId;

    protected JPanel page;
    protected WishSim wishSim;

    // EFFECTS: constructs page with MENU_BACKGROUND_PATH; adds it to wishSim
    public Page(WishSim wishSim, String pageId) {
        this.page = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                ImageIcon background = new ImageIcon(MENU_BACKGROUND_PATH);
                super.paintComponent(g);
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        this.wishSim = wishSim;
        this.pageId = pageId;
        wishSim.add(page, pageId);
    }

    // EFFECTS: constructs page with the image from the given path as the background; adds it to wishSim
    public Page(WishSim wishSim, String pageId, String backgroundImagePath) {
        this.page = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                ImageIcon background = new ImageIcon(backgroundImagePath);
                super.paintComponent(g);
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
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
