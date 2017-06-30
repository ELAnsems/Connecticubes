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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * An abstract {@link Algorithm} implementation to be extended by the concrete strategies. It does
 * the following to make the next guess:
 * <ul>
 * <li>Based on the guesses and their scores so far, evaluate the set of all possible codes that
 * could be a solution.</li>
 * <li>If this set is non-empty, take its first member, assign it a rating of 0, and promote it as
 * our best guess so far.</li>
 * <li>Iterate through the set of all codes of the given game setup, and for each member calculate a
 * rating. Note that here we iterate through <strong>all</strong> codes, not just all
 * <strong>possible</strong> codes, since the best guess for a given algorithm could also be
 * impossible. Compare this calculated rating to the best rating we have so far, and promote it as
 * the best rating if it's higher, or if it's equal and the current code is possible, while the
 * current best guess is not possible.</li>
 * </ul>
 * 
 * <p>
 * This class has a single protected abstract method {@link #calculateGuessRating(String)}. All
 * concrete strategies extending this class should implement only this single method, as they differ
 * only in the way they rate the guesses.
 * 
 * <p>
 * This is the <a href="http://en.wikipedia.org/wiki/Template_method_pattern">Template Method</a>
 * design pattern in action.
 * 
 * @author Stoyan Rachev
 * 
 */
public abstract class AbstractAlgorithm implements Algorithm
{
    private static final double EPSILON = 0.0000001;

    private final transient CubeGame cubeGame;
    private final transient Map<String, Score> guessScores = new HashMap<String, Score>();
    private final transient Set<String> evaluated = new HashSet<String>();

    private transient SortedSet<String> possibleCodes;

    /**
     * Creates an abstract algorithm for the passed game setup.
     * 
     * @param cubeGame The game setup to use.
     */
    public AbstractAlgorithm(final CubeGame cubeGame)
    {
        //Gdx.app.log("Algorithm","AbstractAlgorithm AbstractAlgorithm");
        assert (cubeGame != null);
        this.cubeGame = cubeGame;
        initPossibleCodes();
    }

    private void initPossibleCodes()
    {
        //Gdx.app.log("Algorithm","AbstractAlgorithm initPossibleCodes");
        possibleCodes = new TreeSet<String>();
        possibleCodes.addAll(cubeGame.getAllPossibleCodes());
    }

    /**
     * Returns all possible scores for the current game setup. This method simply delegates to
     * {@link CubeGame#getAllPossibleScores()}.
     * 
     * @return All possible scores for the current game setup.
     */
    protected final List<Score> getAllPossibleScores()
    {
        //Gdx.app.log("Algorithm","AbstractAlgorithm getAllPossibleScores");
        return cubeGame.getAllPossibleScores();
    }

    /**
     * Returns all codes that are still possible (could be a solution), taking into account the
     * scores assigned to all previously made guesses.
     * 
     * @return All codes that are still possible.
     */
    public final SortedSet<String> getPossibleCodes()
    {
        return possibleCodes;
    }

    @Override
    public final String makeGuess()
    {
        //Gdx.app.log("Algorithm","AbstractAlgorithm makeGuess");
        evaluateGuesses();
        String guess = "";
        if (!possibleCodes.isEmpty())
        {
            guess = makeNextGuess();
        }
        assert (guess.equals("") == possibleCodes.equals(""));
        return guess;
    }

    private void evaluateGuesses()
    {
        //Gdx.app.log("Algorithm","AbstractAlgorithm evaluateGuesses");
        for (final String guess : guessScores.keySet())
        {
            if (!evaluated.contains(guess))
            {
                final Score score = guessScores.get(guess);
                possibleCodes = evaluatePossibleCodes(guess, score);
                evaluated.add(guess);
            }
        }
    }

    /**
     * Determines all codes from the still possible codes against which the passed guess evaluates
     * as the passed score. This method simply delegates to {@link
     * CubeGame#evaluatePossibleCodes(String, Score, SortedSet)}.
     * 
     * @param guess The guess to be checked.
     * @param score The score that we are aiming at.
     * @return All codes which satisfy the above conditions, sorted alphabetically.
     */
    protected final SortedSet<String> evaluatePossibleCodes(final String guess, final Score score)
    {
      //  //Gdx.app.log("Algorithm","AbstractAlgorithm evaluatePossibleCodes");
        return cubeGame.evaluatePossibleCodes(guess, score, possibleCodes);
    }

    public int getPossibleCodesSize(){
        evaluateGuesses();
        return possibleCodes.size();
    }

    private String makeNextGuess()
        {
            //Gdx.app.log("Algorithm","AbstractAlgorithm makeNextGuess");
        String bestGuess = possibleCodes.first();
        double maxRating = 0.0;
        final SortedSet<String> allPossibleCodes = cubeGame.getAllPossibleCodes();
        for (final String guess : allPossibleCodes)
        {
            final double rating = calculateGuessRating(guess);
            if (isBetterGuess(guess, rating, bestGuess, maxRating))
            {
                bestGuess = guess;
                maxRating = Math.max(maxRating, rating);
            }
        }
        Gdx.app.log("Algorithm","Number of possible solutions left = "+possibleCodes.size());
        return bestGuess;
    }

    /**
     * Calculates a rating for the passed guess. The calculated rating should be a double value
     * greater or equal to 0. Usually, the guess with the highest rating is the guess eventually
     * returned by {@link #makeGuess()}.
     * 
     * @param guess The guess for which a rating should be calculated.
     * @return The rating calculated for the passed guess.
     */
    protected abstract double calculateGuessRating(final String guess);

    // @checkstyle:off (Too many parameters)
    private boolean isBetterGuess(final String guess, final double rating, final String bestGuess,
        final double maxRating)
    // @checkstyle:on
    {
        //Gdx.app.log("Algorithm","AbstractAlgorithm isBetterGuess");
        boolean result = false;
        if (rating > maxRating)
        {
            result = true;
        }
        else if (Math.abs(maxRating - rating) < EPSILON)
        {
            result = possibleCodes.contains(guess) && !possibleCodes.contains(bestGuess);
        }
        return result;
    }

    @Override
    public final void putGuessScore(final String guess, final Score score)
    {
        //Gdx.app.log("Algorithm","AbstractAlgorithm putGuessScore");
        assert (cubeGame.isValidCode(guess) && cubeGame.isValidScore(score));
        guessScores.put(guess, score);
    }
}
