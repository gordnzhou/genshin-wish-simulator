package model;

import ui.WishSim;
import static ui.WishSim.PRIMOGEMS_PER_WISH;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class WishSimTest {
    private WishSim testWishSim;

    @BeforeEach
    void runBefore() {
        testWishSim = new WishSim(900000);
        testWishSim.init();
    }

    @Test
    void testConstructor() {
        assertEquals(testWishSim.getTotalWishCount(), 0);
        assertTrue(testWishSim.getInventory().isEmpty());
        assertEquals(testWishSim.getPrimogems(), 900000);
        assertEquals(testWishSim.getTotalWishCount(), 0);
    }

    @Test
    void testInit() {
        assertFalse(testWishSim.getBanner() == null);
        assertTrue(testWishSim.getBanner().getThreeStars().size() > 0);
        assertTrue(testWishSim.getBanner().getFourStars().size() > 0);
        assertTrue(testWishSim.getBanner().getThreeStars().size() > 0);
    }

    @Test
    void testMakeWish() {
        testWishSim.makeWish(1);
        assertEquals(testWishSim.getInventory().size(), 1);
        assertEquals(testWishSim.getTotalWishCount(), 1);
        assertEquals(testWishSim.getPrimogems(), 900000 - PRIMOGEMS_PER_WISH * 1);
    }

    @Test
    void testMakeWishFail() {
        testWishSim.makeWish(10000);
        assertEquals(testWishSim.getInventory().size(), 0);
        assertEquals(testWishSim.getTotalWishCount(), 0);
        assertEquals(testWishSim.getPrimogems(), 900000);
    }

    @Test
    void testMakeWishMany() {
        testWishSim.makeWish(3);
        assertEquals(testWishSim.getInventory().size(), 3);
        assertEquals(testWishSim.getTotalWishCount(), 3);
        assertEquals(testWishSim.getPrimogems(), 900000 - PRIMOGEMS_PER_WISH * 3);

        testWishSim.makeWish(100);
        assertEquals(testWishSim.getInventory().size(), 103);
        assertEquals(testWishSim.getTotalWishCount(), 103);
        assertEquals(testWishSim.getPrimogems(), 900000 - PRIMOGEMS_PER_WISH * 103);
    }

    @Test
    void testAddPrimogems() {
        testWishSim.addPrimogems(100);
        assertEquals(testWishSim.getPrimogems(), 900100);
    }

    @Test
    void testAddPrimogemsMany() {
        testWishSim.addPrimogems(1);
        assertEquals(testWishSim.getPrimogems(), 900001);
        testWishSim.addPrimogems(9);
        assertEquals(testWishSim.getPrimogems(), 900010);
        testWishSim.addPrimogems(100);
        assertEquals(testWishSim.getPrimogems(), 900110);
    }
}
