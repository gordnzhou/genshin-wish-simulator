package gui.components;

import gui.WishSim;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static gui.WishSim.loadImageFromPath;

public class PrimogemCounter implements ActionListener {
    private static final String PRIMOGEM_PATH = "data/static/images/primogem.png";
    private static final double ICON_SCALE = 0.15;

    private JPanel panel;
    private JLabel primogemsLabel;
    private JButton addButton;

    WishSim wishSim;

    public PrimogemCounter(JPanel parent, WishSim wishSim) {
        this.panel = new JPanel();
        this.wishSim = wishSim;

        JLabel iconLabel = new JLabel();
        loadImageFromPath(iconLabel, PRIMOGEM_PATH, ICON_SCALE);
        this.panel.add(iconLabel);

        this.primogemsLabel = new JLabel("0");
        this.panel.add(primogemsLabel);

        this.addButton = new JButton("+");
        this.panel.add(addButton);
        this.addButton.addActionListener(this);

        parent.add(this.panel);
    }

    // REQUIRES: primogems >= 0
    // MODIFIES: this
    // EFFECTS: updates PrimogemCounter to show the given number of primogems
    public void updateCount(int primogems) {
        primogemsLabel.setText(Integer.toString(primogems));
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
