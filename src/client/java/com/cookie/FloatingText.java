package com.cookie;

public class FloatingText {
    public double x;
    public double y;
    public double age;
    public String text;

    public FloatingText(double x, double y, String text) {
        this.x = x;
        this.y = y;
        this.text = text;
    }

    public void tick() {
        this.y -= 1.0;
        this.age += 0.05;
    }
}
