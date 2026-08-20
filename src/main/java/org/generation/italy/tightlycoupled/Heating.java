package org.generation.italy.tightlycoupled;

public class Heating {

    private boolean on;

    public void turnOn() {
        on = true;
        System.out.println("Heating: turned ON");
    }

    public void turnOff() {
        on = false;
        System.out.println("Heating: turned OFF");
    }

    public boolean isOn() {
        return on;
    }
}
