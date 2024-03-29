package gui.components;

import javax.swing.*;
import java.awt.*;

public class StyledButton extends JButton {
    public StyledButton(String text) {
        super(text);
        customizeButton();
    }

    // EFFECTS: customizes this button's look in the GUI
    private void customizeButton() {
        setMargin(new Insets(10, 20, 10, 20));
        setSize(10, 10);
        setBorderPainted(true);
        setFocusPainted(true);
        setContentAreaFilled(true);
    }

}
