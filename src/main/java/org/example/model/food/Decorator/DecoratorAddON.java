package org.example.model.food.Decorator;
import org.example.model.food.Food;

public abstract class DecoratorAddON extends Food {

    protected Food foodDecorato;

    public DecoratorAddON(Food food) {
        this.foodDecorato = food;
    }

    @Override
    public abstract String getDescrizione();

    // Costo aggiuntivo dell'add-on
    public abstract double getCostoPlus();

    // Durata aggiuntiva dell'add-on
    public abstract int getDurataPlus();

    @Override
    public double getCosto() {
        return foodDecorato.getCosto() + getCostoPlus();
    }

    @Override
    public int getDurata() {
        return foodDecorato. getDurata() + getDurataPlus();
    }
}
