package org.generation.italy.looselycoupled;

public interface Mediator {

    void notify(Component sender, Event event);

    void register(Component component);
}
