# UML Diagrams - Use Case: Crea Ordine

Questa directory contiene i diagrammi UML tecnici per il sistema **Habibi Shawarma**, focalizzati sullo Use Case **"Crea Ordine"**.

## 📋 Indice dei Diagrammi

### 1. VOPC - View of Participating Classes

**Scopo**: Mostrare tutte le classi partecipanti allo use case "Crea Ordine" organizzate secondo il pattern BCE (Boundary-Control-Entity).

| File | Formato | Descrizione |
|------|---------|-------------|
| `VOPC_CreaOrdine_Complete.drawio` | Draw.io XML | VOPC completo con tutte le relazioni (apribile in [app.diagrams.net](https://app.diagrams.net)) |
| `VOPC_CreaOrdine_Detailed.puml` | PlantUML | VOPC in formato PlantUML (richiede PlantUML renderer) |
| `vopc_crea_ordine.drawio` | Draw.io XML | Versione base del VOPC |

**Classi Incluse nel VOPC:**

- **Boundary (View)**: `CreaOrdineGUIController`, `CreaOrdineCLIController`
- **Control**: `CreaOrdineFacade`, `CreaOrdineController`, `UsaVoucherController`
- **Bean (DTO)**: `OrdineBean`, `FoodBean`, `VoucherBean`, `RiepilogoOrdineBean`
- **Entity**: `Ordine`, `Food`, `Voucher`, `User`
- **Factory**: `OrdineLazyFactory`, `FoodLazyFactory`, `VoucherLazyFactory`

---

### 2. Design-Level Class Diagram

**Scopo**: Mostrare i Design Patterns e le soluzioni ingegneristiche applicate al sistema.

| File | Formato | Descrizione |
|------|---------|-------------|
| `DesignLevel_CreaOrdine_Patterns.drawio` | Draw.io XML | Diagramma design-level completo con pattern (apribile in [app.diagrams.net](https://app.diagrams.net)) |
| `DesignLevel_CreaOrdine_Patterns.puml` | PlantUML | Diagramma design-level in formato PlantUML |
| `class_diagram_crea_ordine.drawio` | Draw.io XML | Class diagram MVC base |

---

## 🎨 Design Patterns Utilizzati

### Pattern Architetturali

| Pattern | Applicazione | Vantaggi |
|---------|--------------|----------|
| **BCE (Boundary-Control-Entity)** | Architettura principale del sistema | Separazione netta delle responsabilità |
| **MVC** | Layer di presentazione (GUI/CLI) | Facilità di testing, manutenibilità |

### Pattern GoF (Gang of Four)

| Pattern | Tipo | Applicazione | Vantaggi |
|---------|------|--------------|----------|
| **Facade** | Strutturale | `CreaOrdineFacade` semplifica l'accesso al sottosistema | Low coupling tra View e Controller |
| **Decorator** | Strutturale | `DecoratorAddON` per gli add-on ai prodotti (Cipolla, Patatine, etc.) | Open/Closed Principle, composizione dinamica |
| **Strategy** | Comportamentale | `Voucher` interface con `VoucherPercentuale`, `VoucherFisso` | Algoritmi intercambiabili per calcolo sconti |
| **Null Object** | Comportamentale | `NessunVoucher` evita null checks | Codice più pulito, nessun controllo null |
| **Factory Method** | Creazionale | `creaProdottoBase()` nel controller | Creazione centralizzata degli oggetti |
| **Singleton** | Creazionale | `*LazyFactory`, `SessionManager` | Istanza unica globale |
| **Lazy Initialization** | Creazionale | `*LazyFactory` con cache | Performance, lazy loading dal DB |

---

## 📐 Principi GRASP Applicati

| Principio | Applicazione |
|-----------|--------------|
| **Controller** | `CreaOrdineController` riceve e gestisce eventi di sistema |
| **Creator** | `CreaOrdineController` crea `Food` tramite Factory Method |
| **Information Expert** | `Ordine` calcola subtotale/sconto/totale (possiede i dati) |
| **Low Coupling** | `CreaOrdineFacade` isola View dalla logica |
| **High Cohesion** | Ogni classe ha responsabilità specifiche |
| **Protected Variations** | Facade protegge dalle variazioni interne |
| **Polymorphism** | `Voucher.calcolaSconto()` comportamento polimorfico |

---

## 📐 Principi SOLID Applicati

| Principio | Applicazione |
|-----------|--------------|
| **SRP** (Single Responsibility) | Controller orchestra, Ordine calcola, Factory crea |
| **OCP** (Open/Closed) | Decorator permette nuovi add-on senza modifiche |
| **LSP** (Liskov Substitution) | Sottoclassi Food/Voucher sono sostituibili |
| **ISP** (Interface Segregation) | Interfaccia Voucher coesa |
| **DIP** (Dependency Inversion) | Controller dipende da astrazioni (Food, Voucher) |

---

## 🔧 Come Visualizzare i Diagrammi

### File `.drawio`
1. Aprire [app.diagrams.net](https://app.diagrams.net)
2. File → Open from → Device
3. Selezionare il file `.drawio`

### File `.puml` (PlantUML)
**Opzione 1: Online**
1. Aprire [PlantUML Web Server](http://www.plantuml.com/plantuml)
2. Incollare il contenuto del file

**Opzione 2: IntelliJ IDEA**
1. Installare plugin "PlantUML integration"
2. Aprire il file `.puml`
3. La preview viene generata automaticamente

**Opzione 3: VS Code**
1. Installare estensione "PlantUML"
2. Alt+D per preview

---

## 📊 Esempio di Utilizzo del Decorator Pattern

```java
// Panino base + Cipolla + Patatine
Food kebab = new PaninoDonerKebab();          // 5.50€, 5 min
kebab = new Cipolla(kebab);                   // +0.50€, +1 min
kebab = new Patatine(kebab);                  // +2.00€, +3 min

// Risultato:
kebab.getDescrizione()  // "Panino Doner Kebab, Cipolla, Patatine"
kebab.getCosto()        // 8.00€
kebab.getDurata()       // 9 minuti

// Nel controller:
private Food applicaDecorator(Food food, String addOnClasse) {
    switch (addOnClasse) {
        case "Cipolla":    return new Cipolla(food);
        case "Patatine":   return new Patatine(food);
        case "SalsaYogurt": return new SalsaYogurt(food);
        // ...
    }
}
```

---

## 📊 Esempio di Utilizzo dello Strategy Pattern

```java
// Ordine usa voucher tramite interfaccia Voucher (Strategy)
public double getSconto() {
    return voucher.calcolaSconto(getSubtotale());
}

// VoucherPercentuale: sconto 10% su 20€ → 2€
// VoucherFisso: sconto 5€ se ordine >= 15€
// NessunVoucher: sempre 0€ (Null Object)

// L'Ordine non sa quale tipo di voucher ha!
// Polimorfismo in azione.
```

---

## 📁 Struttura delle Classi BCE

```
org.example.use_cases.crea_ordine/
├── CreaOrdineFacade.java          // Facade (Control/Entry Point)
├── CreaOrdineController.java      // Control (Orchestratore)
├── beans/
│   ├── OrdineBean.java            // DTO
│   ├── FoodBean.java              // DTO
│   ├── VoucherBean.java           // DTO
│   └── RiepilogoOrdineBean.java   // DTO
└── graphic_controllers/
    ├── CreaOrdineGUIController.java  // Boundary (GUI)
    └── CreaOrdineCLIController.java  // Boundary (CLI)

org.example.model/
├── ordine/
│   ├── Ordine.java                // Entity
│   └── OrdineLazyFactory.java     // Factory
├── food/
│   ├── Food.java                  // Entity (Abstract)
│   ├── PaninoDonerKebab.java      // Concrete Product
│   ├── PiadinaDonerKebab.java     // Concrete Product
│   ├── KebabAlPiatto.java         // Concrete Product
│   └── decorator/
│       ├── DecoratorAddON.java    // Decorator (Abstract)
│       ├── Cipolla.java           // Concrete Decorator
│       ├── SalsaYogurt.java       // Concrete Decorator
│       ├── Patatine.java          // Concrete Decorator
│       └── MixVerdureGrigliate.java // Concrete Decorator
└── voucher/
    ├── Voucher.java               // Strategy Interface
    ├── VoucherPercentuale.java    // Concrete Strategy
    ├── VoucherFisso.java          // Concrete Strategy
    └── NessunVoucher.java         // Null Object
```

---

## 📝 Note Tecniche

- I diagrammi seguono rigorosamente gli standard **UML 2.x** (OMG)
- Le relazioni utilizzano la notazione corretta:
  - `──▷` Generalizzazione/Ereditarietà
  - `····▷` Implementazione interfaccia
  - `──◆` Composizione
  - `──◇` Aggregazione
  - `──>` Associazione/Dipendenza
- Gli stereotipi `<<Boundary>>`, `<<Control>>`, `<<Entity>>` seguono la convenzione BCE
- I pattern GoF sono annotati con stereotipi specifici (`<<Facade>>`, `<<Decorator>>`, etc.)
