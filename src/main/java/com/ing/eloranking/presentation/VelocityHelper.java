package com.ing.eloranking.presentation;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.ing.eloranking.MatchRatings;
import com.ing.eloranking.SuggestedMatch;

public class VelocityHelper {

    public static String generateCommonReport(List<Entry<String,PlayerProfile>> sortedPlayers){

        VelocityEngine ve = new VelocityEngine();
        ve.init();
        VelocityContext context = new VelocityContext();
        context.put("players", sortedPlayers);
        /*  get the Template  */
        Template t = ve.getTemplate("commonPlayerProfileReport.vm" );
        /*  now render the template into a Writer  */
        StringWriter writer = new StringWriter();
        t.merge( context, writer );
        return writer.toString();
    }

    public static String generatePlayerReport(List<Entry<String,PlayerProfile>> sortedPlayers){
        VelocityEngine ve = new VelocityEngine();
        ve.init();
        VelocityContext context = new VelocityContext();
        context.put("players", sortedPlayers);
        /*  get the Template  */
        Template t = ve.getTemplate("playerProfileReport.vm" );
        /*  now render the template into a Writer  */
        StringWriter writer = new StringWriter();
        t.merge( context, writer );
        return writer.toString();
    }

    public static String generateSuggestedMatchesReport(List<PlayerProfile> players){
        VelocityEngine ve = new VelocityEngine();
        ve.init();
        VelocityContext context = new VelocityContext();
        List<SuggestedMatch> suggestedMatches = new ArrayList<SuggestedMatch>();
        for (Iterator iterator = players.iterator(); iterator.hasNext();) {
            PlayerProfile playerProfile = (PlayerProfile) iterator.next();
            if(iterator.hasNext()){                
                PlayerProfile opponentProfile = (PlayerProfile) iterator.next();
                SuggestedMatch suggestedMatch = new SuggestedMatch(playerProfile, opponentProfile);
                suggestedMatches.add(suggestedMatch);
            }else{
                break;
            }
        }
        
        context.put("suggestedMatches", suggestedMatches);
        /*  get the Template  */
        Template t = ve.getTemplate("suggestedMatches.vm" );
        /*  now render the template into a Writer  */
        StringWriter writer = new StringWriter();
        t.merge( context, writer );
        return writer.toString();
    }

    
}
