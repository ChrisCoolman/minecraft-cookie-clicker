package com.cookie;

import java.util.ArrayList;
import java.util.List;

public class Cookie {
    public static double cookies;
    public static double cookiesPerClick;
    public static double cookiesPerSecond;


    // Init buildings
    public static final List<Building> BUILDINGS = new ArrayList<>();
    // Init upgrades
    public static final List<Upgrade> TOTAL_UPGRADES = new ArrayList<>();
    public static List<Upgrade> UPGRADES = new ArrayList<>();

    public static void start() {
        // Init values - should not run if saves are added
        cookies = 0;
        cookiesPerClick = 1;
        cookiesPerSecond = 0;

        // Add buildings
        BUILDINGS.add(new Building("Cursor", 15, 0.1));
        BUILDINGS.add(new Building("Grandma", 100, 1));
        BUILDINGS.add(new Building("Farm", 1100, 8));
        BUILDINGS.add(new Building("Mine", 12000, 47));
        BUILDINGS.add(new Building("Factory", 130000, 260));
        BUILDINGS.add(new Building("Bank", 1400000, 1400));
        BUILDINGS.add(new Building("Temple", 20000000, 7800));
        BUILDINGS.add(new Building("Wizard Tower", 330000000, 44000));

        // Add upgrades

        // Cursor upgrades
        TOTAL_UPGRADES.add(new Upgrade("Reinforced index finger", 100, "Cursor", 2, 1));
        TOTAL_UPGRADES.add(new Upgrade("Carpal tunnel prevention cream", 500, "Cursor", 2, 1));
        TOTAL_UPGRADES.add(new Upgrade("Ambidextrous", 10000, "Cursor", 2, 10));
        // Grandma upgrades
        TOTAL_UPGRADES.add(new Upgrade("Forwards from grandma", 1000, "Grandma", 2, 1));
        TOTAL_UPGRADES.add(new Upgrade("Steel-plated rolling pins", 5000, "Grandma", 2, 5));
        TOTAL_UPGRADES.add(new Upgrade("Lubricated dentures", 50000, "Grandma", 2, 25));
        TOTAL_UPGRADES.add(new Upgrade("Prune juice", 5000000, "Grandma", 2, 50));
        TOTAL_UPGRADES.add(new Upgrade("Double-thick glasses", 500000000, "Grandma", 2, 100));
        //TOTAL_UPGRADES.add(new Upgrade("Aging agents", 50000000000, "Grandma", 2, 5));
        //TOTAL_UPGRADES.add(new Upgrade("Xtreme walkers", 50000000000000, "Grandma", 2, 5));
        // Farm upgrades
        TOTAL_UPGRADES.add(new Upgrade("Cheap hoes", 11000, "Farm", 2, 1));
        TOTAL_UPGRADES.add(new Upgrade("Fertilizer", 55000, "Farm", 2, 5));
        TOTAL_UPGRADES.add(new Upgrade("Cookie trees", 550000, "Farm", 2, 25));
        TOTAL_UPGRADES.add(new Upgrade("Genetically-modified cookies", 55000000, "Farm", 2, 50));
        // Mine upgrades
        TOTAL_UPGRADES.add(new Upgrade("Sugar gas", 120000, "Farm", 2, 1));
        TOTAL_UPGRADES.add(new Upgrade("Megadrill", 600000, "Farm", 2, 1));
        TOTAL_UPGRADES.add(new Upgrade("Ultradrill", 6000000, "Farm", 2, 1));
        TOTAL_UPGRADES.add(new Upgrade("Ultimadrill", 600000000, "Farm", 2, 1));
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
        cookies += cookiesPerClick;
    }

    public static void mouseUp() {

    }

    public static void tick() {
        cookies += calculateCookiesPerSecond();
        cookies = ((double) Math.round(cookies * 100) / 100);


        // Gets ever upgrade
        for(int i = 0; i < TOTAL_UPGRADES.size(); i++) {
            // If you have enough buildings and the upgrade is not unlocked give the upgrade
            if(getBuilding(TOTAL_UPGRADES.get(i).building).amountPurchased >= TOTAL_UPGRADES.get(i).unlockBuildingCount && !TOTAL_UPGRADES.get(i).isUnlocked) {
                UPGRADES.add(TOTAL_UPGRADES.get(i));
                TOTAL_UPGRADES.get(i).isUnlocked = true;
            }
        }
    }

    public static String format(double num) {

        if(num > 1000) {
            num = num / 1000;
            return Double.toString(num) + " thousand";
        }
        return Double.toString(num);
    }

    public static double calculateCookiesPerSecond() {
        double num = 0;
        for(int i = 0; i < BUILDINGS.size(); i++) {
            num += BUILDINGS.get(i).amountPurchased * BUILDINGS.get(i).baseCookiesPerSecond;
        }
        cookiesPerSecond = num;
        return num;
    }

}
