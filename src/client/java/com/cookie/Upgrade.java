package com.cookie;

public class Upgrade {
    public String name;
    public double price;
    public boolean purchased = false;
    public String building;
    public double multiplier;
    public double unlockCookies;
    public boolean isUnlocked = false;
    public double unlockBuildingCount;
    public int unlockFactor;

    public Upgrade(String name, double price, String building, double multiplier, double unlockBuildingCount) {
        this.name = name;
        this.price = price;
        this.building = building;
        this.multiplier = multiplier;
        this.unlockBuildingCount = unlockBuildingCount;
    }

    public void purchase() {
        if (!purchased && Cookie.cookies >= price) {
            Cookie.cookies -= price;
            purchased = true;

            Cookie.getBuilding(this.building).baseCookiesPerSecond *= multiplier;
        }
    }
}
