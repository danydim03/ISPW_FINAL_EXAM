package org.example.model.voucher;

import org.example.exceptions.*;

import java.util.List;

public interface VoucherDAOInterface {
    
    /**
     * Inserisce un Voucher nel DB
     * @param voucher l'oggetto da inserire
     * @throws DAOException errori durante l'accesso al persistence layer
     * @throws PropertyException errori nel caricamento delle properties
     * @throws ResourceNotFoundException risorsa properties non trovata
     */
    void insert(Voucher voucher) throws DAOException, PropertyException, ResourceNotFoundException, MissingAuthorizationException;
    
    /**
     * Elimina un Voucher dal DB
     * @param voucher l'oggetto da eliminare
     * @throws DAOException errori durante l'accesso al persistence layer
     * @throws PropertyException errori nel caricamento delle properties
     * @throws ResourceNotFoundException risorsa properties non trovata
     */
    void delete(Voucher voucher) throws DAOException, PropertyException, ResourceNotFoundException;
    
    /**
     * Aggiorna un Voucher nel DB
     * @param voucher l'oggetto da aggiornare
     * @throws DAOException errori durante l'accesso al persistence layer
     * @throws PropertyException errori nel caricamento delle properties
     * @throws ResourceNotFoundException risorsa properties non trovata
     */
    void update(Voucher voucher) throws DAOException, PropertyException, ResourceNotFoundException, MissingAuthorizationException;
    
    /**
     * Recupera un Voucher per ID
     * @param id l'ID del voucher
     * @return l'oggetto Voucher
     * @throws DAOException errori durante l'accesso al persistence layer
     * @throws ObjectNotFoundException se il voucher non viene trovato
     * @throws PropertyException errori nel caricamento delle properties
     * @throws ResourceNotFoundException risorsa properties non trovata
     */
    Voucher getVoucherById(Long id) throws DAOException, ObjectNotFoundException, PropertyException, ResourceNotFoundException, 
            UserNotFoundException, UnrecognizedRoleException, MissingAuthorizationException, WrongListQueryIdentifierValue;
    
    /**
     * Recupera un Voucher per codice
     * @param codice il codice del voucher
     * @return l'oggetto Voucher
     * @throws DAOException errori durante l'accesso al persistence layer
     * @throws ObjectNotFoundException se il voucher non viene trovato
     * @throws PropertyException errori nel caricamento delle properties
     * @throws ResourceNotFoundException risorsa properties non trovata
     */
    Voucher getVoucherByCodice(String codice) throws DAOException, ObjectNotFoundException, PropertyException, ResourceNotFoundException, 
            UserNotFoundException, UnrecognizedRoleException, MissingAuthorizationException, WrongListQueryIdentifierValue;
    
    /**
     * Recupera tutti i Voucher attivi
     * @return lista di Voucher attivi
     * @throws DAOException errori durante l'accesso al persistence layer
     * @throws PropertyException errori nel caricamento delle properties
     * @throws ResourceNotFoundException risorsa properties non trovata
     */
    List<Voucher> getAllVoucherAttivi() throws DAOException, PropertyException, ResourceNotFoundException, 
            UserNotFoundException, UnrecognizedRoleException, ObjectNotFoundException, MissingAuthorizationException, WrongListQueryIdentifierValue;
}