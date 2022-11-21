package fr.acensi.skyjo.model;

import com.vaadin.flow.component.button.Button;

public class SkyjoCard {
    private int value;
    private String color;
    private boolean visible;
    private Button button;

    public SkyjoCard(int value, String color, Button button) {
        this.value = value;
        this.color = color;
        this.visible = false;
        this.button = button;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        this.button.setClassName(this.button.getClassName() + " color-" + this.color);
        this.button.setText(this.toString());
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }
    @Override
    public String toString() {
        return value + "";
    }
}
