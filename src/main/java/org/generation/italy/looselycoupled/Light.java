package org.generation.italy.looselycoupled;

public class Light extends Component {

    private boolean on;

    public Light(Mediator mediator) {
        super(mediator);
    }

    public void turnOn() {
        on = true;
        System.out.println("Light: turned ON");
    }

    public void turnOff() {
        on = false;
        System.out.println("Light: turned OFF");
    }

    public void blink() {
        System.out.println("Light: blinking (alert)");
    }

    public boolean isOn() {
        return on;
    }
}
