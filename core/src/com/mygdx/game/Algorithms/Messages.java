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

import static com.mygdx.game.Algorithms.util.Messages.*;

/**
 * A utility class to retrieve messages from a resource file.
 * 
 * @author Stoyan Rachev
 */
public final class Messages
{
    public static final String PACKAGE = "Algorithm step:";

    private static final String BUNDLE_NAME = PACKAGE + ".messages";

    // @formatter:off
    public static final String M_C_STARTING_GAME        = msg(0);
    public static final String M_C_GAME_WON             = msg(1);
    public static final String M_C_GAME_LOST            = msg(2);
    public static final String M_C_GUESS                = msg(3);
    public static final String M_C_COWS                 = msg(4);
    public static final String M_C_BULLS                = msg(5);
    // @formatter:on

    private Messages()
    {
        // No implementation needed
    }

    public static String msg(final int key)
    {
      //  //Gdx.app.log("Algorithm","Messages msg");
        assert (key >= 0);
        return getMessage(BUNDLE_NAME, PACKAGE + "." + key);
    }
}
