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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;

/**
 * A concrete {@link Player} implementation for "human" players that use I/O streams, such as the
 * console, for interacting with a {@link Game}. Extends {@link AbstractIOPlayer} to read from a
 * {@link java.io.Reader} and write to a {@link java.io.Writer}.
 * 
 * @author Stoyan Rachev
 */
public class ReaderWriterPlayer extends AbstractIOPlayer
{
    private final transient BufferedReader reader;
    private final transient PrintWriter writer;

    /**
     * Creates a new player with the given game setup, reader, and writer.
     * 
     * @param cubeGame The game setup to use.
     * @param reader The reader to read from.
     * @param writer The writer to write to.
     */
    public ReaderWriterPlayer(final CubeGame cubeGame, final Reader reader, final Writer writer)
    {
        super(cubeGame);
        //Gdx.app.log("Algorithm","ReaderWriterPlayer ReaderWriterPlayer");
        assert (reader != null && writer != null);
        this.reader = new BufferedReader(reader);
        this.writer = new PrintWriter(writer, true);
    }

    @Override
    protected final void printLine(final String message, final Object... args)
    {
        //Gdx.app.log("Algorithm", "printLine: " + message);
        assert (message != null);
        writer.printf(message + "\n", args);
        writer.flush();
    }

    @Override
    protected final int readLineInt(final String message)
    {
        //Gdx.app.log("Algorithm","ReaderWriterPlayer readLineInt");
        assert (message != null);
        print(message);
        final String line = readLine();
        return parseInt(line);
    }

    private void print(final String message)
    {
        //Gdx.app.log("Algorithm","ReaderWriterPlayer print");
        writer.print(message);
        writer.flush();
    }

    private String readLine()
    {
        //Gdx.app.log("Algorithm","ReaderWriterPlayer readLine");
        String line;
        try
        {
            line = reader.readLine();
            if (line == null)
            {
                throw new CubeGameException();
            }
        }
        catch (final IOException e)
        {
            throw new CubeGameException(e);
        }
        return line;
    }

    private int parseInt(final String text)
    {
        //Gdx.app.log("Algorithm","ReaderWriterPlayer parseInt");
        int result;
        try
        {
            result = Integer.parseInt(text);
        }
        catch (final NumberFormatException e)
        {
            throw new CubeGameException(e);
        }
        return result;
    }
}
