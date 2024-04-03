package gui.components;

import gui.WishSim;
import model.InventoryObserver;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

import static gui.WishSim.loadImageFromPath;

public class PrimogemCounter implements ActionListener, InventoryObserver {
    public static final String PRIMOGEM_PATH = "data/static/images/primogem.png";
    private static final double ICON_SCALE = 0.11;

    private JPanel panel;
    private final JLabel primogemsLabel;
    private final PlusButton addButton;

    // EFFECTS: creates a counter and adds it to parent
    public PrimogemCounter(JPanel parent) {
        this.initPanel();
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
                Shape roundedRect = new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30);
                g2.fill(roundedRect);
            }
        };
        this.panel.setOpaque(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String input = JOptionPane.showInputDialog("Enter amount of Primogems you would like to purchase.");
        if (input == null) {
            return;
        }

        try {
            int count = Integer.parseInt(input);
            if (count > 0) {
                WishSim.getInstance().addPrimogems(count);
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
    public void update(int primogems) {
        System.out.println("SETTING TO: " + primogems);
        String newText = String.format("%d    ", primogems);
        primogemsLabel.setText(newText);
    }
}
