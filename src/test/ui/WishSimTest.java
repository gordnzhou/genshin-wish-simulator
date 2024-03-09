package ui;

import model.banner.Banner;
import static ui.WishSim.PRIMOGEMS_PER_WISH;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class WishSimTest {
    private WishSim testWishSim;
    private Banner testBanner;

    @BeforeEach
    void runBefore() {
        try {
            testWishSim = new WishSim(900000);
        } catch (FileNotFoundException e) {
            System.err.println("Unable to run simulator: file not found");
        }
        testWishSim.init();
        testBanner = testWishSim.getBanner();
    }

    @Test
    void testConstructor() {
        assertEquals(testWishSim.getTotalWishCount(), 0);
        assertTrue(testWishSim.getInventoryWishes().isEmpty());
        assertEquals(testWishSim.getPrimogems(), 900000);
        assertEquals(testWishSim.getTotalWishCount(), 0);
        assertTrue(testWishSim.getBanner() != null);
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
        testWishSim.makeWish(testBanner, 1);
        assertEquals(testWishSim.getInventoryWishes().size(), 1);
        assertEquals(testWishSim.getTotalWishCount(), 1);
        assertEquals(testWishSim.getPrimogems(), 900000 - PRIMOGEMS_PER_WISH);
    }

    @Test
    void testMakeWishFail() {
        testWishSim.makeWish(testBanner,10000);
        assertEquals(testWishSim.getInventoryWishes().size(), 0);
        assertEquals(testWishSim.getTotalWishCount(), 0);
        assertEquals(testWishSim.getPrimogems(), 900000);
    }

    @Test
    void testMakeWishMany() {
        testWishSim.makeWish(testBanner,3);
        assertEquals(testWishSim.getTotalWishCount(), 3);
        assertEquals(testWishSim.getPrimogems(), 900000 - PRIMOGEMS_PER_WISH * 3);

        testWishSim.makeWish(testBanner,100);
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
