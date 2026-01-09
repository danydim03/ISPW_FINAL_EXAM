package org.example.use_cases.usa_voucher;

import org.example.exceptions.*;
import org.example.model.ordine.Ordine;
import org.example.model.voucher.*;
import org.example.use_cases.crea_ordine.beans.VoucherBean;

/**
 * Controller Applicativo per lo Use Case "Usa Voucher".
 * 
 * Responsabilità:
 * - Gestire la logica di applicazione/rimozione voucher
 * - Validare i voucher
 * - Calcolare gli sconti
 * - Convertire Voucher entity in VoucherBean
 * 
 * Segue il pattern BCE e i principi GRASP (Single Responsibility, Information
 * Expert).
 */
public class UsaVoucherController {

    public UsaVoucherController() {
        // Il controller viene istanziato dal CreaOrdineController o direttamente dal
        // Facade
    }

    /**
     * Recupera un voucher dal sistema tramite il suo codice.
     * 
     * @param codiceVoucher il codice del voucher da cercare
     * @return il Voucher trovato
     * @throws ObjectNotFoundException se il voucher non esiste
     */
    public Voucher getVoucherByCodice(String codiceVoucher) throws DAOException, ObjectNotFoundException,
            MissingAuthorizationException, WrongListQueryIdentifierValue, UserNotFoundException,
            UnrecognizedRoleException {

        if (codiceVoucher == null || codiceVoucher.trim().isEmpty()) {
            throw new ObjectNotFoundException("Codice voucher non valido");
        }

        return VoucherLazyFactory.getInstance().getVoucherByCodice(codiceVoucher.trim().toUpperCase());
    }

    /**
     * Valida un voucher verificandone la validità.
     * 
     * @param voucher il voucher da validare
     * @return true se il voucher è valido, false altrimenti
     */
    public boolean isVoucherValido(Voucher voucher) {
        return voucher != null && voucher.isValido();
    }

    /**
     * Applica un voucher all'ordine specificato.
     * 
     * @param ordine        l'ordine a cui applicare il voucher
     * @param codiceVoucher il codice del voucher da applicare
     * @return VoucherBean con i dati del voucher applicato, null se non valido
     */
    public VoucherBean applicaVoucherAOrdine(Ordine ordine, String codiceVoucher) throws DAOException,
            ObjectNotFoundException, MissingAuthorizationException, WrongListQueryIdentifierValue,
            UserNotFoundException, UnrecognizedRoleException {

        if (ordine == null || codiceVoucher == null || codiceVoucher.trim().isEmpty()) {
            return null;
        }

        try {
            // Cerca il voucher nel sistema
            Voucher voucher = getVoucherByCodice(codiceVoucher);

            // Verifica validità
            if (!isVoucherValido(voucher)) {
                return null;
            }

            // Applica il voucher all'ordine
            ordine.applicaVoucher(voucher);

            // Converti in Bean e restituisci
            return convertVoucherToBean(voucher);

        } catch (ObjectNotFoundException e) {
            // Voucher non trovato
            return null;
        }
    }

    /**
     * Rimuove il voucher dall'ordine specificato.
     * 
     * @param ordine l'ordine da cui rimuovere il voucher
     */
    public void rimuoviVoucherDaOrdine(Ordine ordine) {
        if (ordine != null) {
            ordine.rimuoviVoucher();
        }
    }

    /**
     * Calcola lo sconto applicato dal voucher sull'ordine.
     * 
     * @param ordine l'ordine su cui calcolare lo sconto
     * @return l'importo dello sconto
     */
    public double calcolaSconto(Ordine ordine) {
        if (ordine == null) {
            return 0.0;
        }
        return ordine.getSconto();
    }

    /**
     * Verifica se l'ordine ha un voucher applicato.
     * 
     * @param ordine l'ordine da verificare
     * @return true se l'ordine ha un voucher applicato
     */
    public boolean hasVoucherApplicato(Ordine ordine) {
        return ordine != null && ordine.hasVoucher();
    }

    /**
     * Ottiene il voucher attualmente applicato all'ordine.
     * 
     * @param ordine l'ordine da cui ottenere il voucher
     * @return VoucherBean del voucher applicato, null se non presente
     */
    public VoucherBean getVoucherApplicato(Ordine ordine) {
        if (ordine == null || !ordine.hasVoucher()) {
            return null;
        }
        return convertVoucherToBean(ordine.getVoucher());
    }

    /**
     * Converte un Voucher entity in VoucherBean.
     * 
     * @param voucher il voucher da convertire
     * @return VoucherBean con i dati del voucher
     */
    public VoucherBean convertVoucherToBean(Voucher voucher) {
        if (voucher == null) {
            return null;
        }

        VoucherBean bean = new VoucherBean();
        bean.setId(voucher.getId());
        bean.setCodice(voucher.getCodice());
        bean.setDescrizione(voucher.getDescrizione());
        bean.setTipoVoucher(voucher.getTipoVoucher());
        bean.setDataScadenza(voucher.getDataScadenza());
        bean.setValido(voucher.isValido());

        if (voucher instanceof VoucherPercentuale voucherPercentuale) {
            bean.setValore(voucherPercentuale.getPercentuale());
        } else if (voucher instanceof VoucherFisso voucherFisso) {
            bean.setValore(voucherFisso.getImportoSconto());
            bean.setMinimoOrdine(voucherFisso.getMinimoOrdine());
        }

        return bean;
    }
}
