package model;

import java.util.List;
import java.util.Random;

public class Banner {
    public static final double FIVE_STAR_BASE_RATE = 0.6;
    public static final int FIVE_STAR_MAX_PITY = 90;

    public static final double FOUR_STAR_BASE_RATE = 5.1;
    public static final int FOUR_STAR_MAX_PITY = 10;

    private List<Wish> fiveStars;
    private List<Wish> fourStars;
    private List<Wish> threeStars;

    private int wishCount;
    private int fourStarPity;
    private int fiveStarPity;

    // EFFECTS: instantiates a banner with no wishes done yet and loads in fiveStars, fourStars, threeStars
    public Banner(List<Wish> wishPool) {
        this.wishCount = 0;
        this.fourStarPity = 0;
        this.fiveStarPity = 0;

        for (Wish wish : wishPool) {
            if (wish.getRarity() == 5) {
                fiveStars.add(wish);
            } else if (wish.getRarity() == 4) {
                fourStars.add(wish);
            } else if (wish.getRarity() == 3) {
                threeStars.add(wish);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: increments wishCount by 1 and returns a calculated randomized wish
    public Wish makeWish() {
        return null;
    }

    // REQUIRES: count >= 1
    // MODIFIES: this
    // EFFECTS: makes wish from current banner 'count' times and returns the resulting wishes
    public List<Wish> makeWishes(int count) {
        return null;
    }

    // REQUIRES: fourStars is non-empty
    // MODIFIES: this
    // EFFECTS: gets a random wish from fourStars and sets fourStarPity to 0
    private Wish getFourStar() {
        return null;
    }

    // REQUIRES: fiveStars is non-empty
    // MODIFIES: this
    // EFFECTS: gets a random wish from fiveStars and sets fiveStarPity to 0
    private Wish getFiveStar() {
        return null;
    }

    // REQUIRES: threeStars is non-empty
    // MODIFIES: this
    // EFFECTS: gets a random wish from threeStars
    private Wish getThreeStar() {
        return null;
    }
}
