package org.generation.italy.looselycoupled;

public class Camera extends Component {

    public Camera(Mediator mediator) {
        super(mediator);
    }

    public void motionDetected() {
        System.out.println("Camera: motion detected");
        mediator.notify(this, Event.MOTION_DETECTED);
    }

    public void noMotionDetected() {
        System.out.println("Camera: no motion detected");
        mediator.notify(this, Event.NO_MOTION_DETECTED);
    }
}
