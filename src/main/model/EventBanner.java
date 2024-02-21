package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Random;

// represents a limited edition event wish banner
public class EventBanner extends Banner {

    private Wish rateUpFiveStar;
    private List<Wish> rateUpFourStars;

    private boolean failedFiveStar = false;
    private boolean failedFourStar = false;

    private Random random;

    // EFFECTS: instantiates an EventBanner with given wishPool, name and rate up 4 and 5 stars
    public EventBanner(String name, List<Wish> wishPool, Wish rateUpFiveStar, List<Wish> rateUpFourStars) {
        super(name, wishPool);
        this.rateUpFourStars = rateUpFourStars;
        this.rateUpFiveStar = rateUpFiveStar;
        this.random = new Random();
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();
        json.put("rate_up_five_star", rateUpFiveStar.toJson());
        json.put("rate_up_four_stars", rateUpFourStarsToJson());
        return json;
    }

    // EFFECTS: returns this event banner's rate up four stars as a JSON array
    private JSONArray rateUpFourStarsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Wish w : rateUpFourStars) {
            jsonArray.put(w.toJson());
        }

        return jsonArray;
    }

    // MODIFIES: this
    // EFFECTS: returns a random wish from fiveStars OR rateUpFiveStar
    @Override
    protected Wish randomFiveStar() {
        Wish randFiveStar = super.randomFiveStar();

        if (failedFiveStar || random.nextInt(2) % 2 == 0) {
            failedFiveStar = false;
            return rateUpFiveStar;
        }

        failedFiveStar = true;
        return randFiveStar;
    }

    // MODIFIES: this
    // EFFECTS: gets a random wish from fourStars OR a rateUpFourStar
    @Override
    protected Wish randomFourStar() {
        Wish randFourStar = super.randomFourStar();

        if (failedFourStar || random.nextInt(2) % 2 == 0) {
            failedFourStar = false;
            int index = random.nextInt(rateUpFourStars.size());
            return rateUpFourStars.get(index);
        }

        failedFourStar = true;
        return randFourStar;
    }

    public Wish getRateUpFiveStar() {
        return rateUpFiveStar;
    }

    public List<Wish> getRateUpFourStars() {
        return rateUpFourStars;
    }

    public boolean getFailedFiveStar() {
        return failedFiveStar;
    }

    public boolean getFailedFourStar() {
        return  failedFourStar;
    }
}
