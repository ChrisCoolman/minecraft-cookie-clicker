package com.cookie;

public class Building {

    public String name;
    public double price;
    public double basePrice;
    public int amountPurchased;
    public double baseCookiesPerSecond;

    public Building(String name, double price, double baseCookiesPerSecond) {
        this.name = name;
        this.price = price;
        this.basePrice = price;
        this.baseCookiesPerSecond = baseCookiesPerSecond;
        this.amountPurchased = 0;
    }

    public void purchase() {
        if (Cookie.cookies >= calculatePrice()) {
            Cookie.cookies -= calculatePrice();
            this.amountPurchased += 1;
        }
    }

    public double calculatePrice() {
        double num = (this.basePrice * Math.pow(1.15, this.amountPurchased));
        return ((double) Math.round(num * 100) / 100);
    }

}
