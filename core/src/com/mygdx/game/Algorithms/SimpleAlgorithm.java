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
 * An implementation of the "simple" CubeGame strategy. In this strategy, the next question is the
 * first one in the ordering that is consistent with the answers given so far.
 * 
 * <p>
 * This strategy is described in details in the paper <a
 * href="http://www.philos.rug.nl/~barteld/master.pdf">Yet Another CubeGame Strategy</a> by
 * Barteld Kooi.
 * 
 * @author Stoyan Rachev
 */
public class SimpleAlgorithm extends AbstractAlgorithm
{
    /**
     * Creates a new "simple" strategy for the specified game setup.
     * 
     * @param cubeGame The game setup to use.
     */
    public SimpleAlgorithm(final CubeGame cubeGame)
    {
        super(cubeGame);
    }

    @Override
    protected final double calculateGuessRating(final String guess)
    {
        return 0.0;
    }
}
