package pl.forestfire.model;

enum State
{
    SUSPECTED("green"),
    BURNING("red"),
    DEAD("black");

    private String color;

    State(String color)
    {
        this.color = color;
    }

    String get_color()
    {
        return color;
    }
}