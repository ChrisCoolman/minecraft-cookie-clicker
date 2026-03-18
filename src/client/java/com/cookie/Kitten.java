package com.cookie;

import java.math.BigDecimal;

public class Kitten {
    public String name;
    public BigDecimal price;
    public double unlockMilk;
    public double milkFactor;
    public boolean purchased;

    public Kitten(String name, BigDecimal price, double unlockMilk, double milkFactor, boolean purchased) {
        this.name = name;
        this.price = price;
        this.unlockMilk = unlockMilk;
        this.milkFactor = milkFactor;
        this.purchased = purchased;
    }

}
