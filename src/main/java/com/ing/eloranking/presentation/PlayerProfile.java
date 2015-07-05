package com.ing.eloranking.presentation;

import java.util.LinkedList;

public class PlayerProfile implements Comparable<PlayerProfile>{

    private final String id;
    private final String name;
    private Double rank;
    
    private final LinkedList<PlayerProfile> wins = new LinkedList<PlayerProfile>();
    private final LinkedList<PlayerProfile> losses = new LinkedList<PlayerProfile>();

    public PlayerProfile(String id, String name,Double rank) {
        this.id = id;
        this.name = name;
        this.rank = rank;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LinkedList<PlayerProfile> getWins() {
        return wins;
    }

    public LinkedList<PlayerProfile> getLosses() {
        return losses;
    }

    public Double getRank() {
        return rank;
    }

    public void setRank(Double rank) {
        this.rank = rank;
    }

    public int compareTo(PlayerProfile opponentProfile) {
        return this.rank.compareTo(opponentProfile.rank);
    }

}
