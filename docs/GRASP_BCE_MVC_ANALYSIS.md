# Analisi dei Principi GRASP e Pattern BCE/MVC

## Panoramica del Progetto

Il progetto **Habibi Shawarma** è un'applicazione Java per la gestione di un sistema di ordinazione di kebab. Il progetto implementa un'architettura stratificata basata su:
- **Pattern BCE (Boundary-Control-Entity)**
- **Pattern MVC (Model-View-Controller)**
- **Principi GRASP (General Responsibility Assignment Software Patterns)**

---

## 📊 Analisi del Pattern BCE / MVC

### ✅ Punti di Forza

#### 1. **Separazione Chiara delle Responsabilità**

La struttura del progetto riflette bene la separazione BCE:

| Layer | Componenti | Esempio |
|-------|------------|---------|
| **Boundary** | `graphic_controllers/`, `cli/`, `beans/` | `CreaOrdineGUIController`, `LoginGraphicControl`, `FoodBean` |
| **Control** | `*Controller.java`, `*Facade.java` | `CreaOrdineController`, `UsaVoucherController`, `CreaOrdineFacade` |
| **Entity** | `model/` | `User`, `Ordine`, `Food`, `Voucher` |

#### 2. **Pattern Facade Ben Implementato**

```
CreaOrdineGUIController (Boundary)
         ↓
  CreaOrdineFacade (Facade per autorizzazione)
         ↓
  CreaOrdineController (Control applicativo)
         ↓
     Entity (Ordine, Food, Voucher)
```

Il `CreaOrdineFacade` agisce come punto di accesso unico, gestendo:
- Autenticazione tramite `SessionManager`
- Autorizzazione basata sul ruolo
- Delega al controller applicativo

#### 3. **Bean per il Trasporto Dati**

I Bean (`FoodBean`, `OrdineBean`, `VoucherBean`, `RiepilogoOrdineBean`) implementano correttamente il pattern DTO:
- Separano la rappresentazione dei dati dalla logica di business
- Includono validazione sintattica nei setter (Fail Fast)
- Sono `Serializable` per il trasporto

#### 4. **Interfacce DAO ben Definite**

```java
// Esempio: UserDAOInterface
public interface UserDAOInterface {
    User getUserByEmail(String email) throws ...;
    User getUserByCodiceFiscale(String codiceFiscale) throws ...;
    void insert(User user) throws ...;
    void delete(User user) throws ...;
    void update(User user) throws ...;
}
```

### ⚠️ Criticità

#### 1. **Accoppiamento tra Boundary e Entity**

In `CreaOrdineGUIController.java` (linee 201-220):
```java
private void iniziaNuovoOrdine() throws CreaOrdineException {
    // ...
    var sessionUser = org.example.session_manager.SessionManager.getInstance()
            .getSessionUserByTokenKey(tokenKey);  // ❌ Accesso diretto a Entity
```

**Problema**: Il controller grafico accede direttamente al `SessionManager` e all'oggetto `User`, violando la separazione BCE.

**Raccomandazione**: Delegare completamente al Facade la gestione della sessione.

#### 2. **Dati Hardcoded nel Boundary**

In `CreaOrdineGUIController.java` (linee 176-186):
```java
private void caricaDatiIniziali() {
    prodottiBaseDisponibili = List.of(
        new FoodBean(null, "Panino Doner Kebab", 5.50, 5, "BASE", "PaninoDonerKebab"),
        // ... altri dati hardcoded
    );
}
```

**Problema**: I dati dei prodotti sono hardcoded nel Boundary invece di essere caricati dal database tramite il Facade.

**Raccomandazione**: Utilizzare `facade.getProdottiBaseDisponibili()` e `facade.getAddOnDisponibili()` che sono già disponibili.

---

## 📐 Analisi dei Principi GRASP

### ✅ Principi Ben Applicati

#### 1. **Information Expert** ⭐⭐⭐⭐⭐

L'entity `Ordine` è l'esperto delle informazioni sui calcoli:

```java
// Ordine.java - L'ordine calcola i propri totali
public double getSubtotale() {
    double totale = 0;
    for (Food f : prodotti) {
        totale += f.getCosto();
    }
    return totale;
}

public double getSconto() {
    return voucher.calcolaSconto(getSubtotale());
}

public double getTotale() {
    return getSubtotale() - getSconto();
}
```

Ogni classe ha le informazioni necessarie per svolgere i propri compiti.

#### 2. **Creator** ⭐⭐⭐⭐⭐

Le Factory creano gli oggetti appropriatamente:

