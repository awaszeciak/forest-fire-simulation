package pl.forestfire.model;

import java.util.Random;

public class Cell {
    public Cell()
    {
        state = State.SUSPECTED;
    }

    Cell(State state)
    {
        this.state = state;
    }

    public boolean startBurning()
    {
        Random random = new Random();
        if (state == State.SUSPECTED && random.nextDouble()<alfa)
        {
            state = State.BURNING;
            return true;
        }
        return false;
    }

    public boolean burn()
    {
        Random random = new Random();
        if (state == State.BURNING && random.nextDouble()<beta)
        {
            state = State.DEAD;
            return true;
        }
        return false;
    }

    public State getState()
    {
        return state;
    }

    public void setState(State new_state)
    {
        state = new_state;
    }

    private State state;

    private final double alfa=1;
    private final double beta=0.1;
}
