package com.ing.eloranking;

public enum ScoreType {

    WIN(1.0), 
    DRAW(0.5),
    LOSS(0.0);
    
    private double value;
    
    private ScoreType(Double value) {
        this.value = value;
    }
    
    public double getValue() {
        return value;
    }
    
}
