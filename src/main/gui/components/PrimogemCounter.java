package gui.components;

import gui.WishSim;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

import static gui.WishSim.loadImageFromPath;

public class PrimogemCounter implements ActionListener {
    public static final String PRIMOGEM_PATH = "data/static/images/primogem.png";
    private static final double ICON_SCALE = 0.11;

    private JPanel panel;
    private JLabel primogemsLabel;
    private PlusButton addButton;

    WishSim wishSim;

    // EFFECTS: creates a counter and adds it to parent
    public PrimogemCounter(JPanel parent, WishSim wishSim) {
        this.initPanel();
        this.wishSim = wishSim;
        JLabel iconLabel = new JLabel();
        iconLabel.setIcon(loadImageFromPath(PRIMOGEM_PATH, ICON_SCALE));
        this.panel.add(iconLabel);
        this.primogemsLabel = new JLabel("0    ");
        this.primogemsLabel.setForeground(Color.WHITE);
        this.panel.add(primogemsLabel);
        this.addButton = new PlusButton();
        this.panel.add(addButton);
        this.addButton.addActionListener(this);
        parent.add(this.panel);
    }

    // MODIFIES: this
    // EFFECTS: creates this panel with proper styling
    private void initPanel() {
        this.panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(0, 0, 0, 100));
                int arcWidth = 30;
                int arcHeight = 30;
                Shape roundedRect = new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), arcWidth, arcHeight);
                g2.fill(roundedRect);
            }
        };
        this.panel.setOpaque(false);
    }

    // REQUIRES: primogems >= 0
    // MODIFIES: this
    // EFFECTS: updates PrimogemCounter to show the given number of primogems
    public void updateCount(int primogems) {
        String newText = String.format("%d    ", primogems);
        primogemsLabel.setText(newText);
    }

    // MODIFIES: this, WishSim
    // EFFECTS: handles adding of primogems to inventory
    private void handleAddPrimogems() {
        String input = JOptionPane.showInputDialog("Enter amount of Primogems you would like to purchase.");
        if (input == null) {
            return;
        }

        try {
            int count = Integer.parseInt(input);
            if (count > 0) {
                wishSim.addPrimogems(count);

                String successMessage = "Added " + count + " Primogems to your Inventory";
                JOptionPane.showMessageDialog(null,
                        successMessage, "Purchase Success", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        } catch (NumberFormatException numErr) {
            numErr.printStackTrace();
        }

        JOptionPane.showMessageDialog(null,
                "Invalid Amount Entered!", "Error:", JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            handleAddPrimogems();
        }
    }
}
