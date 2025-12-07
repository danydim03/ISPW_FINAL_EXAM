package org.example.model.voucher;

public class NessunVoucher implements Voucher {
    
    @Override
    public double calcolaSconto(double totaleOrdine) {
        return 0;
    }
    
    @Override
    public String getCodice() {
        return "";
    }
    
    @Override
    public String getDescrizione() {
        return "Nessuno sconto applicato";
    }
    
    @Override
    public boolean isValido() {
        return true;
    }
}