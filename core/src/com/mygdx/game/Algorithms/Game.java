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

import com.badlogic.gdx.Gdx;

import static com.mygdx.game.Algorithms.util.Logger.debug;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A single CubeGame game. A game is initialized with a {@link CubeGame} instance (game setup),
 * a particular {@link Algorithm}, max number of rounds, and a {@link Player} instance. To play a
 * game, call its {@link #play()} method. The two methods {@link #hasWon()} and
 * {@link #getRoundsPlayed} should be invoked only after the game has finished to determine whether
 * the game is won and the number of rounds it took. A game can be played only once.
 * 
 * @author Stoyan Rachev
 */
public class Game
{

    private final transient CubeGame cubeGame;
    private final transient Algorithm algorithm;
    private final transient int maxRounds;
    private final transient Player player;
    private final transient GuessCalculator calc;

    private transient boolean won = false;
    private transient int roundsPlayed = 0;
    // @formatter:off
    private transient Score[] scores = new Score[] { Score.ZERO_SCORE, Score.ZERO_SCORE, Score.ZERO_SCORE };
    // @formatter:on

    /**
     * Creates a new game with the specified setup, algorithm, max number of rounds, and player.
     * This constructor delegates to
     * {@link #Game(CubeGame, Algorithm, int, Player, GuessCalculator)} by passing null for the
     * guess calculator.
     * 
     * @param cubeGame The game setup to use.
     * @param algorithm The algorithm to use.
     * @param maxRounds Max number of rounds allowed. If the game is not won within this number of
     * rounds, it is terminated and declared "lost".
     * @param player The player to use.
     */
    public Game(final CubeGame cubeGame, final Algorithm algorithm, final int maxRounds,
                final Player player)
    {
        this(cubeGame, algorithm, maxRounds, player, null);

        //Gdx.app.log("Algorithm","Game Game(final CubeGame cubeGame, final Algorithm algorithm, final int maxRounds, final Player player)");
    }

    /**
     * Creates a new game with the specified setup, algorithm, max number of rounds, player, and
     * guess calculator.
     * 
     * @param cubeGame The game setup to use.
     * @param algorithm The algorithm to use.
     * @param maxRounds Max number of rounds allowed. If the game is not won within this number of
     * rounds, it is terminated and declared "lost".
     * @param player The player to use.
     * @param calc A guess calculator used to optimize the performance of multiple games played with
     * the same algorithm, can be null if a guess calculator should not be used.
     */
    public Game(final CubeGame cubeGame, final Algorithm algorithm, final int maxRounds,
                final Player player, final GuessCalculator calc)
    {
        //Gdx.app.log("Algorithm","Game Game(final CubeGame cubeGame, final Algorithm algorithm, final int maxRounds, final Player player, final GuessCalculator calc)");
        assert (cubeGame != null && algorithm != null && maxRounds > 0 && player != null);
        this.cubeGame = cubeGame;
        this.algorithm = algorithm;
        this.maxRounds = maxRounds;
        this.player = player;
        this.calc = calc;

        //Gdx.app.log("Algorithm","Game.Game   Generate random code: "+finalCode+ "==========================================");
    }

    /**
     * Plays the game. Calls {@link Player#startGame()} and {@link Player#endGame(boolean, int)} at
     * the beginning and at the end of the game respectively. At each game round, calls
     * {@link Algorithm#makeGuess()}, {@link Player#getScore(String)}, and
     * {@link Algorithm#putGuessScore(String, Score)} in this order, until a correct guess is made.
     * If this does not happen within the specified max number of rounds, it is terminated and
     * declared lost.
     * 
     * <p>
     * A game can be played only once. If this method is called for a second time on the same
     * instance, an exception is thrown.
     * 
     * @return true if the game is won, false otherwise.
     * @throws CubeGameException If the method is called for a second time on the same instance,
     * if an empty guess is returned by the algorithm, or if another unexpected condition occurs.
     */
    public final boolean play()
    {
        //Gdx.app.log("Algorithm","Game play");
        if (roundsPlayed > 0)
        {
            throw new CubeGameException();
        }
        player.startGame();
        while (roundsPlayed < maxRounds)
        {
            final Score score = playRound(roundsPlayed);
            roundsPlayed++;
            if (isWinningScore(score))
            {
                won = true;
                break;
            }
            shiftScores(score);
        }
        player.endGame(won, roundsPlayed);
        return won;
    }
    public void startPlayer(){
        player.startGame();
    }
    public void endPlayer(){
        player.endGame(won, roundsPlayed);
    }

    public Score playSingleRound(String theCode){

        //Gdx.app.log("Algorithm","Game playSingleRound, code is "+this.cubeGame.finalCode);
        String guess = makeGuess(roundsPlayed);

        Gdx.app.log("Algorithm", "Computer's optimal guess is: "+guess +"");

        Gdx.app.log("Algorithm", "Player's Guess is: "+theCode);
        Score testResult = this.cubeGame.evaluateScoreSafe(guess, this.cubeGame.finalCode);
        Gdx.app.log("Algorithm", "Testresult for Computer's guess =: "+testResult.toString());

        if(!theCode.equals("") && !theCode.equals("NA")){
            testResult = this.cubeGame.evaluateScoreSafe(theCode, this.cubeGame.finalCode);
            Gdx.app.log("Algorithm", "Testresult for Player's =: "+testResult.toString());
            guess=theCode;
        }

        assert cubeGame.isValidCode(guess);
        final Score score = player.getScore(guess);
        assert cubeGame.isValidScore(score);
        debug(guess + " => " + score);
        putGuessScore(guess, score);

        roundsPlayed++;
        if (isWinningScore(score))
        {
            won = true;
        }
        shiftScores(score);
        return score;
    }

    private boolean isWinningScore(final Score score)
    {

        //Gdx.app.log("Algorithm","Game isWinningScore");
        return score.equals(cubeGame.getWinningScore());
    }

    private void shiftScores(final Score score)
    {
        //Gdx.app.log("Algorithm","Game shiftScores");
        final Score[] scoresx = new Score[scores.length];
        System.arraycopy(scores, 0, scoresx, 1, scores.length - 1);
        scoresx[0] = score;
        scores = scoresx;
    }

    public Score playRound(final int round)
    {
        Gdx.app.log("Algorithm","Game playRound, code is "+this.cubeGame.finalCode);
        String guess = makeGuess(round);

        Gdx.app.log("Algorithm", "Computer's optimal guess is: "+guess +"");

        Scanner sc = new Scanner(System.in);
        String testGuess = "NA";
        testGuess =sc.next();
        Gdx.app.log("Algorithm", "Player's testGuess is: "+testGuess);
        Score testResult = this.cubeGame.evaluateScoreSafe(guess, this.cubeGame.finalCode);
        Gdx.app.log("Algorithm", "Testresult for Computer's guess =: "+testResult.toString());

        if(!testGuess.equals("") && !testGuess.equals("NA")){
            testResult = this.cubeGame.evaluateScoreSafe(testGuess, this.cubeGame.finalCode);
            Gdx.app.log("Algorithm", "Testresult for testGuess =: "+testResult.toString());
            guess=testGuess;
        }

        assert cubeGame.isValidCode(guess);
        final Score score = player.getScore(guess);
        assert cubeGame.isValidScore(score);
        debug(guess + " => " + score);
        putGuessScore(guess, score);
        return score;
    }

    private String makeGuess(final int round)
    {
        //Gdx.app.log("Algorithm","Game makeGuess");
        String guess;
        if ((calc != null) && calc.hasGuesses(round))
        {
            final List<Score> scoresx = makeScoresForCalc(round);
            guess = calc.getGuess(scoresx, round);
        }
        else
        {
            guess = algorithm.makeGuess();
        }
        if (guess.equalsIgnoreCase(""))
        {
            //Gdx.app.log("Algorithm", " Game.makeGuess ERROR Guess = empty" + guess);
            throw new CubeGameException();
        }
        return guess;
    }

    private List<Score> makeScoresForCalc(final int round)
    {
        //Gdx.app.log("Algorithm","Game makeScoresForCalc");
        final List<Score> result = new ArrayList<Score>();
        for (int i = round - 1; i >= 0; i--)
        {
            result.add(scores[i]);
        }
        return result;
    }

    public int getPossibleCodesSize(){
        return algorithm.getPossibleCodesSize();
    }

    private void putGuessScore(final String guess, final Score score)
    {

        //Gdx.app.log("Algorithm","Game putGuessScore");
        algorithm.putGuessScore(guess, score);
    }

    /**
     * Returns whether the game has been won or not. Should only be called after the game has been
     * played, i.e. the {@link #play()} method has finished.
     * 
     * @return true if the game has been won, false otherwise.
     */
    public final boolean hasWon()
    {
        //Gdx.app.log("Algorithm","hasWon");
        assert isOver();
        return won;
    }

    /**
     * Returns the actual number of rounds played. Should only be called after the game has been
     * played, i.e. the {@link #play()} method has finished.
     * 
     * @return The actual number of rounds played.
     */
    public final int getRoundsPlayed()
    {
        //Gdx.app.log("Algorithm","Game getRoundsPlayed");
      //  assert isOver();
        return roundsPlayed;
    }

    private boolean isOver()
    {
        //Gdx.app.log("Algorithm","Game isOver");
        return (roundsPlayed > 0);
    }
}
