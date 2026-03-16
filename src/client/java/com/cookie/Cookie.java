package com.cookie;

public class Cookie {
    public static long cookies;
    public static long cookiesPerClick;
    public static long cookiesPerSecond;


    // Init buildings
    public static Building cursor = new Building("Cursor", 15, 15, 0.1);
    public static Building grandma = new Building("Grandma", 100, 100, 1);

    public static void start() {
        // Init values - should not run if saves are added
        cookies = 0l;
        cookiesPerClick = 1l;
        cookiesPerSecond = 1l;
    }

    public static void mouseDown() {
        cookies += cookiesPerClick;
    }

    public static void mouseUp() {

    }

    public static void tick() {
        cookies += cookiesPerSecond;
    }

    public static String format(long num) {
        if(num > 1000) {
            num = num / 1000;
            return Long.toString(num) + " thousand";
        }
        return Long.toString(num);
    }

}
