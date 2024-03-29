package ui;

import model.banner.Banner;
import static ui.ConsoleWishSim.PRIMOGEMS_PER_WISH;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ConsoleWishSimTest {
    private ConsoleWishSim testConsoleWishSim;
    private Banner testBanner;

    @BeforeEach
    void runBefore() {
        try {
            testConsoleWishSim = new ConsoleWishSim(900000);
        } catch (FileNotFoundException e) {
            System.err.println("Unable to run simulator: file not found");
        }
        testConsoleWishSim.init();
        testBanner = testConsoleWishSim.getBanner();
    }

    @Test
    void testConstructor() {
        assertEquals(testConsoleWishSim.getTotalWishCount(), 0);
        assertTrue(testConsoleWishSim.getInventoryWishes().isEmpty());
        assertEquals(testConsoleWishSim.getPrimogems(), 900000);
        assertEquals(testConsoleWishSim.getTotalWishCount(), 0);
        assertTrue(testConsoleWishSim.getBanner() != null);
    }

    @Test
    void testInit() {
        assertFalse(testConsoleWishSim.getBanner() == null);
        assertTrue(testConsoleWishSim.getBanner().getThreeStars().size() > 0);
        assertTrue(testConsoleWishSim.getBanner().getFourStars().size() > 0);
        assertTrue(testConsoleWishSim.getBanner().getThreeStars().size() > 0);
    }

    @Test
    void testMakeWish() {
        testConsoleWishSim.makeWish(testBanner, 1);
        assertEquals(testConsoleWishSim.getInventoryWishes().size(), 1);
        assertEquals(testConsoleWishSim.getTotalWishCount(), 1);
        assertEquals(testConsoleWishSim.getPrimogems(), 900000 - PRIMOGEMS_PER_WISH);
    }

    @Test
    void testMakeWishFail() {
        testConsoleWishSim.makeWish(testBanner,10000);
        assertEquals(testConsoleWishSim.getInventoryWishes().size(), 0);
        assertEquals(testConsoleWishSim.getTotalWishCount(), 0);
        assertEquals(testConsoleWishSim.getPrimogems(), 900000);
    }

    @Test
    void testMakeWishMany() {
        testConsoleWishSim.makeWish(testBanner,3);
        assertEquals(testConsoleWishSim.getTotalWishCount(), 3);
        assertEquals(testConsoleWishSim.getPrimogems(), 900000 - PRIMOGEMS_PER_WISH * 3);

        testConsoleWishSim.makeWish(testBanner,100);
        assertEquals(testConsoleWishSim.getTotalWishCount(), 103);
        assertEquals(testConsoleWishSim.getPrimogems(), 900000 - PRIMOGEMS_PER_WISH * 103);
    }

    @Test
    void testAddPrimogems() {
        testConsoleWishSim.addPrimogems(100);
        assertEquals(testConsoleWishSim.getPrimogems(), 900100);
    }

    @Test
    void testAddPrimogemsMany() {
        testConsoleWishSim.addPrimogems(1);
        assertEquals(testConsoleWishSim.getPrimogems(), 900001);
        testConsoleWishSim.addPrimogems(9);
        assertEquals(testConsoleWishSim.getPrimogems(), 900010);
        testConsoleWishSim.addPrimogems(100);
        assertEquals(testConsoleWishSim.getPrimogems(), 900110);
    }
}
