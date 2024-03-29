package ui;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        try {
            ConsoleWishSim consoleWishSim = new ConsoleWishSim(100000);
            consoleWishSim.runWishSim();
        } catch (FileNotFoundException e) {
            System.err.println("Unable to run simulator: file not found");
        }
    }
}
