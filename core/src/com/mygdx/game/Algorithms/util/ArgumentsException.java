package com.mygdx.game.Algorithms.util;

public class ArgumentsException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public ArgumentsException()
    {
        super();
    }

    public ArgumentsException(final Exception exc)
    {
        super(exc);
        assert (exc != null);
    }

}
