package com.cookie;

public class Cookie {
    public static double cookies;
    public static double cookiesPerClick;
    public static double cookiesPerSecond;


    // Init buildings
    public static Building cursor = new Building("Cursor", 15, 15, 0.1);
    public static Building grandma = new Building("Grandma", 100, 100, 1);

    public static void start() {
        // Init values - should not run if saves are added
        cookies = 0;
        cookiesPerClick = 1;
        cookiesPerSecond = 0;
    }

    public static void mouseDown() {
        cookies += cookiesPerClick;
    }

    public static void mouseUp() {

    }

    public static void tick() {
        cookies += cookiesPerSecond;
        cookies = (double) Math.round(cookies * 100) / 100;
    }

    public static String format(double num) {
        if(num > 1000) {
            num = num / 1000;
            return Double.toString(num) + " thousand";
        }
        return Double.toString(num);
    }

}
