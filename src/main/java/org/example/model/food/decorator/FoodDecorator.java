package org.example.model.food.decorator;

import org.example.model.food.Food;
import org.example.model.food.NullFood;

/**
 * Decorator astratto per aggiungere funzionalità ai Food.
 * 
 * Pattern GoF: Decorator
 * - Mantiene riferimento al componente decorato
 * - Delega di default tutte le operazioni al componente wrappato
 * - I ConcreteDecorator (Cipolla, Patatine, etc.) estendono questa classe
 * e aggiungono comportamento chiamando super.metodo() + proprio contributo
 * 
 * GRASP Compliance:
 * - Polymorphism: tutti i Food (decorati e non) hanno la stessa interfaccia
 * - Low Coupling: il decorator dipende solo dall'interfaccia Food
 * 
 * NOTA: Utilizza Null Object Pattern (NullFood) per gestire casi info-only
 * (caricati dal DAO), evitando NullPointerException e check null sparsi.
 */
public abstract class FoodDecorator extends Food {

    protected Food foodDecorato;

    /**
     * Costruttore che accetta un componente da decorare.
     * Se food è null (es. info-only dal DAO), utilizza un NullFood.
     * 
     * @param food il componente da decorare (o null per info-only)
     */
    protected FoodDecorator(Food food) {
        if (food == null) {
            this.foodDecorato = new NullFood(); // Null Object Pattern
        } else {
            this.foodDecorato = food;
        }
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
