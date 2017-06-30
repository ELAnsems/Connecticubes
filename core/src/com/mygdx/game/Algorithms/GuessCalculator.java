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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A performance optimization facility which calculates and stores the guesses made by a particular
 * algorithm for a given game setup, for up to a predefined number of game rounds (or "levels").
 * 
 * @author Stoyan Rachev
 */
public class GuessCalculator
{
    private final transient CubeGame cubeGame;
    private final transient AlgorithmFactory factory;
    private final transient int levels;

    private final transient List<Score> allScores;
    private final transient Object[] objects;
    private final transient String[] guesses;

    /**
     * Creates a new guess calculator for the specified game setup, algorithm, and levels. This
     * constructor performs the computationally hard guesses initialization, so that subsequent
     * method invocations are very fast.
     * 
     * @param cubeGame The game setup to use.
     * @param factory The algorithm factory used to produce multiple instances of the algorithm
     * being evaluated.
     * @param levels The number of game rounds to calculate and store guesses for.
     */
    public GuessCalculator(final CubeGame cubeGame, final AlgorithmFactory factory,
                           final int levels)
    {
        //Gdx.app.log("Algorithm","GuessCalculator GuessCalculator");
        assert (cubeGame != null && factory != null && levels > 0);
        this.cubeGame = cubeGame;
        this.factory = factory;
        this.levels = levels;
        this.allScores = cubeGame.getAllPossibleScores();
        this.objects = new Object[levels];
        this.guesses = new String[levels];
        initGuesses();
    }

    private void initGuesses()
    {
        //Gdx.app.log("Algorithm","GuessCalculator initGuesses");
        for (int i = 0; i < levels; i++)
        {
            initGuesses(new ArrayList<Score>(), i, 0);
        }
    }

    private void initGuesses(final List<Score> scores, final int level, final int depth)
    {
        //Gdx.app.log("Algorithm","GuessCalculator initGuesses(final List<Score> scores, final int level, final int depth)");
        if (depth == level)
        {
            makeAndAddGuess(scores, level);
        }
        else
        {
            for (final Score score : allScores)
            {
                final List<Score> scoresx = makeScores(scores, score);
                final boolean canMakeGuess =
                    ((depth < level - 1) ? hasGuess(scoresx, depth + 1) : true);
                if (isNotWinningScore(score) && canMakeGuess)
                {
                    initGuesses(scoresx, level, depth + 1);
                }
            }
        }
    }

    private void makeAndAddGuess(final List<Score> scores, final int level)
    {
        //Gdx.app.log("Algorithm","GuessCalculator makeAndAddGuess");
        final String guess = makeGuess(scores, level);
        if (!guess.isEmpty())
        {
            addGuess(scores, level, guess);
        }
    }

    private String makeGuess(final List<Score> scores, final int level)
    {
        //Gdx.app.log("Algorithm","GuessCalculator makeGuess");
        final Algorithm algorithm = factory.getAlgorithm();
        for (int i = 0; i < level; i++)
        {
            final String guess = (i == 0) ? (String) objects[i] : guesses[i];
            algorithm.putGuessScore(guess, scores.get(i));
        }
        return algorithm.makeGuess();
    }

    private void addGuess(final List<Score> scores, final int level, final String guess)
    {
        //Gdx.app.log("Algorithm","GuessCalculator addGuess");
        assert (level < levels);
        if (level == 0)
        {
            objects[level] = guess;
        }
        else
        {
            final Map<Score, Object> map = getFirstMap(level);
            addGuessToMap(scores, level, guess, map, 0);
        }
    }

    @SuppressWarnings("unchecked")
    private Map<Score, Object> getFirstMap(final int level)
    {
        //Gdx.app.log("Algorithm","GuessCalculator getFirstMap");
        Map<Score, Object> map = (Map<Score, Object>) objects[level];
        if (map == null)
        {
            map = new HashMap<Score, Object>();
            objects[level] = map;
        }
        return map;
    }

    // @checkstyle:off (Too many parameters)
    private void addGuessToMap(final List<Score> scores, final int level, final String guess,
        final Map<Score, Object> map, final int depth)
    // @checkstyle:on
    {
        //Gdx.app.log("Algorithm","GuessCalculator addGuessToMap");
        final Score score = scores.get(depth);
        if (depth == level - 1)
        {
            map.put(score, guess);
        }
        else
        {
            final Map<Score, Object> mapx = getNextMap(map, score);
            addGuessToMap(scores, level, guess, mapx, depth + 1);
        }
    }

    @SuppressWarnings("unchecked")
    private Map<Score, Object> getNextMap(final Map<Score, Object> map, final Score score)
    {
        //Gdx.app.log("Algorithm","GuessCalculator getNextMap");
        Map<Score, Object> mapx = (Map<Score, Object>) map.get(score);
        if (mapx == null)
        {
            mapx = new HashMap<Score, Object>();
            map.put(score, mapx);
        }
        return mapx;
    }

    private List<Score> makeScores(final List<Score> scores, final Score score)
    {
        //Gdx.app.log("Algorithm","GuessCalculator makeScores");
        final List<Score> result = new ArrayList<Score>(scores.size() + 1);
        result.addAll(scores);
        result.add(score);
        return result;
    }

    private boolean hasGuess(final List<Score> scores, final int level)
    {
        //Gdx.app.log("Algorithm","GuessCalculator hasGuess");
        guesses[level] = getGuess(scores, level);
        return (!guesses[level].isEmpty());
    }

    private boolean isNotWinningScore(final Score score)
    {

        //Gdx.app.log("Algorithm","GuessCalculator isNotWinningScore");
        return !score.equals(cubeGame.getWinningScore());
    }

    /**
     * Returns true if the calculator has precalculated guesses for the specified level (game
     * round), which is the case if this level is lower than the calculator levels.
     * 
     * @param level The level to check.
     * @return true if the calculator has precalculated guesses for the specified level, false
     * otherwise.
     */
    public final boolean hasGuesses(final int level)
    {
        //Gdx.app.log("Algorithm","GuessCalculator hasGuesses");
        assert (level >= 0);
        return (level < levels);
    }

    /**
     * Returns the precalculated guess for the specified level and previously produced scores.
     * 
     * @param scores A list of scores produced during the previous game rounds.
     * @param level The level for which to return the precalculated guess.
     * @return The precalculated guess for the specified level and scores.
     */
    public final String getGuess(final List<Score> scores, final int level)
    {
        //Gdx.app.log("Algorithm","GuessCalculator getGuess(final List<Score> scores, final int level)");
        assert hasGuesses(level);
        assert (level > 0) ? (scores != null && !scores.isEmpty()) : true;
        return getGuess(scores, level, objects[level], 0);
    }

    @SuppressWarnings("unchecked")
    // @checkstyle:off (Too many parameters)
    private String getGuess(final List<Score> scores, final int level, final Object obj,
        final int depth)
    // @checkstyle:on
    {
        //Gdx.app.log("Algorithm","GuessCalculator getGuess(final List<Score> scores, final int level, final Object obj, final int depth)");
        String result = "";
        if (depth == level)
        {
            result = (String) obj;
        }
        else
        {
            final Map<Score, Object> map = (Map<Score, Object>) obj;
            final Object objx = map.get(scores.get(depth));
            if (objx != null)
            {
                result = getGuess(scores, level, objx, depth + 1);
            }
        }
        return result;
    }

}
