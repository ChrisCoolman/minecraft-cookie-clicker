package com.cookie;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;

import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class Cookie {
    public static BigDecimal cookies;
    public static BigDecimal cookiesPerClick;
    public static BigDecimal cookiesPerSecond;
    public static BigDecimal maxCookies;
    public static BigDecimal handMadeCookies;
    public static double clickCps;
    public static Double milk;
    public static Double milkFactor;


    public static final BigDecimal million = BigDecimal.valueOf(Math.pow(10, 6));
    public static final BigDecimal billion = BigDecimal.valueOf(Math.pow(10, 9));
    public static final BigDecimal trillion = BigDecimal.valueOf(Math.pow(10, 12));


    // Init buildings
    public static final List<Building> BUILDINGS = new ArrayList<>();
    // Init upgrades
    public static final List<Upgrade> TOTAL_UPGRADES = new ArrayList<>();
    public static List<Upgrade> UPGRADES = new ArrayList<>();

    public static final List<Kitten> TOTAL_KITTENS = new ArrayList<>();
    public static List<Kitten> KITTENS = new ArrayList<>();

    public static final List<ClickUpgrade> TOTAL_CLICK_UPGRADE = new ArrayList<>();
    public static List<ClickUpgrade> CLICK_UPGRADES = new ArrayList<>();

    public static void start() {
        // Init values - should not run if saves are added
        cookies = BigDecimal.valueOf(50000);
        cookiesPerClick = BigDecimal.ONE;
        cookiesPerSecond = BigDecimal.ZERO;
        maxCookies = BigDecimal.ZERO;
        handMadeCookies = BigDecimal.valueOf(1000);
        clickCps = 0;
        milkFactor = 0.0;

        // Add buildings
        BUILDINGS.clear();
        BUILDINGS.add(new Building("Cursor", 15, 0.1, BigDecimal.valueOf(0)));
        BUILDINGS.add(new Building("Grandma", 100, 1, BigDecimal.valueOf(0)));
        BUILDINGS.add(new Building("Farm", 1100, 8, BigDecimal.valueOf(100)));
        BUILDINGS.add(new Building("Mine", 12000, 47, BigDecimal.valueOf(1000)));
        BUILDINGS.add(new Building("Factory", 130000, 260, BigDecimal.valueOf(10000)));
        BUILDINGS.add(new Building("Bank", 1400000, 1400, BigDecimal.valueOf(100000)));
        BUILDINGS.add(new Building("Temple", 20000000, 7800, BigDecimal.valueOf(1000000)));
        BUILDINGS.add(new Building("Wizard Tower", 330000000, 44000, BigDecimal.valueOf(10000000)));

        // Add upgrades
        UPGRADES.clear();
        TOTAL_UPGRADES.clear();
        TOTAL_KITTENS.clear();
        TOTAL_CLICK_UPGRADE.clear();

        TOTAL_CLICK_UPGRADE.add(new ClickUpgrade("Plastic Mouse", BigDecimal.valueOf(1000), BigDecimal.valueOf(50000), false));
        TOTAL_CLICK_UPGRADE.add(new ClickUpgrade("Iron Mouse", BigDecimal.valueOf(100000), million.multiply(BigDecimal.valueOf(5)), false));
        TOTAL_CLICK_UPGRADE.add(new ClickUpgrade("Titanium Mouse", million.multiply(BigDecimal.valueOf(10)), million.multiply(BigDecimal.valueOf(500)), false));
        TOTAL_CLICK_UPGRADE.add(new ClickUpgrade("Adamantium Mouse", billion, billion.multiply(BigDecimal.valueOf(50)), false));
        TOTAL_CLICK_UPGRADE.add(new ClickUpgrade("Unobtainium Mouse", billion.multiply(BigDecimal.valueOf(100)), trillion.multiply(BigDecimal.valueOf(5)), false));
        TOTAL_CLICK_UPGRADE.add(new ClickUpgrade("Eludium Mouse", trillion.multiply(BigDecimal.valueOf(10)), trillion.multiply(BigDecimal.valueOf(500)), false));

        TOTAL_KITTENS.add(new Kitten("Kitten helpers", (million.multiply(BigDecimal.valueOf(9))), 0.52, 0.1, false));
        TOTAL_KITTENS.add(new Kitten("Kitten workers", (billion.multiply(BigDecimal.valueOf(9))), 1.0, 0.125, false));
        TOTAL_KITTENS.add(new Kitten("Kitten engineers", (trillion.multiply(BigDecimal.valueOf(90))), 2.0, 0.15, false));

        // Cursor upgrades
        TOTAL_UPGRADES.add(new Upgrade("Reinforced index finger", BigDecimal.valueOf(100), "Cursor", 2, 1));
        TOTAL_UPGRADES.add(new Upgrade("Carpal tunnel prevention cream", BigDecimal.valueOf(500), "Cursor", 2, 1));
        TOTAL_UPGRADES.add(new Upgrade("Ambidextrous", BigDecimal.valueOf(10000), "Cursor", 2, 10));
        // Grandma upgrades
        TOTAL_UPGRADES.add(new Upgrade("Forwards from grandma", BigDecimal.valueOf(1000), "Grandma", 2, 1));
        TOTAL_UPGRADES.add(new Upgrade("Steel-plated rolling pins", BigDecimal.valueOf(5000), "Grandma", 2, 5));
        TOTAL_UPGRADES.add(new Upgrade("Lubricated dentures", BigDecimal.valueOf(50000), "Grandma", 2, 25));
        TOTAL_UPGRADES.add(new Upgrade("Prune juice", million.multiply(BigDecimal.valueOf(5)), "Grandma", 2, 50));
        TOTAL_UPGRADES.add(new Upgrade("Double-thick glasses", million.multiply(BigDecimal.valueOf(500)), "Grandma", 2, 100));
        TOTAL_UPGRADES.add(new Upgrade("Aging agents", billion.multiply(BigDecimal.valueOf(50)), "Grandma", 2, 150));
        TOTAL_UPGRADES.add(new Upgrade("Xtreme walkers", trillion.multiply(BigDecimal.valueOf(50)), "Grandma", 2, 200));
        // Farm upgrades
        TOTAL_UPGRADES.add(new Upgrade("Cheap hoes", BigDecimal.valueOf(11000), "Farm", 2, 1));
        TOTAL_UPGRADES.add(new Upgrade("Fertilizer", BigDecimal.valueOf(55000), "Farm", 2, 5));
        TOTAL_UPGRADES.add(new Upgrade("Cookie trees", BigDecimal.valueOf(550000), "Farm", 2, 25));
        TOTAL_UPGRADES.add(new Upgrade("Genetically-modified cookies", million.multiply(BigDecimal.valueOf(55)), "Farm", 2, 50));
        TOTAL_UPGRADES.add(new Upgrade("Gingerbread scarecrows", billion.multiply(BigDecimal.valueOf(5.5)), "Farm", 2, 100));
        TOTAL_UPGRADES.add(new Upgrade("Pulsar sprinklers", billion.multiply(BigDecimal.valueOf(550)), "Farm", 2, 150));
        // Mine upgrades
        TOTAL_UPGRADES.add(new Upgrade("Sugar gas", BigDecimal.valueOf(120000), "Farm", 2, 1));
        TOTAL_UPGRADES.add(new Upgrade("Megadrill", BigDecimal.valueOf(600000), "Farm", 2, 5));
        TOTAL_UPGRADES.add(new Upgrade("Ultradrill", million.multiply(BigDecimal.valueOf(6)), "Farm", 2, 25));
        TOTAL_UPGRADES.add(new Upgrade("Ultimadrill", million.multiply(BigDecimal.valueOf(600)), "Farm", 2, 50));
        // Factory upgrades
        TOTAL_UPGRADES.add(new Upgrade("Sturdier conveyor belts", million.multiply(BigDecimal.valueOf(1.3)), "Farm", 2, 1));
        TOTAL_UPGRADES.add(new Upgrade("Child labor", million.multiply(BigDecimal.valueOf(6.5)), "Farm", 2, 1));
        TOTAL_UPGRADES.add(new Upgrade("Sweatshop", million.multiply(BigDecimal.valueOf(65)), "Farm", 2, 1));
        TOTAL_UPGRADES.add(new Upgrade("Radium reactors", billion.multiply(BigDecimal.valueOf(6.5)), "Farm", 2, 1));
        TOTAL_UPGRADES.add(new Upgrade("Recombobulators", billion.multiply(BigDecimal.valueOf(650)), "Farm", 2, 1));
        TOTAL_UPGRADES.add(new Upgrade("Deep-bake process", trillion.multiply(BigDecimal.valueOf(65)), "Farm", 2, 1));
        // Bank upgrades
    }

    // Takes a name as input and finds a building with that name
    public static Building getBuilding(String buildingName) {
        for (Building building : BUILDINGS) {
            if (building.name.equals(buildingName)) {
                return building;
            }
        }
        /* default is return cursor building which is always at index 0 */
        return BUILDINGS.getFirst();
    }

    public static void mouseDown() {
        cookies = cookies.add(cookiesPerClick);
        handMadeCookies = handMadeCookies.add(cookiesPerClick);
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.GENERIC_EAT, 0.8f + (float) Math.random() * 0.4f));
    }

    public static void mouseUp() {

    }

    // Runs every second
    public static void tick() {
        BigDecimal baseCPS = calcBaseCPS();

        milkFactor = 0.0;
        for(Kitten k : KITTENS) {
            milkFactor += (getMilk() * k.milkFactor);
        }

        cookiesPerSecond = baseCPS.multiply(BigDecimal.valueOf(1 + milkFactor)).setScale(2, RoundingMode.HALF_UP);

        cookies = cookies.add(cookiesPerSecond.divide(BigDecimal.valueOf(20), 4, RoundingMode.HALF_UP));

        if(cookies.compareTo(maxCookies) >= 0) {
            maxCookies = cookies;
        }

        BigDecimal bonusCPC = cookiesPerSecond.multiply(BigDecimal.valueOf(clickCps));
        cookiesPerClick = BigDecimal.ONE.add(bonusCPC).setScale(2, RoundingMode.HALF_UP);

        checkUnlocks();

    }

    public static void checkUnlocks() {
        // Gets every upgrade
        for (Upgrade u : TOTAL_UPGRADES) {
            if(!u.isUnlocked && getBuilding(u.building).amountPurchased >= u.unlockBuildingCount) {
                UPGRADES.add(u);
                u.isUnlocked = true;
            }
        }
        // Gets every kitten
        for (Kitten k : TOTAL_KITTENS) {
            if(!k.purchased && getMilk() >= k.unlockMilk) {
                KITTENS.add(k);
            }
        }
        // Gets every click upgrade
        for(ClickUpgrade cu : TOTAL_CLICK_UPGRADE) {
            if(!cu.purchased && handMadeCookies.compareTo(cu.unlockCondition) >= 0) {
                cu.purchased = true;
                clickCps += 0.01;
                CLICK_UPGRADES.add(cu);
            }
        }
    }

    public static String format(BigDecimal num) {

        // Compare to returns 1 for greater than
        if (num.compareTo(trillion) >= 0) {
            num = num.divide(trillion, 2, RoundingMode.HALF_UP);
            return num.toString() + " trillion";
        }
        else if (num.compareTo(billion) >= 0) {
            num = num.divide(billion, 2, RoundingMode.HALF_UP);
            return num.toString() + " billion";
        } else if (num.compareTo(million) >= 0) {
            num = num.divide(million, 2, RoundingMode.HALF_UP);
            return num.toString() + " million";
        }
        return num.toString();
    }

    public static BigDecimal calculateCookiesPerSecond() {
        BigDecimal num = BigDecimal.valueOf(0);
        for (int i = 0; i < BUILDINGS.size(); i++) {
            num = num.add(BigDecimal.valueOf(BUILDINGS.get(i).amountPurchased * BUILDINGS.get(i).baseCookiesPerSecond));
        }
        cookiesPerSecond = num;
        return num;
    }

    public static Double getMilk() {
        // Maps cookies from 1000 to 1 trillion to 0 - 2 for the percent of milk
        // I will not add achievements
        if (maxCookies.compareTo(BigDecimal.valueOf(1000)) <= 0) {
            return 0d;
        } else {
            double log = Math.log10(maxCookies.doubleValue());
            double progress = (log - 3.0) / 9.0;
            return Math.clamp(progress, 0.0, 2.0);
        }
    }

    public static int milkSize(int height, double milk) {
        if (maxCookies.compareTo(BigDecimal.valueOf(1000)) <= 0) {
            return height;
        } else {
            // 40%
            double heightPerc = Math.min(milk, 1.0);
            int maxHeight = (int) (height / 4);
            int currentHeight = (int) (heightPerc * maxHeight);
            return height - currentHeight;
        }
    }

    public static BigDecimal calcBaseCPS() {
        BigDecimal total = BigDecimal.ZERO;
        for(Building b : BUILDINGS) {
            total = total.add(BigDecimal.valueOf(b.amountPurchased).multiply(BigDecimal.valueOf(b.baseCookiesPerSecond)));
        }
        return total;
    }
}

