package org.example.use_cases.crea_ordine.beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

import org.example.exceptions.ValidationException;

/**
 * Bean per il trasporto dei dati di un Voucher tra Boundary e Control.
 * Segue il pattern BCE.
 * 
 * Include validazione sintattica nei setter (Fail Fast principle).
 */
public class VoucherBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Set<String> TIPI_VALIDI = Set.of("PERCENTUALE", "FISSO", "NESSUNO");

    private Long id;
    private String codice;
    private String descrizione;
    private String tipoVoucher; // "PERCENTUALE", "FISSO", "NESSUNO"
    private double valore; // percentuale o importo fisso
    private double minimoOrdine;
    private LocalDate dataScadenza;
    private boolean valido;

    public VoucherBean() {
    }

    public VoucherBean(String codice) {
        setCodice(codice);
    }

    public VoucherBean(Long id, String codice, String descrizione, String tipoVoucher,
            double valore, double minimoOrdine, LocalDate dataScadenza, boolean valido) {
        this.id = id;
        setCodice(codice);
        this.descrizione = descrizione;
        setTipoVoucher(tipoVoucher);
        setValore(valore);
        setMinimoOrdine(minimoOrdine);
        this.dataScadenza = dataScadenza;
        this.valido = valido;
    }

    // Getters e Setters con validazione sintattica

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodice() {
        return codice;
    }

    /**
     * Imposta il codice del voucher.
     * 
     * @param codice il codice del voucher
     */
    public void setCodice(String codice) {
        // Il codice può essere null/vuoto solo per tipo NESSUNO
        this.codice = codice;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getTipoVoucher() {
        return tipoVoucher;
    }

    /**
     * Imposta il tipo di voucher.
     * 
     * @param tipoVoucher il tipo (deve essere "PERCENTUALE", "FISSO" o "NESSUNO")
     * @throws ValidationException se il tipo non è valido
     */
    public void setTipoVoucher(String tipoVoucher) {
        if (tipoVoucher != null && !TIPI_VALIDI.contains(tipoVoucher.toUpperCase())) {
            throw new ValidationException(
                    "Tipo voucher non valido: " + tipoVoucher + ". Valori ammessi: PERCENTUALE, FISSO, NESSUNO");
        }
        this.tipoVoucher = tipoVoucher != null ? tipoVoucher.toUpperCase() : null;
    }

    public double getValore() {
        return valore;
    }

    /**
     * Imposta il valore dello sconto.
     * 
     * @param valore il valore (non può essere negativo)
     * @throws ValidationException se il valore è negativo
     */
    public void setValore(double valore) {
        if (valore < 0) {
            throw new ValidationException("Il valore dello sconto non può essere negativo: " + valore);
        }
        this.valore = valore;
    }

    public double getMinimoOrdine() {
        return minimoOrdine;
    }

    /**
     * Imposta il minimo ordine richiesto.
     * 
     * @param minimoOrdine il minimo ordine (non può essere negativo)
     * @throws ValidationException se il minimo è negativo
     */
    public void setMinimoOrdine(double minimoOrdine) {
        if (minimoOrdine < 0) {
            throw new ValidationException("Il minimo ordine non può essere negativo: " + minimoOrdine);
        }
        this.minimoOrdine = minimoOrdine;
    }

    public LocalDate getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(LocalDate dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    public boolean isValido() {
        return valido;
    }

    public void setValido(boolean valido) {
        this.valido = valido;
    }

    @Override
    public String toString() {
        return String.format("VoucherBean{codice='%s', tipo='%s', valore=%.2f, valido=%b}",
                codice, tipoVoucher, valore, valido);
    }
}