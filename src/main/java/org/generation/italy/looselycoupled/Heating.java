package org.generation.italy.looselycoupled;

public class Heating extends Component {

    private boolean on;

    public Heating(Mediator mediator) {
        super(mediator);
    }

    public void turnOn() {
        on = true;
        System.out.println("Heating: turned ON");
    }

    public void turnOff() {
        on = false;
        System.out.println("Heating: turned OFF");
    }

    public void reportFailure() {
        System.out.println("Heating: malfunction detected!");
        mediator.notify(this, Event.HEATING_FAILURE);
    }

    public boolean isOn() {
        return on;
    }
}
