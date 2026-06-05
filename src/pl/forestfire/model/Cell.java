package pl.forestfire.model;

import java.util.Random;

public class Cell {
    public Cell(double alfa, double beta)
    {
        if (alfa < 0 || alfa > 1 || beta < 0 || beta > 1) {
            throw new IllegalArgumentException("Alfa i beta musi być między 0, a 1");
        }
        this.state = State.SUSPECTED;
        this.alfa = alfa;
        this.beta = beta;
    }

    public Cell(State state, double alfa, double beta)
    {
        if (alfa < 0 || alfa > 1 || beta < 0 || beta > 1) {
            throw new IllegalArgumentException("Alfa i beta musi być między 0, a 1");
        }
        this.state = state;
        this.alfa = alfa;
        this.beta = beta;
    }

    public boolean startBurning(double probability)
    {
        if (probability < 0 || probability > 1) {
            throw new IllegalArgumentException("Prawdopodobieństwo musi być między 0, a 1");
        }
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
        if (state == State.BURNING && random.nextDouble() < alfa)
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

    public void setState(State newState)
    {
        state = newState;
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
