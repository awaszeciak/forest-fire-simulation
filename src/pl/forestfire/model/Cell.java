package pl.forestfire.model;

public class Cell {
    public Cell()
    {
        state = State.SUSPECTED;
    }

    Cell(State state)
    {
        this.state = state;
    }

    boolean start_burning()
    {
        Random random = new Random();
        if (state == State.SUSPECTED && random.nextDouble()<alfa)
        {
            state = State.BURNING;
            return true;
        }
        return false;
    }

    boolean burn()
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

    private State state;

    private final double alfa=1;
    private final double beta=0.1;
}
