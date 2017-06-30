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
 * A factory for {@link KnuthAlgorithm} instances.
 * 
 * @author Stoyan Rachev
 */
public class KnuthAlgorithmFactory implements AlgorithmFactory
{
    private final transient CubeGame cubeGame;
    
    /**
     * Creates a new factory with the specified game setup.
     * 
     * @param cubeGame The game setup to use.
     */
    public KnuthAlgorithmFactory(final CubeGame cubeGame)
    {
        //Gdx.app.log("Algorithm","KnuthAlgorithmFactory KnuthAlgorithmFactory");
        assert (cubeGame != null);
        this.cubeGame = cubeGame;
    }

    @Override
    public final Algorithm getAlgorithm()
    {

        //Gdx.app.log("Algorithm","KnuthAlgorithmFactory getAlgorithm");
        return new KnuthAlgorithm(cubeGame);
    }

}
