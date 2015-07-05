package com.ing.eloranking.driver;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.collections.keyvalue.DefaultMapEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ing.eloranking.presentation.EloRatingInitializer;
import com.ing.eloranking.presentation.PlayerProfile;
import com.ing.eloranking.presentation.VelocityHelper;

public class Main {

    private static Logger logger = LoggerFactory.getLogger(Main.class);


    public static void main(String[] args) {


        CommandLineParser parser = new DefaultParser();
        // create the Options
        Options options = createOptions();
        HashMap<String, PlayerProfile> playerProfilesMap = null;
        List<Entry<String, PlayerProfile>> sortedPlayers = null;                
        HashMap<String,String> nameIdMap = new HashMap<String, String>();
        EloRatingInitializer eloRatingInitializer = new EloRatingInitializer();
        try{            
            playerProfilesMap = eloRatingInitializer.readFiles(getPlayersDefaultFilePath(args), getMatcheScoresDefaultFilePath(args),nameIdMap);
            sortedPlayers = EloRatingInitializer.entriesSortedByValues(playerProfilesMap);
        }catch(Exception e){
            logger.error("Something is wrong with the input files",e);
        }       

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp( "commands", options );
        
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String command = scanner.nextLine();
            try {
                // parse the command line arguments
                CommandLine line = parser.parse( options,new String[]{command}); 
                
                if( line.hasOption( "E" ) ) {
                    break;
                }
                                
                else if( line.hasOption( "F" ) ) {
                    try{
                        String[] files = line.getOptionValues("F")[0].trim().split("\\s+");         
                        playerProfilesMap = eloRatingInitializer.readFiles(files[0],files[1],nameIdMap);
                        sortedPlayers = EloRatingInitializer.entriesSortedByValues(playerProfilesMap);
                    }catch(Exception e){
                        logger.error("Something is wrong with the input files",e);
                    }
                }
                
                else if( line.hasOption( "L" ) ) {                    
                    logger.info( VelocityHelper.generatePlayerReport(sortedPlayers));
                    continue;
                }
                
                else if( line.hasOption( "R" ) ) {
                    try {
                        String[] optionValues = line.getOptionValues("R");
                        PlayerProfile player = null;
                        if(optionValues != null && optionValues.length > 0 ){                        
                            String playerName = optionValues[0];
                            String id = nameIdMap.get(playerName.trim());
                            if(id != null){                                
                                player = playerProfilesMap.get(id);
                            }
                            if(player == null){
                                logger.warn("No player exists for name:{}",playerName);
                                continue;
                            }
                            List<Entry<String, PlayerProfile>> list = new ArrayList<Map.Entry<String,PlayerProfile>>();
                            Entry<String, PlayerProfile> e = new DefaultMapEntry(player.getName(),player);
                            list.add(e);
                            logger.info(VelocityHelper.generateCommonReport(list));
                        }else{
                            logger.info(VelocityHelper.generateCommonReport(sortedPlayers));                            
                        }
                    } catch (Exception e) {
                        logger.error("Error during finding player profile",e);
                    }
                }
              
                
                else if( line.hasOption( "S" ) ) {                                        
                    List<String> keys = new ArrayList<String>(playerProfilesMap.keySet());
                    Collections.shuffle(keys);
                    Set<Double> rankSet = new HashSet<Double>();
                    List<PlayerProfile> suggestions = new ArrayList<PlayerProfile>();
                    for (String id : keys) {
                        PlayerProfile player = playerProfilesMap.get(id);
                        if(!rankSet.contains(player.getRank())){
                            suggestions.add(player);
                        }
                        if(suggestions.size() > 19){
                            break;
                        }
                    }
                    logger.info(VelocityHelper.generateSuggestedMatchesReport(suggestions));
                }
                
            }
            catch( ParseException exp ) {
                logger.error( "No command available for.{}",command);
            }
        }
        scanner.close();
        
    }

    private static Options createOptions() {
        Options options = new Options();
        options.addOption( "L", "list-players", false, "list of players sorted by score sorted by score, their ranking (position in the list) and their number of wins and losses" );
        options.addOption( Option.builder("R").longOpt( "player-match-report")
                .desc( "report for each person, showing with whom they played and how they fared exampleusage: -R Micheline or just -R" )
                .hasArgs()
                .optionalArg(true)
                .argName("playerName playerId")
                .build());
        
        options.addOption("S","suggestions",false,"list of suggested next matches");
        
        options.addOption( Option.builder("F").longOpt( "file-locations")
                .desc( "location of the files how they fared example usage: -F ./src/test/players.txt ./src/test/matchScores.txt" )
                .hasArgs()
                .argName("playerFilePath matchScoresFilePath")
                .build());
        
        options.addOption("E","exit",false,"exit from program");
        return options;
    }
    
    public static String getPlayersDefaultFilePath(String[] args) {
        if(args.length > 1){
            return args[0];
        }
        return "file:" + System.getProperty("user.dir") + File.separator + "players.txt";
    }
    
    public static String getMatcheScoresDefaultFilePath(String[] args) {
        if(args.length > 1){
            return args[1];
        }
        return "file:" + System.getProperty("user.dir") + File.separator + "matchScores.txt";
    }
}
