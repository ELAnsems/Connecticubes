package com.mygdx.game.Algorithms.util;

public final class Logger
{
    enum Level
    {
        ERROR, WARNING, INFO, DEBUG
    }

    private Logger()
    {
        // No implementation needed
    }

    private static Level level = Level.INFO;

    public static void error(final String message)
    {
        if (levelHigherThan(Level.ERROR))
        {
            log(message);
        }
    }

    public static void warning(final String message)
    {
        if (levelHigherThan(Level.WARNING))
        {
            log(message);
        }
    }

    public static void info(final String message)
    {
        if (levelHigherThan(Level.INFO))
        {
            log(message);
        }
    }

    public static void debug(final String message)
    {
        if (levelHigherThan(Level.DEBUG))
        {
            log(message);
        }
    }

    private static boolean levelHigherThan(final Level levelx)
    {
        return level.ordinal() >= levelx.ordinal();
    }

    private static void log(final String message)
    {
        System.out.println(message); // NOPMD SystemPrintln
    }
}
