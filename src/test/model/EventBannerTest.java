package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EventBannerTest {
    private EventBanner testEventBanner;

    private List<Wish> wishPool;
    private Wish rateUpFiveStar;
    private List<Wish> rateUpFourStars;

    @BeforeEach
    void runBefore() {
        wishPool = new ArrayList<>();
        wishPool.add(new Weapon(3, "Sword", WeaponType.SWORD));
        wishPool.add(new Character(5, "Jean", Element.ANEMO, WeaponType.SWORD));
        wishPool.add(new Character(4, "Amber", Element.PYRO, WeaponType.BOW));
        wishPool.add(new Weapon(3, "Sword", WeaponType.SWORD));
        wishPool.add(new Character(5, "Diluc", Element.PYRO, WeaponType.CLAYMORE));
        wishPool.add(new Character(4, "Lisa", Element.ELECTRO, WeaponType.CATALYST));
        wishPool.add(new Character(4, "Kaeya", Element.CRYO, WeaponType.SWORD));
        wishPool.add(new Weapon(3, "aa", WeaponType.CLAYMORE));
        wishPool.add(new Weapon(3, "bb", WeaponType.POLEARM));
        wishPool.add(new Weapon(3, "cc", WeaponType.BOW));
        wishPool.add(new Weapon(5, "dd", WeaponType.CATALYST));

        rateUpFiveStar = new Character(5, "Ganyu", Element.CRYO, WeaponType.BOW);

        rateUpFourStars = new ArrayList<>();
        rateUpFourStars.add(new Character(4, "Xiangling", Element.PYRO, WeaponType.POLEARM));
        rateUpFourStars.add(new Character(4, "Collei", Element.DENDRO, WeaponType.BOW));

        testEventBanner = new EventBanner("Banner", wishPool, rateUpFiveStar, rateUpFourStars);
    }

    @Test
    void testConstructor() {
        assertEquals(testEventBanner.getFiveStarPity(), 0);
        assertEquals(testEventBanner.getFourStarPity(), 0);
        assertEquals(testEventBanner.getWishCount(), 0);
        assertEquals(testEventBanner.getName(), "Banner");
        assertEquals(testEventBanner.getRateUpFiveStar(), rateUpFiveStar);
        assertEquals(testEventBanner.getRateUpFourStars(), rateUpFourStars);

        for (Wish wish : testEventBanner.getThreeStars()) {
            assertEquals(wish.getRarity(), 3);
        }
        for (Wish wish : testEventBanner.getFourStars()) {
            assertEquals(wish.getRarity(), 4);
        }
        for (Wish wish : testEventBanner.getFiveStars()) {
            assertEquals(wish.getRarity(), 5);
        }
    }

    @Test
    void testMakeWish() {
        Wish wish = testEventBanner.makeWish();

        assertEquals(testEventBanner.getWishCount(), 1);
        assertTrue(wishPool.contains(wish) || wish.equals(rateUpFiveStar) || rateUpFourStars.contains(wish));
    }

    @Test
    void testMakeWishMany() {
        for (int i = 0; i < 1000; i++) {
            assertEquals(testEventBanner.getWishCount(), i);

            Wish wish = testEventBanner.makeWish();

            if (wish.equals(rateUpFiveStar)) {
                assertEquals(testEventBanner.getFailedFiveStar(), false);
                assertEquals(testEventBanner.getFiveStarPity(), 0);
            } else if (rateUpFourStars.contains(wish)) {
                assertEquals(testEventBanner.getFailedFourStar(), false);
                assertEquals(testEventBanner.getFourStarPity(), 0);
            } else {
                assertTrue(wishPool.contains(wish));
            }
        }
    }
}
