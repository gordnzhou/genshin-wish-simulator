package persistence;

import model.*;

import model.Character;
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
            writer.writeBanner(banner);
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

            Banner banner = new Banner("a", new ArrayList<>(List.of(c1, c2, c3)));
            JsonWriter writer = new JsonWriter(BANNER_JSON);
            writer.open();
            writer.writeBanner(banner);
            writer.close();

            JsonReader reader = new JsonReader(BANNER_JSON);
            banner = reader.readBanner();
            assertEquals(0, banner.getWishCount());
            assertEquals("a", banner.getName());
            assertEquals(1, banner.getThreeStars().size());
            assertEquals(1, banner.getFourStars().size());
            assertEquals(1, banner.getFiveStars().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }


    @Test
    void testWriterEmptyInventory() {
        try {
            Map<Wish, Integer> inventory = new HashMap<>();
            JsonWriter writer = new JsonWriter(EMPTY_INVENTORY_JSON);
            writer.open();
            writer.writeInventory(inventory);
            writer.close();

            JsonReader reader = new JsonReader(EMPTY_INVENTORY_JSON);
            inventory = reader.readInventory();
            assertEquals(0, inventory.size());
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

            Map<Wish, Integer> inventory = new HashMap<>();
            inventory.put(c1, 2);
            inventory.put(c2, 1);
            inventory.put(c3, 10);
            JsonWriter writer = new JsonWriter(INVENTORY_JSON);
            writer.open();
            writer.writeInventory(inventory);
            writer.close();

            JsonReader reader = new JsonReader(INVENTORY_JSON);
            inventory = reader.readInventory();
            assertEquals(3, inventory.size());
            for (Map.Entry<Wish, Integer> entry : inventory.entrySet()) {
                Wish wish = entry.getKey();
                int count = entry.getValue();

                if (wish.getRarity() == 5) {
                    assertEquals(count, 10);
                } else if (wish.getRarity() == 4) {
                    assertEquals(count, 2);
                } else if (wish.getRarity() == 3) {
                    assertEquals(count, 1);
                }
            }
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}