package org.knur.com;

import javax.annotation.Nullable;

public class PVPEventPair {
    private PVPEventPlayer left;
    private PVPEventPlayer right;

    private PVPEventPlayer winner = null;

    public PVPEventPlayer getWinner()
    {
        return winner;
    }

    public void setWinner(PVPEventPlayer winner)
    {
        if(!winner.equals(left) && !winner.equals(right))
            throw new Error("Invalid winner");

        this.winner = winner;
    }

    public PVPEventPlayer getLeftPlayer()
    {
        return left;
    }

    public PVPEventPlayer getRightPlayer()
    {
        return right;
    }
    public PVPEventPair(@Nullable PVPEventPlayer p1, @Nullable PVPEventPlayer p2)
    {
        left = p1;
        right = p2;
    }
}
