package gui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

class PlusButton extends JButton {
    public PlusButton() {
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setPreferredSize(new Dimension(20, 20));
        setForeground(Color.BLACK);
        setText("+");
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(new Color(245, 244, 205));
        g2.fill(new Ellipse2D.Double(0, 0, getWidth(), getHeight()));
        g2.setColor(getForeground());
        Font originalFont = getFont();
        Font newFont = originalFont.deriveFont(Font.BOLD, 18f);
        g2.setFont(newFont);

        FontMetrics fm = g2.getFontMetrics(newFont);
        int stringWidth = fm.stringWidth(getText());
        int stringHeight = fm.getAscent();

        int x = (getWidth() - stringWidth) / 2;
        int y = (getHeight() - stringHeight) / 2 + fm.getAscent() - 2;

        g2.drawString(getText(), x, y);
    }

    @Override
    protected void paintBorder(Graphics g) {

    }
}