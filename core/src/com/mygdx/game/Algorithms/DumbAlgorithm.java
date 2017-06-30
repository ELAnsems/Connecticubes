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

/**
 * A "dumb" strategy, mainly intended to be used for testing. It always guesses the same code (the
 * first in the set of all possible codes), and so has almost no chance of winning. Unlike other
 * algorithms in this package, this algorithm does not extend {@link AbstractAlgorithm}, but rather
 * implements the {@link Algorithm} interface directly.
 * 
 * @author Stoyan Rachev
 */
public class DumbAlgorithm implements Algorithm
{
    private final transient String code;

    public DumbAlgorithm(final CubeGame cubeGame)
    {
        code = cubeGame.getAllPossibleCodes().first();
        assert cubeGame.isValidCode(code);
    }

    @Override
    public final String makeGuess()
    {
        return code;
    }

    @Override
    public final void putGuessScore(final String guess, final Score score)
    {
        // No implementation needed
    }

    public int getPossibleCodesSize(){
        return getPossibleCodesSize();
    }
}
