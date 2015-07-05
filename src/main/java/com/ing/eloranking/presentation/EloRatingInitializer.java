package com.ing.eloranking.presentation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ing.eloranking.EloRatingCalculator;
import com.ing.eloranking.MatchRatings;
import com.ing.eloranking.ScoreType;

public class EloRatingInitializer {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public HashMap<String, PlayerProfile> readFiles(String idPlayerFileLocation, String scoresFileLocation,HashMap<String,String> nameIdMap){
        nameIdMap.clear();
        HashMap<String, PlayerProfile> playerProfilesMap = readPlayerIdFile(idPlayerFileLocation,nameIdMap);
        enrichPlayerProfilesPerMatch(playerProfilesMap, scoresFileLocation);
        return playerProfilesMap;
    }

    public HashMap<String, PlayerProfile> readPlayerIdFile(String idPlayerFileLocation,HashMap<String,String> nameIdMap){

        HashMap<String, PlayerProfile> idPlayerMap = new HashMap<String, PlayerProfile>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(idPlayerFileLocation));
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] values = line.split("\\s+");
                if(values.length>1){
                    PlayerProfile player = new PlayerProfile(values[0], values[1],1000.0);
                    idPlayerMap.put(player.getId(),player);
                    nameIdMap.put(player.getName(),player.getId());
                }else{
                    logger.error("Found line with missing information for {} file. Line:{}",idPlayerFileLocation,line);
                }
            }            
            return idPlayerMap;
        }catch(Exception e){
            throw new RuntimeException("Could read file from location: "+idPlayerFileLocation);
        }finally {
            if(br!=null){
                try {
                    br.close();                    
                } catch (Exception e) {
                    logger.debug("Could not close the file in location:"+idPlayerFileLocation);                    
                }
            }
        }
    }

    private void enrichPlayerProfilesPerMatch(HashMap<String, PlayerProfile> playerProfilesMap,String scoresFileLocation){

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(scoresFileLocation));
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] values = line.split("\\s+");
                if(values.length>1){
                    PlayerProfile winner = playerProfilesMap.get(values[0]);
                    PlayerProfile loser = playerProfilesMap.get(values[1]);
                    if(winner==null || loser==null){
                        continue;
                    }
                    winner.getWins().add(loser);
                    loser.getLosses().add(winner);      
                    MatchRatings ratings = EloRatingCalculator.getNewRating(winner.getRank(), loser.getRank(), ScoreType.WIN);
                    winner.setRank(ratings.getCurrentPlayerRank());
                    loser.setRank(ratings.getOpponentRank());                    
                }else{
                    logger.error("Found line with missing information for {} file. Line:{}",scoresFileLocation,line);
                }
            }            
        }catch(Exception e){
            throw new RuntimeException("Could read file from location: "+scoresFileLocation);
        }finally {
            if(br!=null){
                try {
                    br.close();                    
                } catch (Exception e) {
                    logger.debug("Could not close the file in location:"+scoresFileLocation);                    
                }
            }
        }

    }

    public static <K,V extends Comparable<? super PlayerProfile>> List<Entry<K, PlayerProfile>> entriesSortedByValues(Map<K,PlayerProfile> map) {

        List<Entry<K,PlayerProfile>> sortedEntries = new ArrayList<Entry<K,PlayerProfile>>(map.entrySet());
        
        Collections.sort(sortedEntries, 
                new Comparator<Entry<K,PlayerProfile>>() {
            public int compare(Entry<K, PlayerProfile> o1, Entry<K, PlayerProfile> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        return sortedEntries;
    }


}