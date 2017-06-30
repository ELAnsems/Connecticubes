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
 * Interface to be implemented by algorithm factory implementations. This interface has a single
 * method which returns a newly created algorithm of the correct type.
 * 
 * <p>
 * Algorithm factories are intended to be used whenever a large number of games should be played
 * with the same algorithm, for example when evaluating the effectiveness of the different
 * strategies. A single {@link Algorithm} instance can't be reused for multiple games, as algorithms
 * are mutable, that is playing a single game changes their state in a way that makes them
 * unsuitable for playing a second one.
 * 
 * <p>
 * This is the <a href="http://en.wikipedia.org/wiki/Abstract_factory_pattern">Abstract Factory</a>
 * design pattern in action.
 * 
 * @author Stoyan Rachev
 */
public interface AlgorithmFactory
{
    /**
     * Returns a newly created algorithm of the correct type.
     * 
     * @return A newly created algorithm of the correct type.
     */
    Algorithm getAlgorithm();
}
