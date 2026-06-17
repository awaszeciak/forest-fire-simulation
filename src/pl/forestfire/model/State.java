package pl.forestfire.model;

/**
 * Enum reprezentujący możliwe stany komórki lasu.
 *
 * <p>
 *     Każdy stan posiada przypisany kolor wykorzystywany w wizualizacji
 *     oraz znak używany do oznaczania komórki w trybie tekstowym.
 * </p>
 */
public enum State
{
    /**
     * Zdrowe drzewo, które może się zapalić.
     */
    SUSPECTED("green", 's'),
    /**
     * Palące się drzewo, od którego pobliskie drzewa mogą się zapalić.
     */
    BURNING("red", 'b'),
    /**
     * Spalone drzewo, które nie może ponownie się zapalić.
     */
    DEAD("black", 'd');

    /**
     * Kolor przypisany do danego stanu.
     */
    private final String color;
    /**
     * Symbol tekstowy oznaczający dany stan.
     */
    private final char state;

    /**
     * Tworzy stan komórki z przypisanym kolorem i symbolem.
     * @param color kolor komórki w danym stanie
     * @param s znak oznaczający stan komórki
     */
    State(String color, char s)
    {
        this.color = color;
        state = s;
    }

    /**
     * Zwraca kolor przypisany do danego stanu.
     *
     * @return kolor stanu
     */
    public String getColor()
    {
        return color;
    }

    /**
     * Zwraca znak oznaczający dany stan.
     *
     * @return znak oznaczający stan
     */
    public char getChar() {return state;}
}