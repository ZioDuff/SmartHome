package org.generation.italy.tightlycoupled;

public class Main {

    public static void main(String[] args) {
        Light light = new Light();
        Heating heating = new Heating();
        Camera camera = new Camera(light, heating);

        camera.motionDetected();
        camera.noMotionDetected();
    }
}
