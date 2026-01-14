# VOPC - Use Case "Crea Ordine" (con FoodFactory)

## Diagramma Mermaid

```mermaid
classDiagram
    direction TB
    
    %% ==================== ACTOR ====================
    class Cliente {
        <<Actor>>
    }
    
    %% ==================== BOUNDARY (VIEW) ====================
    class CreaOrdineGUIController {
        <<Boundary>>
        -facade: CreaOrdineFacade
        -prodottiBaseDisponibili: List~FoodBean~
        -addOnDisponibili: List~FoodBean~
        +initialize()
        +onAggiungiProdotto()
        +onApplicaVoucher()
        +onConfermaOrdine()
        +aggiornaRiepilogo()
    }
    
    class CreaOrdineCLIController {
        <<Boundary>>
        -facade: CreaOrdineFacade
        -prodottiBase: List~FoodBean~
        -addOns: List~FoodBean~
        +start()
        +handleAggiungiProdotto()
        +handleConfermaOrdine()
    }
    
    %% ==================== CONTROL ====================
    class CreaOrdineFacade {
        <<Facade>>
        -controller: CreaOrdineController
        +inizializzaNuovoOrdine(): OrdineBean
        +aggiungiProdottoAOrdine(FoodBean): boolean
        +applicaVoucher(String): VoucherBean
        +getRiepilogoOrdine(): RiepilogoOrdineBean
        +confermaOrdine(): boolean
    }
    
    class CreaOrdineController {
        <<Control>>
        -ordineCorrente: Ordine
        -voucherController: UsaVoucherController
        +inizializzaNuovoOrdine(String): OrdineBean
        +aggiungiProdottoAOrdine(FoodBean): boolean
        +getRiepilogoOrdine(): RiepilogoOrdineBean
        +confermaOrdine(): boolean
    }
    
    class UsaVoucherController {
        <<Control>>
        +applicaVoucherAOrdine(Ordine, String): VoucherBean
        +rimuoviVoucherDaOrdine(Ordine)
    }
    
    %% ==================== FACTORY ====================
    class FoodFactory {
        <<Factory>>
        +creaProdottoBase(String)$ Food
        +applicaDecorator(Food, String)$ Food
    }
    
    %% ==================== BEANS (DTO) ====================
    class OrdineBean {
        <<Bean>>
        -numeroOrdine: Long
        -clienteId: String
        -totale: double
        -stato: String
    }
    
    class FoodBean {
        <<Bean>>
        -descrizione: String
        -costo: double
        -classe: String
        -addOnSelezionati: List~String~
    }
    
    class VoucherBean {
        <<Bean>>
        -codice: String
        -tipoVoucher: String
        -valore: double
    }
    
    class RiepilogoOrdineBean {
        <<Bean>>
        -righeOrdine: List~RigaOrdineBean~
        -subtotale: double
        -sconto: double
        -totale: double
        +getTotaleFormattato(): String
    }
    
    %% ==================== ENTITY ====================
    class Ordine {
        <<Entity>>
        -numeroOrdine: Long
        -prodotti: List~Food~
        -voucher: Voucher
        +aggiungiProdotto(Food)
        +getTotale(): double
    }
    
    class Food {
        <<Entity Abstract>>
        #tipo: String
        +getDescrizione(): String
        +getCosto(): double
    }
    
    class PaninoDonerKebab {
        <<Entity>>
    }
    class PiadinaDonerKebab {
        <<Entity>>
    }
    class KebabAlPiatto {
        <<Entity>>
    }
    
    class DecoratorAddON {
        <<Decorator Abstract>>
        #foodDecorato: Food
    }
    
    class Cipolla {
        <<Decorator>>
    }
    class Patatine {
        <<Decorator>>
    }
    class SalsaYogurt {
        <<Decorator>>
    }
    
    class Voucher {
        <<Interface Strategy>>
        +calcolaSconto(double): double
    }
    
    class VoucherPercentuale {
        <<Entity>>
    }
    class VoucherFisso {
        <<Entity>>
    }
    class NessunVoucher {
        <<Null Object>>
    }
    
    %% ==================== RELAZIONI ====================
    
    %% Actor -> Boundary
    Cliente --> CreaOrdineGUIController
    Cliente --> CreaOrdineCLIController
    
    %% Boundary -> Control (usa Beans)
    CreaOrdineGUIController --> CreaOrdineFacade
    CreaOrdineCLIController --> CreaOrdineFacade
    CreaOrdineFacade --> CreaOrdineController
    CreaOrdineController --> UsaVoucherController
    
    %% Control -> Factory (NUOVA RELAZIONE)
    CreaOrdineController --> FoodFactory : usa
    
    %% Factory -> Entity (crea)
    FoodFactory ..> Food : crea
    FoodFactory ..> DecoratorAddON : crea
    
    %% Control -> Entity
    CreaOrdineController --> Ordine
    UsaVoucherController --> Voucher
    
    %% Composizione Ordine
    Ordine "1" *-- "*" Food : contiene
    Ordine "1" o-- "0..1" Voucher : applica
    
    %% Ereditarietà Food
    Food <|-- PaninoDonerKebab
    Food <|-- PiadinaDonerKebab
    Food <|-- KebabAlPiatto
    Food <|-- DecoratorAddON
    
    %% Decorator Aggregation
    DecoratorAddON o--> Food : decora
    DecoratorAddON <|-- Cipolla
    DecoratorAddON <|-- Patatine
    DecoratorAddON <|-- SalsaYogurt
    
    %% Ereditarietà Voucher
    Voucher <|.. VoucherPercentuale
    Voucher <|.. VoucherFisso
    Voucher <|.. NessunVoucher
    
    %% Dipendenze Beans (tratteggiate)
    CreaOrdineController ..> OrdineBean : ritorna
    CreaOrdineController ..> RiepilogoOrdineBean : ritorna
    CreaOrdineController ..> FoodBean : riceve
    UsaVoucherController ..> VoucherBean : ritorna
    
    %% Boundary tiene Beans
    CreaOrdineGUIController --> FoodBean : mantiene
    CreaOrdineCLIController --> FoodBean : mantiene
```

---

## Legenda

| Stereotipo | Colore consigliato | Descrizione |
|------------|-------------------|-------------|
| `<<Actor>>` | Giallo | Utente del sistema |
| `<<Boundary>>` | Blu | View/Controller Grafico |
| `<<Facade>>` | Viola | Punto di ingresso per la View |
| `<<Control>>` | Verde | Controller Applicativo |
| `<<Factory>>` | Arancione chiaro | **NUOVO** - Crea Entity |
| `<<Bean>>` | Rosa | DTO per comunicazione |
| `<<Entity>>` | Arancione | Oggetti di dominio |
| `<<Decorator>>` | Arancione | Pattern Decorator |
| `<<Strategy>>` | Arancione | Pattern Strategy |

---

## Miglioramento GRASP ottenuto

| Principio | Prima | Dopo |
|-----------|-------|------|
| **Low Coupling** | Controller dipendeva da 7 classi Food | Controller dipende solo da FoodFactory |
| **Creator** | Controller creava Food | Factory crea Food |
| **Information Expert** | Controller "sapeva" come costruire | Factory "sa" come costruire |

Copia il codice Mermaid su [mermaid.live](https://mermaid.live) per visualizzarlo!
