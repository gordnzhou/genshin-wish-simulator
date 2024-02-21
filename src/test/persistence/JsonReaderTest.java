package persistence;

import model.Character;
import org.junit.jupiter.api.Test;

import model.*;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JsonReaderTest {
    private static final String TEST_INVENTORY_PATH = "./data/test_data/test_inventory.json";
    private static final String TEST_EMPTY_INVENTORY_PATH = "./data/test_data/test_empty_inventory.json";
    private static final String TEST_BANNER_PATH = "./data/test_data/test_banner.json";

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
    void testReaderBanner() {
        JsonReader reader = new JsonReader(TEST_BANNER_PATH);
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
        JsonReader reader = new JsonReader(TEST_EMPTY_INVENTORY_PATH);
        try {
            Map<Wish, Integer> inventory = reader.readInventory();
            assertEquals(0, inventory.size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralInventory() {
        JsonReader reader = new JsonReader(TEST_INVENTORY_PATH);
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