package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.WishSim;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class WishSimTest {
    private WishSim testWishSim;

    @BeforeEach
    void runBefore() {
        testWishSim = new WishSim();
        testWishSim.init();
    }

    @Test
    void testConstructor() {
        assertEquals(testWishSim.getTotalWishCount(), 0);
        assertTrue(testWishSim.getInventory().isEmpty());
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
    }

    @Test
    void testMakeWishMany() {
        testWishSim.makeWish(3);
        assertEquals(testWishSim.getInventory().size(), 3);
        assertEquals(testWishSim.getTotalWishCount(), 3);

        testWishSim.makeWish(100);
        assertEquals(testWishSim.getInventory().size(), 103);
        assertEquals(testWishSim.getTotalWishCount(), 103);
    }
}
