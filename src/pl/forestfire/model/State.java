package pl.forestfire.model;

public enum State
{
    SUSPECTED("green"),
    BURNING("red"),
    DEAD("black");

    private String color;

    State(String color)
    {
        this.color = color;
    }

    public String getColor()
    {
        return color;
    }
}