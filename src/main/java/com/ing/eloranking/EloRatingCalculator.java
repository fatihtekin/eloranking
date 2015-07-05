package com.ing.eloranking;


public class EloRatingCalculator {
        
        /**
         * Get new rating.
         * 
         * @param rating
         *            Rating of the current player 
         * @param opponentRating
         *            Rating of the opponent player
         * @param score
         *            Score: Loss, Draw or Win
         * @return the new rating
         */
        public static MatchRatings getNewRating(double rating, double opponentRating, ScoreType score) {
                double kFactor       = 32D;
                double expectedScore = getExpectedScore(rating, opponentRating);
                double    newRating     = calculateNewRating(rating, score, expectedScore, kFactor);                
                return new MatchRatings(newRating, opponentRating-(newRating-rating));
        }
        
        public static void main(String[] args) {
            MatchRatings matchRatings = getNewRating(2400, 2000, ScoreType.LOSS);
            System.out.println(matchRatings.getCurrentPlayerRank()+" "+matchRatings.getOpponentRank());
            
        }
        
        
        /**
         * Calculate the new rating based on the ELO standard formula.
         * newRating = oldRating + constant * (score - expectedScore)
         * 
         * @param oldRating     Old Rating
         * @param score                 Score
         * @param expectedScore Expected Score
         * @param constant              Constant
         * @return                              the new rating of the player
         */
        private static double calculateNewRating(double oldRating, ScoreType score, double expectedScore, double kFactor) {
                return oldRating + (double) (kFactor * (score.getValue() - expectedScore));
        }
        
        /**
         * Get expected score based on two players. 
         * @param rating                        Rating
         * @param opponentRating        Opponent(s) rating
         * @return  the expected score
         */
        private static double getExpectedScore (double rating, double opponentRating) {
                return 1.0 / (1.0 + Math.pow(10.0, ((double) (opponentRating - rating) / 400.0)));
        }

}