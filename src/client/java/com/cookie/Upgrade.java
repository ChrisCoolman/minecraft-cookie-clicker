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

    public Upgrade(String name, double price, String building, double multiplier, double unlockCookies, double unlockBuildingCount, int unlockFactor) {
        this.name = name;
        this.price = price;
        this.building = building;
        this.multiplier = multiplier;
        this.unlockCookies = unlockCookies;
        this.unlockBuildingCount = unlockBuildingCount;

        //Unlock factor is for cookies or buildings
        // 0 is cookies and 1 is buildings
        this.unlockFactor = unlockFactor;
    }

    public void purchase() {
        if (!purchased && Cookie.cookies >= price) {
            Cookie.cookies -= price;
            purchased = true;

            Cookie.getBuilding(this.building).baseCookiesPerSecond *= multiplier;
        }
    }
}
