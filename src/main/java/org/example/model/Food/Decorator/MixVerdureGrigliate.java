package org.example.model.Food.Decorator;
import org.example.model.Food.Food;


public class MixVerdureGrigliate extends DecoratorAddON {
    
    public MixVerdureGrigliate(Food food) {
        super(food);
    }
    
    @Override
    public String getDescrizione() {
        return foodDecorato.getDescrizione() + ", Mix Verdure Grigliate";
    }
    
    @Override
    public double getCostoPlus() {
        return 1.50;
    }
    
    @Override
    public int getDurataPlus() {
        return 2;
    }
}