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

/**
 * An exception thrown by any method in this package when a non-generic error is encountered.
 * 
 * @author Stoyan Rachev
 */
public class CubeGameException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public CubeGameException()
    {
        super();
    }

    public CubeGameException(final Exception exc)
    {
        super(exc);
        //Gdx.app.log("Algorithm","CubeGameException CubeGameException");
        assert (exc != null);
    }
}
