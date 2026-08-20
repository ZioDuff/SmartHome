package org.generation.italy.tightlycoupled;

public class Light {

    private boolean on;

    public void turnOn() {
        on = true;
        System.out.println("Light: turned ON");
    }

    public void turnOff() {
        on = false;
        System.out.println("Light: turned OFF");
    }

    public boolean isOn() {
        return on;
    }
}
