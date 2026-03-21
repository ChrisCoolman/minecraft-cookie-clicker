package com.cookie;

import java.math.BigDecimal;

public class ClickUpgrade {
    public String name;
    public BigDecimal unlockCondition;
    public BigDecimal price;
    public boolean purchased;

    public ClickUpgrade(String name, BigDecimal unlockCondition, BigDecimal price, boolean purchased) {
        this.name = name;
        this.unlockCondition = unlockCondition;
        this.price = price;
        this.purchased = purchased;
    }

    public void purchase() {
        if(Cookie.cookies.compareTo(price) >= 0 && !purchased) {
            Cookie.cookies = Cookie.cookies.subtract(price);
            Cookie.clickCps += 0.1;
        }
    }

}
