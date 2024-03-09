package persistence;

import model.Element;
import model.Inventory;
import model.WeaponType;
import model.banner.Banner;
import model.banner.EventBanner;
import model.wish.*;
import model.wish.Character;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JsonWriterTest {
    private static final String INVENTORY_JSON = "./data/test_data/test_inventory.json";
    private static final String EMPTY_INVENTORY_JSON = "./data/test_data/test_empty_inventory.json";
    private static final String BANNER_JSON = "./data/test_data/test_banner2.json";
    private static final String EMPTY_BANNER_JSON = "./data/test_data/test_empty_banner.json";

    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyBanner() {
        try {
            Banner banner = new Banner("Empty", new ArrayList<>());
            JsonWriter writer = new JsonWriter(EMPTY_BANNER_JSON);
            writer.open();
            writer.writeWritable(banner);
            writer.close();

            JsonReader reader = new JsonReader(EMPTY_BANNER_JSON);
            banner = reader.readBanner();
            assertEquals(0, banner.getWishCount());
            assertEquals("Empty", banner.getName());
            assertEquals(0, banner.getThreeStars().size() +
                    banner.getFourStars().size() + banner.getFiveStars().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralBanner() {
        try {
            Wish c1 = new Character(4, "Amber", Element.PYRO, WeaponType.BOW);
            Wish c2 = new Weapon(3, "Cool Steel", WeaponType.SWORD);
            Wish c3 = new Character(5, "Jean", Element.ANEMO, WeaponType.SWORD);

            List<Wish> wishPool = new ArrayList<>(List.of(c1, c2, c3));
            List<Wish> rateUpFourStars = new ArrayList<>(List.of(c1, c2));

            EventBanner banner = new EventBanner("a", wishPool, c3, rateUpFourStars);
            JsonWriter writer = new JsonWriter(BANNER_JSON);
            writer.open();
            writer.writeWritable(banner);
            writer.close();

            JsonReader reader = new JsonReader(BANNER_JSON);
            banner = (EventBanner) reader.readBanner();
            assertEquals(0, banner.getWishCount());
            assertEquals("a", banner.getName());
            assertEquals(1, banner.getThreeStars().size());
            assertEquals(1, banner.getFourStars().size());
            assertEquals(1, banner.getFiveStars().size());
            assertEquals(c3.getName(), banner.getRateUpFiveStar().getName());
            assertEquals(2, banner.getRateUpFourStars().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }


    @Test
    void testWriterEmptyInventory() {
        try {
            Inventory inventory = new Inventory(new HashMap<>(), 0);
            JsonWriter writer = new JsonWriter(EMPTY_INVENTORY_JSON);
            writer.open();
            writer.writeWritable(inventory);
            writer.close();

            JsonReader reader = new JsonReader(EMPTY_INVENTORY_JSON);
            inventory = reader.readInventory();
            assertEquals(0, inventory.getWishes().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralInventory() {
        try {
            Wish c1 = new Character(4, "Amber", Element.PYRO, WeaponType.BOW);
            Wish c2 = new Weapon(3, "Cool Steel", WeaponType.SWORD);
            Wish c3 = new Character(5, "Jean", Element.ANEMO, WeaponType.SWORD);

            Map<Wish, Integer> wishes = new HashMap<>();
            wishes.put(c1, 2);
            wishes.put(c2, 1);
            wishes.put(c3, 10);
            Inventory inventory = new Inventory(wishes, 0);

            JsonWriter writer = new JsonWriter(INVENTORY_JSON);
            writer.open();
            writer.writeWritable(inventory);
            writer.close();

            JsonReader reader = new JsonReader(INVENTORY_JSON);
            inventory = reader.readInventory();
            assertEquals(3, inventory.getWishes().size());
            for (Wish wish : inventory.getWishes()) {
                int count = inventory.getWishCopies(wish);

                if (wish.getRarity() == 5) {
                    assertEquals(10, count);
                    assertEquals(c3.getName(), wish.getName());
                } else if (wish.getRarity() == 4) {
                    assertEquals(2, count);
                    assertEquals(c1.getName(), wish.getName());
                } else if (wish.getRarity() == 3) {
                    assertEquals(1, count);
                    assertEquals(c2.getName(), wish.getName());
                }
            }
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}