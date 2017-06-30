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
 * Interface to be implemented by strategy implementations. This interface has two methods, one for
 * making a guess, and another for accepting a score for the guess. During a game, these methods are
 * invoked repeatedly until a correct guess is made, or until the max number of guesses is reached.
 * 
 * @author Stoyan Rachev
 */
public interface Algorithm
{
    /**
     * Makes a next guess based on the implemented strategy. Should take into account the scores
     * assigned to all previously made guesses.
     * 
     * @return The guess made by the algorithm.
     */
    String makeGuess();

    /**
     * Accepts a score for the last guess returned by {@link #makeGuess()}.
     * 
     * @param guess The guess which is rated with the passed score. Normally, this should be the
     * last guess returned by {@link #makeGuess()}.
     * @param score The score assigned to the passed guess.
     */
    void putGuessScore(final String guess, final Score score);


    int getPossibleCodesSize();
}