```java
// OrdineLazyFactory crea gli Ordini
public Ordine newOrdine(String clienteId) throws DAOException {
    ordineCorrente = OrdineLazyFactory.getInstance().newOrdine(clienteId);
}

// CreaOrdineController crea i Food usando Factory Method
private Food creaProdottoBase(String classe) {
    switch (classe) {
        case "PaninoDonerKebab": return new PaninoDonerKebab();
        // ...
    }
}
```

#### 3. **Low Coupling** ⭐⭐⭐⭐

La delega tra controller riduce l'accoppiamento:

```java
// CreaOrdineController.java - Delega a UsaVoucherController
public VoucherBean applicaVoucher(String codiceVoucher) throws ... {
    return voucherController.applicaVoucherAOrdine(ordineCorrente, codiceVoucher);
}
```

Il DAOFactoryAbstract con pattern Abstract Factory riduce l'accoppiamento con la persistenza:

```java
// Cambio di persistenza trasparente: DB, FS, DEMO
switch (persistenceType) {
    case DB -> me = new DAOFactoryDB();
    case DEMO -> me = new DAOFactoryDemo();
    case FS -> me = new DAOFactoryFS();
}
```

#### 4. **High Cohesion** ⭐⭐⭐⭐

Ogni classe ha una responsabilità ben definita:

| Classe | Responsabilità |
|--------|---------------|
| `UsaVoucherController` | Solo gestione voucher |
| `CreaOrdineController` | Solo orchestrazione creazione ordine |
| `SessionManager` | Solo gestione sessioni |
| `OrdineMapper` | Solo conversione Entity↔Bean |

#### 5. **Controller** ⭐⭐⭐⭐⭐

Pattern Controller ben implementato con tre livelli:

```
1. Graphic Controller (Boundary) → gestisce eventi UI
2. Facade → gestisce autorizzazione
3. Application Controller → gestisce logica di business
```

#### 6. **Polymorphism** ⭐⭐⭐⭐⭐

**Pattern Decorator per gli Add-on:**
```java
public abstract class DecoratorAddON extends Food {
    protected Food foodDecorato;
    
    @Override
    public double getCosto() {
        return foodDecorato.getCosto() + getCostoPlus();
    }
}

// Uso: prodotto = new Cipolla(new SalsaYogurt(new PaninoDonerKebab()));
```

**Pattern Strategy per i Voucher:**
```java
public interface Voucher {
    double calcolaSconto(double totaleOrdine);
}

// VoucherPercentuale: 10% di sconto
// VoucherFisso: €5 di sconto
// NessunVoucher: nessuno sconto (Null Object Pattern)
```

#### 7. **Indirection** ⭐⭐⭐⭐

`DAOFactoryAbstract` fornisce indirection tra business logic e persistenza:

```java
// Il codice non conosce l'implementazione concreta
UserDAOInterface userDAO = DAOFactoryAbstract.getInstance().getUserDAO();
```

#### 8. **Pure Fabrication** ⭐⭐⭐⭐⭐

Classi di supporto che non rappresentano concetti del dominio:

