package org.example.model.voucher;

public interface Voucher {
    
    // Calcola lo sconto da applicare
    double calcolaSconto(double totaleOrdine);
    
    // Restituisce il codice del voucher
    String getCodice();
    
    // Descrizione dello sconto
    String getDescrizione();
    
    // Verifica se il voucher è valido (non scaduto, etc.)
    boolean isValido();
}