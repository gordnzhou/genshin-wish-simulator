package model;

import exceptions.NotEnoughPrimosException;
import model.wish.*;
import model.wish.Character;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


public class InventoryTest {
    private Inventory inventory;
    private Wish wish1;
    private Wish wish2;

    @BeforeEach
    public void runBefore() {
        inventory = new Inventory(new HashMap<>(), 0);
        wish1 = new Character(5, "a", Element.ELECTRO, WeaponType.POLEARM);
        wish2 = new Weapon(4, "b", WeaponType.BOW);
    }

    @Test
    public void testConstructor() {
        assertEquals(0, inventory.getPrimogems());
        assertTrue(inventory.getWishes().isEmpty());
    }

    @Test
    public void testAddPrimogems() {
        inventory.addPrimogems(50);
        assertEquals(50, inventory.getPrimogems());
        inventory.addPrimogems(0);
        assertEquals(50, inventory.getPrimogems());
        inventory.addPrimogems(-10);
        assertEquals(50, inventory.getPrimogems());
    }

    @Test
    public void testRemovePrimogems() {
        inventory.addPrimogems(80);
        assertEquals(80, inventory.getPrimogems());
        try {
            inventory.removePrimogems(20);
            assertEquals(60, inventory.getPrimogems());
            inventory.removePrimogems(-10);
            inventory.removePrimogems(0);
            assertEquals(60, inventory.getPrimogems());
        } catch (Exception e) {
            fail("No Exception Expected");
        }
    }

    @Test
    public void testRemovePrimogemsNotEnoughPrimosException() {
        inventory.addPrimogems(50);
        try {
            inventory.removePrimogems(80);
            fail("NotEnoughPrimosException Expected");
        } catch (NotEnoughPrimosException e) {
            // pass
        }
    }

    @Test
    public void testAddWish() {
        assertEquals(0, inventory.getWishCopies(wish1));

        inventory.addWish(wish1);
        assertEquals(1, inventory.getWishCopies(wish1));
        assertTrue(inventory.getWishes().contains(wish1));

        inventory.addWish(wish1);
        inventory.addWish(wish1);
        assertEquals(3, inventory.getWishCopies(wish1));

        inventory.addWish(wish2);
        assertTrue(inventory.getWishes().contains(wish2));
        assertEquals(1, inventory.getWishCopies(wish2));
        assertEquals(3, inventory.getWishCopies(wish1));
    }
}