| Classe | Funzione |
|--------|----------|
| `SessionManager` | Gestione sessioni (non è un'entità di dominio) |
| `LazyFactoryAbstract` | Cache e gestione lazy loading |
| `PageNavigationController` | Navigazione UI |
| `PropertiesHandler` | Configurazione |

#### 9. **Protected Variations** ⭐⭐⭐⭐⭐

**Interfacce DAO proteggono dalle variazioni di persistenza:**
```java
public interface UserDAOInterface { ... }

// Implementazioni: UserDAODB, UserDAOFS, UserDAODemo
// Cambio trasparente tramite configurazione
```

**Gerarchia di eccezioni protegge dalle variazioni di errore:**
```java
public abstract class HabibiException extends Exception {
    protected HabibiException(String message) { ... }
    protected HabibiException(String message, Throwable cause) { ... }
}
// Tutte le eccezioni dell'applicazione estendono HabibiException
```

### ⚠️ Criticità GRASP

#### 1. **Violazione Single Responsibility in LoginControl**

```java
public class LoginControl {
    public LoginBean login(String email, String password) { ... }
    public void emailMatches(String email) { ... }  // ❌ Validazione non dovrebbe essere qui
}
```

**Raccomandazione**: Estrarre la validazione in una classe `EmailValidator` separata.

#### 2. **Switch Statements Multipli (Code Smell)**

In `CreaOrdineController.java`:
```java
private Food creaProdottoBase(String classe) {
    switch (classe) {
        case "PaninoDonerKebab": return new PaninoDonerKebab();
        case "PiadinaDonerKebab": return new PiadinaDonerKebab();
        // ...
    }
}

private Food applicaDecorator(Food food, String addOnClasse) {
    switch (addOnClasse) {
        case "Cipolla": return new Cipolla(food);
        // ...
    }
}
```

**Raccomandazione**: Utilizzare una `FoodFactory` con pattern Registry per eliminare gli switch.

#### 3. **Costruttore con Troppi Parametri**

```java
public User(String name, String surname, String codiceFiscale, 
            String email, String password, LocalDate registrationDate) { ... }
```

**Nota**: Già giustificato nel pom.xml (S107 escluso), ma per entity complesse il pattern Builder sarebbe più appropriato.

---

## 🏗️ Pattern Implementati

| Pattern | Implementazione | Valutazione |
|---------|----------------|-------------|
| **Decorator** | `DecoratorAddON` per gli add-on | ⭐⭐⭐⭐⭐ Eccellente |
| **Strategy** | `Voucher` interface | ⭐⭐⭐⭐⭐ Eccellente |
| **Null Object** | `NessunVoucher` | ⭐⭐⭐⭐⭐ Eccellente |
| **Factory Method** | `creaProdottoBase()` | ⭐⭐⭐⭐ Buono |
| **Abstract Factory** | `DAOFactoryAbstract` | ⭐⭐⭐⭐⭐ Eccellente |
| **Singleton** | `SessionManager`, `LazyFactory` | ⭐⭐⭐⭐ Buono |
| **Facade** | `CreaOrdineFacade`, `UsaVoucherFacade` | ⭐⭐⭐⭐⭐ Eccellente |
| **Lazy Initialization** | `*LazyFactory` | ⭐⭐⭐⭐⭐ Eccellente |

---

## 📝 Raccomandazioni di Miglioramento

### Alta Priorità

1. **Rimuovere dati hardcoded dal Boundary**
   - Usare i metodi del Facade già esistenti per caricare prodotti e add-on

2. **Eliminare accesso diretto a Entity dal Boundary**
   - In `CreaOrdineGUIController`, delegare completamente al Facade

3. **Creare `FoodFactory` con pattern Registry**
   - Eliminare switch statements per la creazione di Food e Decorator

### Media Priorità

4. **Estrarre `EmailValidator`**
   - Separare la responsabilità di validazione da `LoginControl`

5. **Aggiungere Mapper per tutti gli Entity**
   - Attualmente solo `OrdineMapper` esiste; creare `UserMapper`, `FoodMapper`, `VoucherMapper`

### Bassa Priorità

6. **Considerare Builder Pattern per Entity complessi**
   - `User` e `Ordine` potrebbero beneficiare del pattern Builder

7. **Aggiungere più test di integrazione**
   - Attualmente solo test unitari su Bean

---

## 🎯 Conclusione

### Valutazione Complessiva

| Aspetto | Valutazione | Commento |
|---------|-------------|----------|
| **Pattern BCE/MVC** | ⭐⭐⭐⭐ (4/5) | Ben strutturato con alcune violazioni minori |
| **Principi GRASP** | ⭐⭐⭐⭐ (4.5/5) | Eccellente applicazione della maggior parte dei principi |
| **Design Patterns** | ⭐⭐⭐⭐⭐ (5/5) | Uso appropriato e ben motivato dei pattern |
| **Manutenibilità** | ⭐⭐⭐⭐ (4/5) | Buona separazione, alcune aree da migliorare |
| **Estensibilità** | ⭐⭐⭐⭐⭐ (5/5) | Factory astratte permettono facile estensione |

### Punti di Eccellenza

1. ✅ Architettura stratificata ben definita
2. ✅ Pattern Decorator per gli add-on
3. ✅ Pattern Strategy per i voucher con Null Object
4. ✅ Abstract Factory per la persistenza multi-layer
5. ✅ Facade per autorizzazione e accesso controllato
6. ✅ Gerarchia di eccezioni coerente
7. ✅ Bean con validazione integrata

### Aree di Miglioramento

1. ⚠️ Dati hardcoded nel Boundary (da correggere)
2. ⚠️ Accesso diretto a Entity dal Boundary (da correggere)
3. ⚠️ Switch statements per la creazione di oggetti
4. ⚠️ Validazione email nel Controller invece che in classe dedicata

---

**Autore**: Analisi automatica del progetto  
**Data**: Gennaio 2026  
**Versione**: 1.0
