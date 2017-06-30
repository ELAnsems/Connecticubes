package com.mygdx.game.Algorithms;

/**
 * A CubeGame score, or "answer". This is a pair of two numbers (cows, bulls), where "bulls" is
 * the number of pawns that are of the right color and in the right place, and "cows" is the number
 * of pawns that are of the right color, but are not in the right place.
 *
 * @author Stoyan Rachev
 */
public class Score
{
    public static final Score ZERO_SCORE = new Score(0, 0);

    private final transient int cows;
    private final transient int bulls;

    public Score(final int cows, final int bulls)
    {
     //   //Gdx.app.log("Algorithm","Score Score");
        this.cows = cows;
        this.bulls = bulls;
    }

    public int getCows(){
        return cows;
    }
    public int getBulls(){
        return bulls;
    }

    @Override
    public final String toString()
    {

        //Gdx.app.log("Algorithm","Score toString");
        return "(" + cows + ", " + bulls + ")";
    }

    @Override
    public final boolean equals(final Object obj)
    {
     //   //Gdx.app.log("Algorithm","Score equals");
        boolean result = false;
        if (obj instanceof Score)
        {
            final Score other = (Score) obj;
            result = (cows == other.cows) && (bulls == other.bulls);
        }
        return result;
    }

    @Override
    public final int hashCode()
    {
        //Gdx.app.log("Algorithm","Score hashCode");
        return bulls * (CubeGame.MAX_LENGTH + 1) + cows;
    }

}
