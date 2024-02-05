package ui;

import model.Banner;
import model.Wish;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WishSim {
    private List<Wish> inventory; // change to hashmap in the future?
    private Banner banner;
    private Scanner scanner;

    // EFFECTS: parses wish data into banner and runs the wish simulator
    public WishSim() {
        List<Wish> wishPool = new ArrayList<Wish>();

        inventory = new ArrayList<Wish>();
        banner = new Banner(wishPool);
    }

    // REQUIRES: count >= 1
    // MODIFIES: this
    // EFFECTS: makes wish from banner 'count' times and adds all the results to inventory
    public void makeWish(Banner banner, int count) {

    }
}
