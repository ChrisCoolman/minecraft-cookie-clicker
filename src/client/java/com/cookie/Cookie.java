package com.cookie;

import java.util.ArrayList;
import java.util.List;

public class Cookie {
    public static double cookies;
    public static double cookiesPerClick;
    public static double cookiesPerSecond;


    // Init buildings
    public static final List<Building> BUILDINGS = new ArrayList<>();

    public static void start() {
        // Init values - should not run if saves are added
        cookies = 0;
        cookiesPerClick = 1;
        cookiesPerSecond = 0;

        // Add buildings
        BUILDINGS.clear();
        BUILDINGS.add(new Building("Cursor", 15, 15, 0.1));
        BUILDINGS.add(new Building("Grandma", 100, 100, 1));
        BUILDINGS.add(new Building("Farm", 1100, 1100, 8));
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
        cookies += cookiesPerSecond;
        cookies = ((double) Math.round(cookies * 100) / 100);
    }

    public static String format(double num) {

        if(num > 1000) {
            num = num / 1000;
            return String.format("%.2f", num) + " thousand";
        }
        return String.format("%.2f", num);
    }

}
