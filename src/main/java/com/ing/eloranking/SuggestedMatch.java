package com.ing.eloranking;

import com.ing.eloranking.presentation.PlayerProfile;

public class SuggestedMatch {

    private PlayerProfile winnerPlayer; 
    
    private PlayerProfile loserPlayer;

    public SuggestedMatch(PlayerProfile player1, PlayerProfile player2) {
        if(player1.getRank() > player2.getRank()){
            winnerPlayer = player1;
            loserPlayer = player2;
        }else{            
            this.winnerPlayer = player2;
            this.loserPlayer = player1;
        }   
    }

    public PlayerProfile getWinnerPlayer() {
        return winnerPlayer;
    }

    public void setWinnerPlayer(PlayerProfile winnerPlayer) {
        this.winnerPlayer = winnerPlayer;
    }

    public PlayerProfile getLoserPlayer() {
        return loserPlayer;
    }

    public void setLoserPlayer(PlayerProfile loserPlayer) {
        this.loserPlayer = loserPlayer;
    }
    
    
}
