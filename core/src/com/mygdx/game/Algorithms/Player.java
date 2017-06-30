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
 * Interface to be implemented by players. A player interacts with an {@link Algorithm} within a
 * {@link Game} by providing scores for the guesses made by the algorithm.
 * 
 * @author Stoyan Rachev
 */
public interface Player
{
    /**
     * Starts the game for the player. This method is invoked by {@link Game#play()} before the game
     * actually starts. It gives the player a chance to perform initialization, display messages, or
     * carry out other preparation activities.
     */
    void startGame();

    /**
     * Ends the game for the player. This method is invoked by {@link Game#play()} after the game
     * has finished. It gives the player a chance to perform clean-up, display messages, or carry
     * out other finalization activities.
     * 
     * @param won true if the game has been won, false otherwise.
     * @param roundsPlayed The number of rounds played during the game.
     */
    void endGame(final boolean won, final int roundsPlayed);

    /**
     * Gets the score for the given guess. This method is invoked by {@link Game#play()} repeatedly
     * for each guess made by the {@link Algorithm}. The returned score is passed back to the
     * algorithm via the {@link Algorithm#putGuessScore(String, Score)} method.
     * 
     * @param guess The guess to provide a score for.
     * @return The score for the passed guess.
     */
    Score getScore(final String guess);
}
