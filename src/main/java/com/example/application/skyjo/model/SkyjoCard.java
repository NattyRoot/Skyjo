package com.example.application.skyjo.model;

public class SkyjoCard {
    private final int value;
    private boolean visible = false;

    public SkyjoCard(int value, boolean visible) {
        this.value = value;
        this.visible = visible;
    }

    public int getValue() {
        return value;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public String toString() {
        return this.value + "";
    }
}
