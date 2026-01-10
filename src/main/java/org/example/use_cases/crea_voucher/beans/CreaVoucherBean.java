package org.example.use_cases.crea_voucher.beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
import java.util.regex.Pattern;

import org.example.exceptions.ValidationException;

/**
 * Bean per il trasporto dei dati per la creazione di un Voucher tra Boundary e
 * Control.
 * Segue il pattern BCE.
 * 
 * Include validazione sintattica nei setter (Fail Fast principle).
 * La validazione semantica (es. unicità codice) è delegata al Controller.
 */
public class CreaVoucherBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Set<String> TIPI_VALIDI = Set.of("PERCENTUALE", "FISSO");
    private static final Pattern CODICE_PATTERN = Pattern.compile("^[A-Z0-9]+$");
    private static final double MAX_PERCENTUALE = 100.0;
    private static final double MIN_VALORE = 0.0;

    private String codice;
    private String tipoVoucher; // "PERCENTUALE" o "FISSO"
    private double valore; // percentuale (0-100) o importo fisso
    private double minimoOrdine; // per voucher fissi, minimo ordine richiesto
    private LocalDate dataScadenza; // opzionale

    public CreaVoucherBean() {
        this.minimoOrdine = 0.0;
    }

    public CreaVoucherBean(String codice, String tipoVoucher, double valore) {
        setCodice(codice);
        setTipoVoucher(tipoVoucher);
        setValore(valore);
        this.minimoOrdine = 0.0;
    }

    public CreaVoucherBean(String codice, String tipoVoucher, double valore,
            double minimoOrdine, LocalDate dataScadenza) {
        setCodice(codice);
        setTipoVoucher(tipoVoucher);
        setValore(valore);
        setMinimoOrdine(minimoOrdine);
        setDataScadenza(dataScadenza);
    }

    // Getters e Setters con validazione sintattica

    public String getCodice() {
        return codice;
    }

    /**
     * Imposta il codice del voucher.
     * 
     * @param codice il codice del voucher (deve essere alfanumerico maiuscolo, non
     *               vuoto)
     * @throws ValidationException se il codice non è valido
     */
    public void setCodice(String codice) {
        if (codice == null || codice.trim().isEmpty()) {
            throw new ValidationException("Il codice del voucher non può essere vuoto");
        }

        String codiceTrimmed = codice.trim().toUpperCase();

        if (!CODICE_PATTERN.matcher(codiceTrimmed).matches()) {
            throw new ValidationException(
                    "Il codice deve contenere solo lettere maiuscole e numeri (es. SCONTO10, WELCOME2024)");
        }

        this.codice = codiceTrimmed;
    }

    public String getTipoVoucher() {
        return tipoVoucher;
    }

    /**
     * Imposta il tipo di voucher.
     * 
     * @param tipoVoucher il tipo (deve essere "PERCENTUALE" o "FISSO")
     * @throws ValidationException se il tipo non è valido
     */
    public void setTipoVoucher(String tipoVoucher) {
        if (tipoVoucher == null || tipoVoucher.trim().isEmpty()) {
            throw new ValidationException("Il tipo di voucher deve essere specificato");
        }

        String tipoUpper = tipoVoucher.trim().toUpperCase();

        if (!TIPI_VALIDI.contains(tipoUpper)) {
            throw new ValidationException(
                    "Tipo voucher non valido: " + tipoVoucher + ". Valori ammessi: PERCENTUALE, FISSO");
        }

        this.tipoVoucher = tipoUpper;
    }

    public double getValore() {
        return valore;
    }

    /**
     * Imposta il valore dello sconto.
     * 
     * @param valore il valore (deve essere > 0, per PERCENTUALE deve essere <= 100)
     * @throws ValidationException se il valore non è valido
     */
    public void setValore(double valore) {
        if (valore <= MIN_VALORE) {
            throw new ValidationException("Il valore dello sconto deve essere maggiore di 0");
        }

        // Se è un voucher percentuale, valida che non superi 100%
        if ("PERCENTUALE".equals(this.tipoVoucher) && valore > MAX_PERCENTUALE) {
            throw new ValidationException(
                    "La percentuale di sconto non può superare il 100%");
        }

        this.valore = valore;
    }

    public double getMinimoOrdine() {
        return minimoOrdine;
    }

    /**
     * Imposta il minimo ordine richiesto.
     * 
     * @param minimoOrdine il minimo ordine (deve essere >= 0)
     * @throws ValidationException se il minimo è negativo
     */
    public void setMinimoOrdine(double minimoOrdine) {
        if (minimoOrdine < 0) {
            throw new ValidationException("Il minimo ordine non può essere negativo");
        }
        this.minimoOrdine = minimoOrdine;
    }

    public LocalDate getDataScadenza() {
        return dataScadenza;
    }

    /**
     * Imposta la data di scadenza del voucher.
     * 
     * @param dataScadenza la data di scadenza (opzionale, ma se presente deve
     *                     essere futura)
     * @throws ValidationException se la data è nel passato
     */
    public void setDataScadenza(LocalDate dataScadenza) {
        if (dataScadenza != null && dataScadenza.isBefore(LocalDate.now())) {
            throw new ValidationException(
                    "La data di scadenza non può essere nel passato");
        }
        this.dataScadenza = dataScadenza;
    }

    /**
     * Valida la coerenza complessiva del bean.
     * Questo metodo può essere chiamato prima di usare il bean.
     * 
     * @throws ValidationException se il bean non è valido
     */
    public void validate() {
        if (codice == null || codice.isEmpty()) {
            throw new ValidationException("Il codice del voucher è obbligatorio");
        }
        if (tipoVoucher == null || tipoVoucher.isEmpty()) {
            throw new ValidationException("Il tipo di voucher è obbligatorio");
        }
        if (valore <= MIN_VALORE) {
            throw new ValidationException("Il valore dello sconto è obbligatorio");
        }
    }

    @Override
    public String toString() {
        return String.format(
                "CreaVoucherBean{codice='%s', tipo='%s', valore=%.2f, minimoOrdine=%.2f, scadenza=%s}",
                codice, tipoVoucher, valore, minimoOrdine, dataScadenza);
    }
}
