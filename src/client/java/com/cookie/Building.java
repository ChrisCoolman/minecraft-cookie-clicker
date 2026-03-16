package com.cookie;

public class Building {

    public static String name;
    public static double price;
    public static double basePrice;
    public static int amountPurchased;
    public static double baseCookiesPerSecond;

    public Building(String name, double price, double basePrice, double baseCookiesPerSecond) {
        Building.name = name;
        Building.price = price;
        Building.basePrice = basePrice;
        Building.baseCookiesPerSecond = baseCookiesPerSecond;
    }

    public static double calculatePrice() {
        return (basePrice * Math.pow(1.15, amountPurchased));
    }

}
