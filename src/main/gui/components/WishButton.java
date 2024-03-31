package gui.components;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;

import static gui.WishSim.PRIMOGEMS_PER_WISH;
import static gui.components.PrimogemCounter.PRIMOGEM_PATH;

public class WishButton extends JButton {
    private static final String WISH_BUTTON_PATH = "data/static/images/wish-button.png";

    // EFFECTS: initializes and styles WishButton
    public WishButton(int wishCount) {
        super("");
        customizeButton(wishCount);
    }

    // MODIFIES: this
    // EFFECTS: customizes this button's look in the GUI
    private void customizeButton(int wishCount) {
        setText(String.format("<html>Wish x%d <br> <img src='%s' width='30' height='30'>x %d<br></html>",
                wishCount, getClass().getResource("/assets/primogem.png"), wishCount * PRIMOGEMS_PER_WISH));
        Image image = new ImageIcon(WISH_BUTTON_PATH).getImage();
        ImageIcon imageIcon = new ImageIcon(image.getScaledInstance(300, 74, Image.SCALE_SMOOTH));
        setIcon(imageIcon);
        setText("<html><center><font color=\"#B0A7A0\" size=\"4\"><b>"
                + this.getText() + "</b></font></center></html>");
        setOpaque(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setVerticalTextPosition(SwingConstants.CENTER);
        setHorizontalTextPosition(SwingConstants.CENTER);
    }
}
