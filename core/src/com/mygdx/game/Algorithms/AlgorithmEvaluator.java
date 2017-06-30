/*
 * $Id: $
 *
 * Copyright 2012 Stoyan Rachev (stoyanr@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mygdx.game.Algorithms;

import static com.mygdx.game.Algorithms.util.Logger.debug;
import static com.mygdx.game.Algorithms.util.Logger.info;

/**
 * An evaluator of the effectiveness of the different strategies. It is initialized with a
 * {@link CubeGame} instance (game setup), and an {@link AlgorithmFactory}. To perform an
 * evaluation, call the {@link #evaluate()} method of the evaluator. The remaining methods of this
 * class should be invoked only after the evaluation is complete to determine the various statistics
 * produced by the evaluation, among them the average rounds played which is the main characteristic
 * used to measure the strategy effectiveness. An evaluation can be performed only once.
 * 
 * <p>
 * Performing an evaluation requires playing all possible games for a given game setup. This is
 * computationally hard and can take a lot of time, especially for larger alphabets and combination
 * lengths. To alleviate this issue, an instance of the {@link GuessCalculator} class is created
 * upon construction and used during the evaluation.
 * 
 * @author Stoyan Rachev
 */
public class AlgorithmEvaluator
{
    public static final int MAX_ROUNDS = 10;

    private final transient CubeGame cubeGame;
    private final transient AlgorithmFactory factory;
    private final transient GuessCalculator calc;

    private transient int totalRoundsPlayed = 0;
    private transient int maxRoundsPlayed = 0;
    private transient int gamesPlayed = 0;
    private transient int gamesWon = 0;

    /**
     * Creates a new evaluator for the specified setup and algorithm.
     * 
     * @param cubeGame The game setup to use.
     * @param factory The algorithm factory used to produce multiple instances of the algorithm
     * being evaluated.
     * @param levels The number of levels passed to the {@link GuessCalculator} constructor.
     */
    public AlgorithmEvaluator(final CubeGame cubeGame, final AlgorithmFactory factory,
                              final int levels)
    {
        //Gdx.app.log("Algorithm","AlgorithmEvaluator AlgorithmEvaluator");
        assert (cubeGame != null && factory != null);
        this.cubeGame = cubeGame;
        this.factory = factory;
        this.calc = new GuessCalculator(cubeGame, factory, levels);
    }

    /**
     * Evaluates the strategy effectiveness. This method delegates to
     * {@link CubeGame#visitCodes(CodeVisitor)} by passing a special visitor which plays a game
     * for each visited code. Finally, it prints the evaluation statistics.
     */
    public final void evaluate()
    {
        //Gdx.app.log("Algorithm", " AlgorithmEvaluator.evaluate()");
        cubeGame.visitCodes(new GamesVisitor());
        printInfo();
    }

    class GamesVisitor implements CodeVisitor
    {
        @Override
        public void visit(final String code)
        {
            assert cubeGame.isValidCode(code);
            debug("Code: " + code);
            final Player player = new DefaultPlayer(cubeGame, code);
            final Algorithm algorithm = factory.getAlgorithm();
            final Game game = new Game(cubeGame, algorithm, MAX_ROUNDS, player, calc);
            final boolean won = game.play();
            final int roundsPlayed = game.getRoundsPlayed();
            debugGameWon(won, roundsPlayed);
            updateStatistics(won, roundsPlayed);
        }

        private void debugGameWon(final boolean won, final int roundsPlayed)
        {
            if (won)
            {
                debug("Game won in " + roundsPlayed + " round(s)");
            }
            else
            {
                debug("Game lost in " + roundsPlayed + " round(s)");
            }
        }

        private void updateStatistics(final boolean won, final int roundsPlayed)
        {
            totalRoundsPlayed += roundsPlayed;
            maxRoundsPlayed = Math.max(maxRoundsPlayed, roundsPlayed);
            gamesPlayed++;
            if (won)
            {
                gamesWon++;
            }
        }
    }

    private void printInfo()
    {
        //Gdx.app.log("Algorithm", " AlgorithmEvaluator.printInfo Algorithm Evaluation for " + factory.getAlgorithm().getClass());
        //Gdx.app.log("Algorithm", " ===============================================");
        //Gdx.app.log("Algorithm", " Total Rounds Played: " + totalRoundsPlayed);
        //Gdx.app.log("Algorithm", " Max Rounds Played: " + maxRoundsPlayed);
        //Gdx.app.log("Algorithm", " Games Played: " + gamesPlayed);
        //Gdx.app.log("Algorithm", " Games Won: " + gamesWon);
        //Gdx.app.log("Algorithm", "Average Rounds Played: " + getAverageRoundsPlayed());

        info("Algorithm Evaluation for " + factory.getAlgorithm().getClass());
        info("===============================================");
        info("Total Rounds Played: " + totalRoundsPlayed);
        info("Max Rounds Played: " + maxRoundsPlayed);
        info("Games Played: " + gamesPlayed);
        info("Games Won: " + gamesWon);
        info("Average Rounds Played: " + getAverageRoundsPlayed());
        info("");
    }

    /**
     * Returns the total number of rounds played in all games during the evaluation. Should only be
     * called after the evaluation is complete, i.e. the {@link #evaluate()} method has finished.
     * 
     * @return The total number of rounds played in all games during the evaluation.
     */
    public final int getTotalRoundsPlayed()
    {
        assert hasFinished();
        return totalRoundsPlayed;
    }

    /**
     * Returns the max number of rounds played in any of the games during the evaluation. Should
     * only be called after the evaluation is complete, i.e. the {@link #evaluate()} method has
     * finished.
     * 
     * @return The max number of rounds played in any of the games during the evaluation.
     */
    public final int getMaxRoundsPlayed()
    {
        assert hasFinished();
        return maxRoundsPlayed;
    }

    /**
     * Returns the number of games played during the evaluation. Should only be called after the
     * evaluation is complete, i.e. the {@link #evaluate()} method has finished.
     * 
     * @return The number of games played during the evaluation.
     */
    public final int getGamesPlayed()
    {
        assert hasFinished();
        return gamesPlayed;
    }

    /**
     * Returns the number of games won during the evaluation. Should only be called after the
     * evaluation is complete, i.e. the {@link #evaluate()} method has finished.
     * 
     * @return The number of games won during the evaluation.
     */
    public final int getGamesWon()
    {
        assert hasFinished();
        return gamesWon;
    }

    /**
     * Returns the average number of rounds played during the evaluation, which is the total number
     * of rounds played in all games divided by the number of played games. Should only be called
     * after the evaluation is complete, i.e. the {@link #evaluate()} method has finished.
     * 
     * @return The average number of rounds played during the evaluation.
     */
    public final double getAverageRoundsPlayed()
    {
        assert hasFinished();
        return (double) totalRoundsPlayed / (double) gamesPlayed;
    }

    private boolean hasFinished()
    {
        return (gamesPlayed > 0);
    }
}
