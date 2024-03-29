package gui.components;

import javax.swing.*;
import java.awt.*;

public class WishButton extends JButton {
    public WishButton(String text) {
        super(text);
        customizeButton();
    }

    // EFFECTS: customizes this button's look in the GUI
    private void customizeButton() {
        setMargin(new Insets(10, 50, 10, 50));
        setSize(10, 10);
        setBorderPainted(true);
        setFocusPainted(true);
        setContentAreaFilled(true);
    }

    private void addListener() {
//        addActionListener();
    }
}
