package pl.forestfire.model;

import java.util.Random;

public class Cell {
    public Cell(double alfa, double beta)
    {
        this.state = State.SUSPECTED;
        this.alfa = alfa;
        this.beta = beta;
    }

    public Cell(State state, double alfa, double beta)
    {
        this.state = state;
        this.alfa = alfa;
        this.beta = beta;
    }

    public boolean startBurning(double probability)
    {
        Random random = new Random();
        if (state == State.SUSPECTED && random.nextDouble() < probability)
        {
            state = State.BURNING;
            return true;
        }
        return false;
    }

    public boolean burn()
    {
        Random random = new Random();
        if (state == State.BURNING && random.nextDouble() < beta)
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

    private final double alfa;
    private final double beta;

    public double getAlfa() {
        return alfa;
    }

    public double getBeta() {
        return beta;
    }
}
