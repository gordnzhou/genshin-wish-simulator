package ui;

public class Main {
    public static void main(String[] args) {
        WishSim wishSim = new WishSim();
        wishSim.init();

        wishSim.makeWish(10);
        System.out.println("\n\n");
        wishSim.viewInventory();
    }
}
