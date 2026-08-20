package org.generation.italy.looselycoupled;

import java.util.HashMap;
import java.util.Map;

// Questa classe è il "Mediator concreto": è l'unico punto in cui i componenti
// della casa (Light, Heating, Camera...) vengono messi in comunicazione tra loro.
// I componenti non si conoscono mai direttamente: parlano solo con questa classe.
public class SmartHomeMediator implements Mediator {

    // Qui teniamo traccia di tutti i componenti registrati, usando la loro
    // classe (es. Light.class) come "etichetta" per ritrovarli in seguito.
    // Esempio: components.get(Light.class) restituisce l'istanza di Light.
    private final Map<Class<? extends Component>, Component> components = new HashMap<>();

    // Metodo chiamato una volta sola, all'avvio, per "iscrivere" un componente
    // al mediator. Da questo momento il mediator sa che quel componente esiste
    // e potrà comandarlo quando serve.
    @Override
    public void register(Component component) {
        components.put(component.getClass(), component);
    }

    // Metodo di comodo, usato solo internamente da questa classe, per recuperare
    // un componente già registrato a partire dal suo tipo (es. get(Light.class)).
    // Evita di dover fare cast manuali ogni volta che ci serve un componente.
    private <T extends Component> T get(Class<T> type) {
        return type.cast(components.get(type));
    }

    // Questo è il cuore del pattern: qui arrivano tutte le notifiche dai
    // componenti ("è successo questo evento") e il mediator decide cosa fare.
    // I componenti che generano l'evento non sanno chi altro verrà coinvolto:
    // è il mediator a conoscere le regole della casa.
    @Override
    public void notify(Component sender, Event event) {
        switch (event) {
            // Se la telecamera rileva movimento, accendiamo luce e riscaldamento.
            case MOTION_DETECTED -> {
                get(Light.class).turnOn();
                get(Heating.class).turnOn();
            }
            // Se la telecamera non rileva più movimento, spegniamo entrambi.
            case NO_MOTION_DETECTED -> {
                get(Light.class).turnOff();
                get(Heating.class).turnOff();
            }
            // Se il riscaldamento segnala un guasto, lo spegniamo per sicurezza
            // e usiamo la luce per avvisare (lampeggio) che c'è un problema.
            case HEATING_FAILURE -> {
                get(Heating.class).turnOff();
                get(Light.class).blink();
            }
            // Se arrivasse un evento che non conosciamo, meglio segnalarlo subito
            // con un errore chiaro piuttosto che ignorarlo silenziosamente.
            default -> throw new IllegalArgumentException("Unknown event: " + event);
        }
    }
}
