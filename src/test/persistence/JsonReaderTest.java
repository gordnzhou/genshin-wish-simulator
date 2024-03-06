package persistence;

import model.banner.Banner;
import model.banner.EventBanner;
import model.wish.Wish;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JsonReaderTest {
    private static final String INVENTORY_JSON = "./data/test_data/test_inventory.json";
    private static final String EMPTY_INVENTORY_JSON = "./data/test_data/test_empty_inventory.json";
    private static final String BANNER_JSON = "./data/test_data/test_banner.json";
    private static final String EMPTY_BANNER_JSON = "./data/test_data/test_empty_banner.json";

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Map<Wish, Integer> inventory = reader.readInventory();
            inventory.clear();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyBanner() {
        JsonReader reader = new JsonReader(EMPTY_BANNER_JSON);
        try {
            Banner banner = reader.readBanner();
            assertEquals(0, banner.getWishCount());
            assertEquals("Empty", banner.getName());
            assertEquals(0, banner.getThreeStars().size() +
                    banner.getFourStars().size() + banner.getFiveStars().size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderBanner() {
        JsonReader reader = new JsonReader(BANNER_JSON);
        try {
            EventBanner banner = (EventBanner) reader.readBanner();
            assertEquals(0, banner.getWishCount());
            assertEquals("Test Banner", banner.getName());
            assertEquals(13, banner.getThreeStars().size());
            assertEquals(57, banner.getFourStars().size());
            assertEquals(7, banner.getFiveStars().size());
            assertEquals(3, banner.getRateUpFourStars().size());
            assertEquals("Xianyun", banner.getRateUpFiveStar().getName());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderEmptyInventory() {
        JsonReader reader = new JsonReader(EMPTY_INVENTORY_JSON);
        try {
            Map<Wish, Integer> inventory = reader.readInventory();
            assertEquals(0, inventory.size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralInventory() {
        JsonReader reader = new JsonReader(INVENTORY_JSON);
        try {
            Map<Wish, Integer> inventory = reader.readInventory();
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
            fail("Couldn't read from file");
        }
    }
}