package gui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class StyledButton extends JButton {
    public StyledButton(String text) {
        super(text);
        customizeButton();
    }

    // EFFECTS: customizes this button's look in the GUI
    private void customizeButton() {
        setText(this.getText());
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setVerticalTextPosition(SwingConstants.CENTER);
        setHorizontalTextPosition(SwingConstants.CENTER);
    }

    @Override
    public void setText(String text) {
        super.setText("<html><center><font color=\"#54525E\" size=\"3\"><b>"
                + text + "</b></font></center></html>");
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(226, 224, 214));
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
        super.paintComponent(g2);
    }

}
