package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// represents a standard banner that users can wish from
public class Banner {
    // for random percentage calculation
    public static final int ACCURACY = 100000;

    public static final double FIVE_STAR_BASE_RATE = 0.6;
    public static final int FIVE_STAR_MAX_PITY = 90;
    public static final int FIVE_STAR_SOFT_PITY = 74;

    public static final double FOUR_STAR_BASE_RATE = 5.1;
    public static final int FOUR_STAR_MAX_PITY = 10;
    public static final int FOUR_STAR_SOFT_PITY = 8;

    private String name;
    private Random random;

    private List<Wish> fiveStars;
    private List<Wish> fourStars;
    private List<Wish> threeStars;

    private int wishCount;
    private int fourStarPity;
    private int fiveStarPity;

    // EFFECTS: instantiates a banner with no wishes done yet and loads in fiveStars, fourStars, threeStars
    public Banner(String name, List<Wish> wishPool) {
        this.wishCount = 0;
        this.fourStarPity = 0;
        this.fiveStarPity = 0;
        this.name = name;

        this.random = new Random();
        this.fiveStars = new ArrayList<>();
        this.fourStars = new ArrayList<>();
        this.threeStars = new ArrayList<>();
        for (Wish wish : wishPool) {
            if (wish.getRarity() == 5) {
                fiveStars.add(wish);
            } else if (wish.getRarity() == 4) {
                fourStars.add(wish);
            } else {
                threeStars.add(wish);
            }
        }
    }

    // REQUIRES: fourStars, fivesStars, threeStars are all non-empty
    // MODIFIES: this
    // EFFECTS: increments wishCount, fourStarPity, and fiveStartPity by 1,
    //          then returns a calculated randomized wish
    public Wish makeWish() {
        wishCount++;
        fourStarPity++;
        fiveStarPity++;

        double fiveStarRate = calcStarRate(fiveStarPity, FIVE_STAR_BASE_RATE,
                FIVE_STAR_SOFT_PITY, FIVE_STAR_MAX_PITY);
        double fourStarRate = calcStarRate(fourStarPity, FOUR_STAR_BASE_RATE,
                FOUR_STAR_SOFT_PITY, FOUR_STAR_MAX_PITY);

        int randNum = random.nextInt(ACCURACY);
        if (randNum <= fiveStarRate * ACCURACY / 100) {
            return randomFiveStar();
        } else if (randNum <= fourStarRate * ACCURACY / 100) {
            return randomFourStar();
        }
        return randomThreeStar();
    }

    // REQUIRES: count >= 1 and fourStars, fivesStars, threeStars are all non-empty
    // MODIFIES: this
    // EFFECTS: increments wishCount, fourStarPity, and fiveStarPity by 'count',
    //          then makes wish from current banner 'count' times the returns the results
    public List<Wish> makeWish(int count) {
        List<Wish> wishes = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            wishes.add(makeWish());
        }
        return wishes;
    }

    // REQUIRES: 0 < baseRate < 100, softPity < hardPity, all parameters > 0
    // EFFECTS: generates probability of a 5 star based on number of wishes done so far;
    //          100% if at hardPity, increased baseRate if at softPity, baseRate otherwise
    private double calcStarRate(int wishesSoFar, double baseRate, int softPity, int hardPity) {
        if (wishesSoFar >= hardPity) {
            return 100;
        } else if (wishesSoFar >= softPity) {
            return baseRate + (5.85 * (wishesSoFar - softPity));
        }
        return baseRate;
    }

    // REQUIRES: fourStars is non-empty
    // MODIFIES: this
    // EFFECTS: gets a random wish from fourStars and sets fourStarPity to 0
    private Wish randomFourStar() {
        fourStarPity = 0;
        int index = random.nextInt(fourStars.size());
        return fourStars.get(index);
    }

    // REQUIRES: fiveStars is non-empty
    // MODIFIES: this
    // EFFECTS: gets a random wish from fiveStars and sets fiveStarPity to 0
    private Wish randomFiveStar() {
        fiveStarPity = 0;
        int index = random.nextInt(fiveStars.size());
        return fiveStars.get(index);
    }

    // REQUIRES: threeStars is non-empty
    // EFFECTS: gets a random wish from threeStars
    private Wish randomThreeStar() {
        int index = random.nextInt(threeStars.size());
        return threeStars.get(index);
    }

    public List<Wish> getFiveStars() {
        return fiveStars;
    }

    public List<Wish> getFourStars() {
        return fourStars;
    }

    public List<Wish> getThreeStars() {
        return threeStars;
    }

    public int getWishCount() {
        return wishCount;
    }

    public int getFourStarPity() {
        return fourStarPity;
    }

    public int getFiveStarPity() {
        return fiveStarPity;
    }

    public String getName() {
        return name;
    }
}
