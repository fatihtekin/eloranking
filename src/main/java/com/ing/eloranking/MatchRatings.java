package com.ing.eloranking;

public class MatchRatings {

    private final double currentPlayerRank;
    private final double opponentRank;    
    
    public MatchRatings(double currentPlayerRank, double opponentRank) {
        this.currentPlayerRank = Math.round(currentPlayerRank * 100.0 ) / 100.0;
        this.opponentRank = Math.round(opponentRank * 100.0 ) / 100.0;
    }

    public double getCurrentPlayerRank() {
        return currentPlayerRank;
    }

    public double getOpponentRank() {
        return opponentRank;
    }
    
}
