package org.example.model.food.Decorator;
import org.example.model.food.Food;


public class SalsaYogurt extends DecoratorAddON {
    
    public SalsaYogurt(Food food) {
        super(food);
    }
    
    @Override
    public String getDescrizione() {
        return foodDecorato.getDescrizione() + ", Salsa Yogurt";
    }
    
    @Override
    public double getCostoPlus() {
        return 0.80;
    }
    
    @Override
    public int getDurataPlus() {
        return 0;
    }
}