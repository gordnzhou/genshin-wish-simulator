package ui;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        try {
            WishSim wishSim = new WishSim(100000);
            wishSim.runWishSim();
        } catch (FileNotFoundException e) {
            System.err.println("Unable to run simulator: file not found");
        }
    }
}
