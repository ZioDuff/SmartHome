package org.generation.italy.looselycoupled;

public abstract class Component {

    protected Mediator mediator;

    protected Component(Mediator mediator) {
        this.mediator = mediator;
    }
}
