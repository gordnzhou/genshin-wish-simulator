package gui.components;

import javax.swing.*;
import java.awt.*;

public class PrimogemCounter {

    private JPanel panel;
    private JLabel primogemsLabel;

    public PrimogemCounter(JPanel parent) {
        this.primogemsLabel = new JLabel("0");
        this.panel = new JPanel();
        this.panel.setBackground(Color.BLUE);
        this.panel.add(primogemsLabel);
        parent.add(this.panel);
    }

    // TODO:
    // REQUIRES: primogems >= 0
    // MODIFIES: this
    // EFFECTS: updates PrimogemCounter to show the given number of primogems
    public void updateCount(int primogems) {
        primogemsLabel.setText(Integer.toString(primogems));
    }
}
