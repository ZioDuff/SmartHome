package org.generation.italy.looselycoupled;

public class Main {

    public static void main(String[] args) {
        SmartHomeMediator mediator = new SmartHomeMediator();

        Light light = new Light(mediator);
        Heating heating = new Heating(mediator);
        Camera camera = new Camera(mediator);

        mediator.register(light);
        mediator.register(heating);
        mediator.register(camera);

        camera.motionDetected();
        camera.noMotionDetected();
        heating.reportFailure();
    }
}
