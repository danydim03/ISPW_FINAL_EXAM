package org.example.model.food.decorator;

import org.example.model.food.Food;
import java.util.Objects;

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
 */
public abstract class FoodDecorator extends Food {

    protected Food foodDecorato;

    /**
     * Costruttore che richiede obbligatoriamente un componente da decorare.
     * 
     * @param food il componente da decorare (non può essere null)
     * @throws NullPointerException se food è null
     */
    protected FoodDecorator(Food food) {
        Objects.requireNonNull(food, "Il componente Food non può essere null");
        this.foodDecorato = food;
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
