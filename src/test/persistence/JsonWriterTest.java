package persistence;

import model.*;

import model.Character;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JsonWriterTest {
    private static final String TEST_INVENTORY_PATH = "./data/test_data/test_inventory.json";
    private static final String TEST_EMPTY_INVENTORY_PATH = "./data/test_data/test_empty_inventory.json";

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
    void testWriterEmptyInventory() {
        try {
            Map<Wish, Integer> inventory = new HashMap<>();
            JsonWriter writer = new JsonWriter(TEST_EMPTY_INVENTORY_PATH);
            writer.open();
            writer.writeInventory(inventory);
            writer.close();

            JsonReader reader = new JsonReader(TEST_EMPTY_INVENTORY_PATH);
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
            JsonWriter writer = new JsonWriter(TEST_INVENTORY_PATH);
            writer.open();
            writer.writeInventory(inventory);
            writer.close();

            JsonReader reader = new JsonReader(TEST_INVENTORY_PATH);
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