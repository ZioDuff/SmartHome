# SmartHome — Mediator Pattern

Progetto didattico che mostra il **Mediator Pattern** confrontando due implementazioni dello stesso scenario (una smart home con telecamera, luce e riscaldamento): una **senza** il pattern (`tightlycoupled`) e una **con** il pattern (`looselycoupled`).

## Cos'è il Mediator Pattern

Il Mediator è un design pattern comportamentale (GoF) che **centralizza la comunicazione tra un gruppo di oggetti** (detti *colleague*) in un unico oggetto mediatore, invece di farli comunicare direttamente tra loro.

Idea chiave: invece di una rete di riferimenti incrociati fra tutti gli oggetti (ognuno che conosce e chiama direttamente gli altri), ogni oggetto conosce **solo il mediatore**. Quando succede qualcosa, l'oggetto lo comunica al mediatore, e sarà il mediatore a decidere quali altri oggetti coinvolgere e come.

```
Senza Mediator                    Con Mediator

 A ───── B                          A         B
 │ ╲   ╱ │                           ╲       ╱
 │  ╲ ╱  │                            ╲     ╱
 │   X   │                         [ Mediator ]
 │  ╱ ╲  │                            ╱     ╲
 │ ╱   ╲ │                           ╱       ╲
 C ───── D                          C         D
```

## Che problema risolve

Quando più oggetti devono coordinarsi, la soluzione "naturale" è farli comunicare direttamente: ogni oggetto tiene un riferimento agli altri e li richiama quando serve. Su piccola scala funziona, ma non scala:

- **Accoppiamento a rete (N×N)**: con *n* oggetti che comunicano tutti tra loro, il numero di collegamenti diretti cresce rapidamente. Ogni oggetto finisce per conoscere i dettagli di molti altri.
- **Difficoltà a modificare o riusare i singoli oggetti**: un oggetto che chiama direttamente altri tre oggetti non può essere riusato altrove senza portarsi dietro quelle dipendenze.
- **Logica di coordinamento sparsa**: le regole "quando succede X, fai Y e Z" finiscono duplicate o distribuite in più classi, invece di stare in un unico posto leggibile.
- **Basso rispetto dell'Open/Closed Principle**: aggiungere un nuovo oggetto al gruppo, o una nuova regola di coordinamento, spesso significa modificare più classi esistenti.

Il Mediator risolve questi problemi spostando **tutta la logica di coordinamento in un solo oggetto**. I singoli componenti diventano più semplici (sanno solo "notificare un evento al mediatore"), più riusabili (non dipendono l'uno dall'altro) e più facili da testare in isolamento.

## I due package a confronto

### `tightlycoupled` — senza Mediator

```java
public class Camera {

    private final Light light;
    private final Heating heating;

    public Camera(Light light, Heating heating) {
        this.light = light;
        this.heating = heating;
    }

    public void motionDetected() {
        light.turnOn();
        heating.turnOn();
    }
}
```

`Camera` conosce direttamente `Light` e `Heating` e li richiama esplicitamente. Problemi:

- `Camera` dipende da ogni singolo dispositivo coinvolto: se domani si aggiunge un allarme o una serranda, bisogna **modificare la classe `Camera`**.
- La logica "cosa succede quando la telecamera rileva movimento" è scritta *dentro* `Camera`, non in un posto dedicato: non è riusabile per un altro sensore che voglia lo stesso comportamento.
- Se un altro componente (es. `Heating`) dovesse a sua volta influenzare `Light`, servirebbe dargli anche a lui un riferimento diretto a `Light`, aumentando ulteriormente l'accoppiamento a rete.

### `looselycoupled` — con Mediator

```java
public class Camera extends Component {

    public Camera(Mediator mediator) {
        super(mediator);
    }

    public void motionDetected() {
        mediator.notify(this, Event.MOTION_DETECTED);
    }
}
```

```java
public class SmartHomeMediator implements Mediator {

    @Override
    public void notify(Component sender, Event event) {
        switch (event) {
            case MOTION_DETECTED -> {
                get(Light.class).turnOn();
                get(Heating.class).turnOn();
            }
            // ...
        }
    }
}
```

`Camera` non conosce più `Light` né `Heating`: sa solo di avere un `Mediator` a cui notificare "è successo questo evento". È `SmartHomeMediator` a decidere, in un unico punto, quali dispositivi coinvolgere e come.

Elementi chiave dell'implementazione:

| Elemento | Ruolo |
|---|---|
| `Mediator` (interfaccia) | Contratto comune: `notify(sender, event)` e `register(component)`. |
| `SmartHomeMediator` | Il mediatore concreto: conosce tutti i componenti registrati e contiene le regole di coordinamento. |
| `Component` (astratta) | Classe base dei *colleague*: tiene un riferimento al `Mediator`, mai agli altri componenti. |
| `Event` (enum) | Gli eventi che i componenti possono notificare, con sicurezza a compile-time (niente stringhe libere). |
| `Light`, `Heating`, `Camera` | I *colleague* concreti: eseguono azioni e/o notificano eventi, ma non si conoscono tra loro. |

Un dettaglio interessante di questa implementazione: la comunicazione è **bidirezionale**. Non solo `Camera` notifica il mediatore, ma anche `Heating.reportFailure()` lo fa — simulando un guasto che il mediatore gestisce spegnendo il riscaldamento e facendo lampeggiare la luce (`Light.blink()`), senza che `Heating` sappia nulla dell'esistenza di `Light`.

## Confronto diretto

| Aspetto | `tightlycoupled` | `looselycoupled` |
|---|---|---|
| Chi conosce chi | Ogni componente conosce direttamente gli altri componenti coinvolti | Ogni componente conosce solo il `Mediator` |
| Dove vive la logica di coordinamento | Sparsa dentro i singoli componenti (es. in `Camera`) | Centralizzata in `SmartHomeMediator` |
| Aggiungere un nuovo dispositivo | Richiede modificare i componenti che devono interagire con lui | Basta registrarlo nel mediatore (`register(...)`) e aggiungere/estendere le regole nel mediatore |
| Riuso dei componenti in altri contesti | Difficile: portano con sé dipendenze dirette | Più semplice: dipendono solo dall'interfaccia `Mediator` |
| Testabilità in isolamento | Complessa: servono le dipendenze reali o dei mock passati nel costruttore | Più semplice: basta un `Mediator` fittizio |
| Accoppiamento | Alto (a rete, N×N) | Basso (a stella, tutti verso il mediatore) |

## Come eseguire gli esempi

```bash
javac -d target/classes src/main/java/org/generation/italy/tightlycoupled/*.java
java -cp target/classes org.generation.italy.tightlycoupled.Main
```

```bash
javac -d target/classes src/main/java/org/generation/italy/looselycoupled/*.java
java -cp target/classes org.generation.italy.looselycoupled.Main
```
