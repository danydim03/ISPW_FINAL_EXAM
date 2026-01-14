# Report: Gestione delle Eccezioni in Habibi Shawarma

## 1. Panoramica

Il sistema implementa una gerarchia di eccezioni personalizzate basata su una classe astratta comune `HabibiException`, che estende `java.lang.Exception`.

## 2. Struttura della Gerarchia

```
java.lang.Exception
        │
        ▼
HabibiException (abstract, checked)
        │
        ├── DAOException
        ├── ObjectNotFoundException
        ├── UserNotFoundException
        ├── ResourceNotFoundException
        ├── PropertyException
        ├── CreaOrdineException
        ├── VoucherNotValidException
        ├── MissingAuthorizationException
        ├── UnrecognizedRoleException
        ├── BeanFormatException
        ├── EmailFormatException
        ├── WrongPasswordException
        └── WrongListQueryIdentifierValue

java.lang.IllegalArgumentException
        │
        ▼
ValidationException (unchecked)
```

## 3. Classe Base: HabibiException

```java
public abstract class HabibiException extends Exception {
    
    protected HabibiException(String message) {
        super(message);
    }
    
    protected HabibiException(String message, Throwable cause) {
        super(message, cause);  // Supporta exception chaining
    }
    
    protected HabibiException(Throwable cause) {
        super(cause);
    }
}
```

### Caratteristiche:
- **Abstract**: non istanziabile direttamente
- **Checked**: richiede gestione esplicita con `try-catch` o `throws`
- **Tre costruttori**: supporta tutti gli scenari di creazione eccezioni

## 4. Exception Chaining (Wrapping)

Tutte le sottoclassi supportano il wrapping delle eccezioni:

```java
// Esempio di chaining nel layer DAO
try {
    connection.executeQuery(sql);
} catch (SQLException e) {
    throw new DAOException("Errore durante la query", e);  // ← preserva causa
}
```

Il costruttore `(String message, Throwable cause)` permette di:
- Aggiungere contesto semantico all'errore
- Preservare lo stack trace originale
- Mantenere la catena di cause per il debugging

## 5. Eccezioni per Categoria

| Categoria | Eccezioni | Uso |
|-----------|-----------|-----|
| **Persistenza** | DAOException, PropertyException, WrongListQueryIdentifierValue | Errori di accesso dati |
| **Not Found** | ObjectNotFoundException, UserNotFoundException, ResourceNotFoundException | Entità non trovate |
| **Autenticazione** | MissingAuthorizationException, WrongPasswordException, UnrecognizedRoleException | Errori di accesso |
| **Formato** | BeanFormatException, EmailFormatException | Dati malformati |
| **Business** | CreaOrdineException, VoucherNotValidException | Errori di logica applicativa |

## 6. Caso Speciale: ValidationException

```java
public class ValidationException extends IllegalArgumentException {
    // Unchecked - per validazione sintattica dei Bean
}
```

**Perché è separata?**
- Estende `IllegalArgumentException` (unchecked)
- Usata per validazione sintattica nei Bean (fail-fast)
- Non richiede dichiarazione `throws`

## 7. Vantaggi della Soluzione

| Vantaggio | Descrizione |
|-----------|-------------|
| **Catch unificato** | `catch(HabibiException e)` cattura tutte le eccezioni custom |
| **Tracciabilità** | Lo stack trace preserva la causa originale |
| **Semplicità** | Gerarchia piatta, facile da comprendere |
| **Checked** | Forza la gestione esplicita degli errori |

## 8. Esempio di Utilizzo

```java
public User login(String email, String password) 
        throws UserNotFoundException, WrongPasswordException, DAOException {
    try {
        User user = userDAO.getUserByEmail(email);
        if (!user.getPassword().equals(password)) {
            throw new WrongPasswordException("Password non corretta");
        }
        return user;
    } catch (SQLException e) {
        throw new DAOException("Errore accesso database", e);  // chaining
    }
}
```

## 9. Conclusioni

La soluzione adottata privilegia la **semplicità** mantenendo il supporto completo per:
- Exception chaining
- Gestione centralizzata (`catch HabibiException`)
- Tracciabilità degli errori
- Separazione checked/unchecked
