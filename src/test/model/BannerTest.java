package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BannerTest {
    private Banner testBannerA;
    private Banner testBannerB;
    private Banner testBannerC;
    private Banner testBannerW;

    private List<Wish> wishPoolA;
    private List<Wish> wishPoolB;
    private List<Wish> wishPool;
    private List<Wish> wishPoolW;

    @BeforeEach
    void runBefore() {
        wishPool = new ArrayList<>();
        wishPool.add(new Character(4, "Amber", Element.PYRO, WeaponType.BOW));
        testBannerC = new Banner("Banner C", wishPool);

        wishPoolW = new ArrayList<>();
        wishPoolW.add(new Weapon(3, "Sword", WeaponType.SWORD));
        testBannerW = new Banner("Banner W", wishPoolW);

        wishPoolA = new ArrayList<>();
        wishPoolA.add(new Character(5, "Jean", Element.ANEMO, WeaponType.SWORD));
        wishPoolA.add(new Character(4, "Amber", Element.PYRO, WeaponType.BOW));
        wishPoolA.add(new Weapon(3, "Sword", WeaponType.SWORD));
        testBannerA = new Banner("Banner A", wishPoolA);

        wishPoolB = new ArrayList<>();
        wishPoolB.addAll(wishPoolA);
        wishPoolB.add(new Character(5, "Diluc", Element.PYRO, WeaponType.GREATSWORD));
        wishPoolB.add(new Character(4, "Lisa", Element.ELECTRO, WeaponType.CATALYST));
        wishPoolB.add(new Character(4, "Kaeya", Element.CRYO, WeaponType.SWORD));
        wishPoolB.add(new Weapon(3, "aa", WeaponType.GREATSWORD));
        wishPoolB.add(new Weapon(3, "bb", WeaponType.POLEARM));
        wishPoolB.add(new Weapon(3, "cc", WeaponType.BOW));
        wishPoolB.add(new Weapon(5, "dd", WeaponType.CATALYST));
        testBannerB = new Banner("Banner B", wishPoolB);
    }

    @Test
    void testConstructor() {
        assertEquals(testBannerA.getFiveStarPity(), 0);
        assertEquals(testBannerA.getFourStarPity(), 0);
        assertEquals(testBannerA.getWishCount(), 0);
        assertEquals(testBannerA.getName(), "Banner A");

        for (Wish wish : testBannerA.getThreeStars()) {
            assertEquals(wish.getRarity(), 3);
        }
        for (Wish wish : testBannerA.getFourStars()) {
            assertEquals(wish.getRarity(), 4);
        }
        for (Wish wish : testBannerA.getFiveStars()) {
            assertEquals(wish.getRarity(), 5);
        }
    }

    @Test
    void testWish() {
        Wish wish = testBannerA.makeWish();
        assertEquals(testBannerA.getWishCount(), 1);
        assertTrue(wishPoolA.contains(wish));

        if (wish.getRarity() == 3) {
            assertEquals(testBannerA.getFiveStarPity(), 1);
            assertEquals(testBannerA.getFourStarPity(), 1);
        } else if (wish.getRarity() == 4) {
            assertEquals(testBannerA.getFiveStarPity(), 1);
            assertEquals(testBannerA.getFourStarPity(), 0);
        } else if (wish.getRarity() == 5) {
            assertEquals(testBannerA.getFiveStarPity(), 0);
            assertEquals(testBannerA.getFourStarPity(), 1);
        }
    }

    @Test
    void testWishMany() {
        for (int i = 1; i <= 100; i++) {
            Wish wish = testBannerB.makeWish();
            assertEquals(testBannerB.getWishCount(), i);
            assertTrue(wishPoolB.contains(wish));

            if (wish.getRarity() == 4) {
                assertEquals(testBannerA.getFourStarPity(), 0);
            } else if (wish.getRarity() == 5) {
                assertEquals(testBannerA.getFiveStarPity(), 0);
            }

            if (wish instanceof Character) {
                assertTrue(((Character) wish).getVision() != null);
                assertTrue(((Character) wish).getWeapon() != null);
            } else {
                assertTrue(((Weapon) wish).getWeaponType() != null);
            }
        }
    }

    @Test
    void testWishWithCount() {
        List<Wish> wishes = testBannerB.makeWish(50);
        assertEquals(testBannerB.getWishCount(), 50);
        assertEquals(wishes.size(), 50);

        int fivePity = 0;
        int fourPity = 0;
        for (Wish wish : wishes) {
            assertTrue(wishPoolB.contains(wish));

            fourPity++;
            fivePity++;
            if (wish.getRarity() == 4) {
                fourPity = 0;
            } else if (wish.getRarity() == 5) {
                fivePity = 0;
            }
        }
        assertEquals(fivePity, testBannerB.getFiveStarPity());
        assertEquals(fourPity, testBannerB.getFourStarPity());
    }

    @Test
    void testWishFourStarPity() {
        boolean seenFourStar = false;

        for (int i = 1; i <= Banner.FOUR_STAR_MAX_PITY; i++) {
            Wish wish = testBannerB.makeWish();
            assertEquals(testBannerB.getWishCount(), i);
            assertTrue(wishPoolB.contains(wish));

            seenFourStar |= (wish.getRarity() == 4);
        }
        assertTrue(seenFourStar);
    }

    @Test
    void testWishFiveStarPity() {
        boolean seenFiveStar = false;

        for (int i = 1; i <= Banner.FIVE_STAR_MAX_PITY; i++) {
            Wish wish = testBannerB.makeWish();
            assertEquals(testBannerB.getWishCount(), i);
            assertTrue(wishPoolB.contains(wish));

            seenFiveStar |= (wish.getRarity() == 4);
        }
        assertTrue(seenFiveStar);
    }
}