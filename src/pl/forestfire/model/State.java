package pl.forestfire.model;

public enum State
{
    SUSPECTED("green", 's'),
    BURNING("red", 'b'),
    DEAD("black", 'd');

    private final String color;
    private final char state;

    State(String color, char s)
    {
        this.color = color;
        state = s;
    }

    public String getColor()
    {
        return color;
    }
    public char getChar() {return state;}
}