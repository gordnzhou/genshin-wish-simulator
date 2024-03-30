package gui.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static gui.WishSim.loadImageFromPath;

public class BannerDisplay {
    private static final double BANNER_SCALE = 0.8;

    private static final String STANDARD_BANNER_IMAGE_PATH = "data/static/images/wanderlust-invocation.png";
    private static final String EVENT_BANNER_IMAGE_PATH = "data/static/images/crane-soars-skyward.png";

    private JPanel bannerImageArea;
    private JLabel imageLabel;

    /// EFFECTS: Initializes BannerDisplay panel and adds it to parent
    public BannerDisplay(JPanel parent) {
        bannerImageArea = new JPanel();
        bannerImageArea.setOpaque(false);
        bannerImageArea.setBorder(new EmptyBorder(10, 20, 0, 20));
        imageLabel = new JLabel();
        imageLabel.setIcon(loadImageFromPath(EVENT_BANNER_IMAGE_PATH, BANNER_SCALE));
        bannerImageArea.add(imageLabel);
        parent.add(bannerImageArea, BorderLayout.CENTER);
    }

    // MODIFIES: this
    // EFFECTS: displays standard banner image
    public void switchToStandardBanner() {
        imageLabel.setIcon(loadImageFromPath(STANDARD_BANNER_IMAGE_PATH, BANNER_SCALE));
        imageLabel.repaint();
    }

    // MODIFIES: this
    // EFFECTS: displays event banner image
    public void switchToEventBanner() {
        imageLabel.setIcon(loadImageFromPath(EVENT_BANNER_IMAGE_PATH, BANNER_SCALE));
        imageLabel.repaint();
    }
}
