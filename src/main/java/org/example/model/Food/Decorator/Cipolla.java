package org.example.model.Food.Decorator;
import org.example.model.Food.Food;


public class Cipolla extends DecoratorAddON {

    public Cipolla(Food food) {
        super(food);
    }

    @Override
    public String getDescrizione() {
        return foodDecorato.getDescrizione() + ", Cipolla";
    }

    @Override
    public double getCostoPlus() {
        return 0.50;
    }

    @Override
    public int getDurataPlus() {
        return 1;
    }
}