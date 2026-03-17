package com.cookie;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Building {

    public String name;
    public BigDecimal price;
    public double basePrice;
    public int amountPurchased;
    public double baseCookiesPerSecond;

    public Building(String name, double price, double baseCookiesPerSecond) {
        this.name = name;
        this.price = BigDecimal.valueOf(price);
        this.basePrice = Double.valueOf(String.valueOf(price));
        this.baseCookiesPerSecond = baseCookiesPerSecond;
        this.amountPurchased = 0;
    }

    public void purchase() {
        if (Cookie.cookies.compareTo(calculatePrice()) == 0 || Cookie.cookies.compareTo(calculatePrice()) == 1) {
            Cookie.cookies = Cookie.cookies.subtract(calculatePrice());
            this.amountPurchased += 1;
        }
    }

    public BigDecimal calculatePrice() {
        BigDecimal num = (BigDecimal.valueOf(this.basePrice).multiply(BigDecimal.valueOf(Math.pow(1.15, this.amountPurchased))));
        return num.setScale(2, RoundingMode.CEILING);
    }

}
