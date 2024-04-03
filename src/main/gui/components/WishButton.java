package gui.components;

import model.InventoryObserver;

import javax.swing.*;
import java.awt.*;

import static gui.WishSim.PRIMOGEMS_PER_WISH;

public class WishButton extends JButton implements InventoryObserver {
    private static final String WISH_BUTTON_PATH = "data/static/images/wish-button.png";

    private static final String textColor = "#B0A7A0";
    private static final String textColorRed = "#FF0000";
    private static final String buttonText = "<html><center><b>"
            + "<font color='#B0A7A0' size='4'>Wish x%d </font><br>"
            + "<font color='%s' size='4'><img src='%s' width='30' height='30'>x %d<br></font>"
            + "</b></center></html>";

    private final int wishCount;

    // EFFECTS: initializes and styles WishButton
    public WishButton(int wishCount) {
        super("");
        this.wishCount = wishCount;
        customizeButton(wishCount);
    }

    // MODIFIES: this
    // EFFECTS: customizes this button's look in the GUI
    private void customizeButton(int wishCount) {
        setText(String.format(buttonText, wishCount, textColor,
                getClass().getResource("/assets/primogem.png"), wishCount * PRIMOGEMS_PER_WISH));
        Image image = new ImageIcon(WISH_BUTTON_PATH).getImage();
        ImageIcon imageIcon = new ImageIcon(image.getScaledInstance(300, 74, Image.SCALE_SMOOTH));
        setIcon(imageIcon);
        setOpaque(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setVerticalTextPosition(SwingConstants.CENTER);
        setHorizontalTextPosition(SwingConstants.CENTER);
    }

    @Override
    public void update(int primogems) {
        if (primogems >= wishCount * PRIMOGEMS_PER_WISH) {
            setText(String.format(buttonText, wishCount, textColor,
                    getClass().getResource("/assets/primogem.png"), wishCount * PRIMOGEMS_PER_WISH));
        } else {
            setText(String.format(buttonText, wishCount, textColorRed,
                    getClass().getResource("/assets/primogem.png"), wishCount * PRIMOGEMS_PER_WISH));
        }
    }
}
