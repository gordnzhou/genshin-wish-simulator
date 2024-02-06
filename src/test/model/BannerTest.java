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

    private List<Wish> wishPoolA;
    private List<Wish> wishPoolB;

    @BeforeEach
    void runBefore() {
        wishPoolA = new ArrayList<>();
        wishPoolA.add(new Character(5, "Jean", Character.Element.ANEMO));
        wishPoolA.add(new Character(4, "Amber", Character.Element.PYRO));
        wishPoolA.add(new Weapon(3, "Sword", Weapon.WeaponType.SWORD));

        wishPoolB = new ArrayList<>();
        wishPoolB.addAll(wishPoolA);
        wishPoolB.add(new Character(5, "Diluc", Character.Element.PYRO));
        wishPoolB.add(new Character(4, "Lisa", Character.Element.ELECTRO));
        wishPoolB.add(new Character(4, "Kaeya", Character.Element.CRYO));
        wishPoolB.add(new Weapon(3, "aa", Weapon.WeaponType.GREATSWORD));
        wishPoolB.add(new Weapon(3, "bb", Weapon.WeaponType.SPEAR));
        wishPoolB.add(new Weapon(3, "cc", Weapon.WeaponType.BOW));
        wishPoolB.add(new Weapon(5, "dd", Weapon.WeaponType.CATALYST));

        testBannerA = new Banner("Banner A", wishPoolA);
        testBannerB = new Banner("Banner B", wishPoolB);
    }

    @Test
    void testConstructor() {
        assertEquals(testBannerA.getFiveStarPity(), 0);
        assertEquals(testBannerA.getFourStarPity(), 0);
        assertEquals(testBannerA.getWishCount(), 0);

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