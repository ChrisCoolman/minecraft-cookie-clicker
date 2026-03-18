package com.cookie;

import java.math.BigDecimal;

public class Upgrade {
    public String name;
    public BigDecimal price;
    public boolean purchased = false;
    public String building;
    public double multiplier;
    public boolean isUnlocked = false;
    public double unlockBuildingCount;

    public Upgrade(String name, double price, String building, double multiplier, double unlockBuildingCount) {
        this.name = name;
        this.price = BigDecimal.valueOf(price);
        this.building = building;
        this.multiplier = multiplier;
        this.unlockBuildingCount = unlockBuildingCount;
    }

    public void purchase() {
        if (!purchased && (Cookie.cookies.compareTo(price) == 1 || Cookie.cookies.compareTo(price) == 0)) {
            Cookie.cookies = Cookie.cookies.subtract(price);
            purchased = true;

            Cookie.getBuilding(this.building).baseCookiesPerSecond *= multiplier;
        }
    }

    public String generateTooltip() {
        return building + "s are twice as efficient";
    }
}
