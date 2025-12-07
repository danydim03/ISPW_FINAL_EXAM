package org.example.model.voucher;

public class VoucherPercentuale implements Voucher {
    
    private String codice;
    private double percentuale; // es. 10. 0 per 10%
    private boolean valido;
    
    public VoucherPercentuale(String codice, double percentuale) {
        this.codice = codice;
        this.percentuale = percentuale;
        this. valido = true;
    }
    
    @Override
    public double calcolaSconto(double totaleOrdine) {
        if (!valido) {
            return 0;
        }
        return totaleOrdine * (percentuale / 100.0);
    }
    
    @Override
    public String getCodice() {
        return codice;
    }
    
    @Override
    public String getDescrizione() {
        return "Sconto del " + percentuale + "%";
    }
    
    @Override
    public boolean isValido() {
        return valido;
    }
    
    public void setValido(boolean valido) {
        this.valido = valido;
    }
}