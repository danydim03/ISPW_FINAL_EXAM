package org.example.model.food.decorator;

import org.example.model.food.Food;

/**
 * Decorator astratto per aggiungere funzionalità ai Food.
 * 
 * Pattern GoF: Decorator (PURO)
 * - Mantiene riferimento al componente decorato (MAI null)
 * - Delega di default tutte le operazioni al componente wrappato
 * - I ConcreteDecorator (Cipolla, Patatine, etc.) estendono questa classe
 * e aggiungono comportamento chiamando super.metodo() + proprio contributo
 * 
 * GRASP Compliance:
 * - Polymorphism: tutti i Food (decorati e non) hanno la stessa interfaccia
 * - Low Coupling: il decorator dipende solo dall'interfaccia Food
 * - High Cohesion: responsabilità UNICA di decorare un componente
 * 
 * NOTA: Per i metadati degli add-on (GUI), usare AddOnDescriptor (Value
 * Object).
 * I Decorator vengono usati ESCLUSIVAMENTE per decorare prodotti reali.
 */
public abstract class FoodDecorator extends Food {

    protected Food foodDecorato;

    /**
     * Costruttore che accetta un componente da decorare.
     * 
     * GoF Compliance: il componente NON può essere null.
     * I Decorator devono SEMPRE wrappare un componente reale.
     * 
     * @param food il componente da decorare (obbligatorio, non null)
     * @throws IllegalArgumentException se food è null
     */
    protected FoodDecorator(Food food) {
        if (food == null) {
            throw new IllegalArgumentException(
                    "GoF Decorator: il componente decorato non può essere null. " +
                            "Per i metadati degli add-on, usare AddOnDescriptor.");
        }
        this.foodDecorato = food;
        // Tutti i decorator sono di tipo ADDON
        this.tipo = "ADDON";
    }

    /**
     * Delega di default al componente decorato.
     * I ConcreteDecorator sovrascrivono per aggiungere comportamento.
     */
    @Override
    public String getDescrizione() {
        return this.foodDecorato.getDescrizione();
    }

    /**
     * Delega di default al componente decorato.
     * I ConcreteDecorator sovrascrivono per aggiungere costo.
     */
    @Override
    public double getCosto() {
        return this.foodDecorato.getCosto();
    }

    /**
     * Delega di default al componente decorato.
     * I ConcreteDecorator sovrascrivono per aggiungere durata.
     */
    @Override
    public int getDurata() {
        return this.foodDecorato.getDurata();
    }
}
