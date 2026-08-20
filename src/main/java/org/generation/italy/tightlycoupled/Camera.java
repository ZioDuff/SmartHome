package org.generation.italy.tightlycoupled;

public class Camera {

    private final Light light;
    private final Heating heating;

    public Camera(Light light, Heating heating) {
        this.light = light;
        this.heating = heating;
    }

    public void motionDetected() {
        System.out.println("Camera: motion detected");
        light.turnOn();
        heating.turnOn();
    }

    public void noMotionDetected() {
        System.out.println("Camera: no motion detected");
        light.turnOff();
        heating.turnOff();
    }
}
