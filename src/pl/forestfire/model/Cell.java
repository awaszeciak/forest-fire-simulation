package pl.forestfire.model;

import java.util.Random;

/**
 * Klasa przedstwiająca pojedyńczą komórke (drzewo).
 */
public class Cell {
    /**
     * Konstruktor pojedyńczej komórki, domyslnie przyjmuje się stan na SUSPECTED
     * @param alfa podstawowe prawdopodobieństwo zapalenia
     * @param beta podstawowe prawdopodobieństwo spalenia sie
     */
    public Cell(double alfa, double beta)
    {
        if (alfa < 0 || alfa > 1 || beta < 0 || beta > 1) {
            throw new IllegalArgumentException("Alfa i beta musi być pomiędzy 0, a 1");
        }
        this.state = State.SUSPECTED;
        this.alfa = alfa;
        this.beta = beta;
    }

    /**
     * Konstruktor ustawiający prawdopodobieństwa oraz stan
     * @param state stan komórki
     * @param alfa pradopodobieństwo zapalenia
     * @param beta prawdopodobieństwo spalenia sie
     */
    public Cell(State state, double alfa, double beta)
    {
        if (alfa < 0 || alfa > 1 || beta < 0 || beta > 1) {
            throw new IllegalArgumentException("Alfa i beta musi być między 0, a 1");
        }
        this.state = state;
        this.alfa = alfa;
        this.beta = beta;
    }

    /**
     * Metoda odpowiadająca za zapalnie się pojedyńczej komórki
     * @param probability prawdoipodobieństwo zapalenia
     * @return czy sie zapaliła
     */
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

    /**
     * Metoda odpowiadająca za to czy komórka sie spaliła
     * @return czy się spaliła
     */
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

    /**
     * Metoda zwracająca obecny stan
     * @return stan
     */
    public State getState()
    {
        return state;
    }

    /**
     * Metoda wymuszająca zmiane stanu
     * @param newState nowy stan do ustawienia
     */
    public void setState(State newState)
    {
        state = newState;
    }

    /**
     * Stan komórki
     */
    private State state;

    /**
     * podstawowe prawdopodobieństwo zapalenia
     */
    private final double alfa;
    /**
     * Prawdopodobieństwo spalenia się
     */
    private final double beta;

    /**
     * Zwracanie ustawionego prawdopodonbieństwa zapelanie
     * @return prawdopodobieństwo zapalanie
     */
    public double getAlfa() {
        return alfa;
    }

    /**
     * Zwracanie ustawionego pradopodbieństwa spaleniania się
     * @return prawdopodobieństwo spalenia się
     */
    public double getBeta() {
        return beta;
    }
}
