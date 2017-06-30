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

import java.util.SortedSet;

/**
 * An implementation of the "worst-case" CubeGame strategy, first published by Donald Knuth. This
 * strategy attempts to minimize the number of questions by always asking the question for which the
 * largest number of combinations that yield any given answer is minimal.
 * 
 * <p>
 * This strategy is described in details in the paper <a
 * href="http://www.philos.rug.nl/~barteld/master.pdf">Yet Another CubeGame Strategy</a> by
 * Barteld Kooi.
 * 
 * @author Stoyan Rachev
 */
public class KnuthAlgorithm extends AbstractAlgorithm
{
    /**
     * Creates a new "worst-case" strategy for the specified game setup.
     * 
     * @param cubeGame The game setup to use.
     */
    public KnuthAlgorithm(final CubeGame cubeGame)
    {
        super(cubeGame);
        //Gdx.app.log("Algorithm","KnuthAlgorithm KnuthAlgorithm");
    }

    @Override
    protected final double calculateGuessRating(final String guess)
    {
        //Gdx.app.log("Algorithm","KnuthAlgorithm calculateGuessRating");
        int maxSize = 0;
        for (final Score score : getAllPossibleScores())
        {
            final SortedSet<String> codes = evaluatePossibleCodes(guess, score);
            maxSize = Math.max(maxSize, codes.size());
        }
        return (getPossibleCodes().size() - maxSize);
    }
}
