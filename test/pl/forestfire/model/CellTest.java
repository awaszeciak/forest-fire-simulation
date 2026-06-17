package pl.forestfire.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy jednostkowe klasy {@link Cell}.
 *
 * <p>
 *     Sprawdzają poprawność konstruktorów, walidację parametrów,
 *     zmianę stanów komórki oraz działanie metod odpowiedzialnych
 *     za zapalanie i spalanie drzewa.
 * </p>
 */
public class CellTest {
    @Test
    void constructorShouldSetDefaultStateToSuspected() {
        Cell cell = new Cell(0.4, 0.5);
        assertEquals(State.SUSPECTED, cell.getState());
    }

    @Test
    void constructorShouldSetGivenState() {
        Cell cell = new Cell(State.BURNING, 0.4, 0.5);
        assertEquals(State.BURNING, cell.getState());
    }

    @Test
    void constructorShouldThrowExceptionWhenAlfaIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> new Cell(-0.1, 0.5));
    }

    @Test
    void constructorShouldThrowExceptionWhenAlfaIsGreaterThanOne() {
        assertThrows(IllegalArgumentException.class, () -> new Cell(1.1, 0.5));
    }

    @Test
    void constructorShouldThrowExceptionWhenBetaIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> new Cell(0.5, -0.1));
    }

    @Test
    void constructorShouldThrowExceptionWhenBetaIsGreaterThanOne() {
        assertThrows(IllegalArgumentException.class, () -> new Cell(0.5, 1.1));
    }

    @Test
    void secondConstructorShouldThrowExceptionWhenAlfaIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> new Cell(State.SUSPECTED,-0.1, 0.5));
    }

    @Test
    void secondConstructorShouldThrowExceptionWhenAlfaIsGreaterThanOne() {
        assertThrows(IllegalArgumentException.class, () -> new Cell(State.SUSPECTED,1.1, 0.5));
    }

    @Test
    void secondConstructorShouldThrowExceptionWhenBetaIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> new Cell(State.SUSPECTED,0.5, -0.1));
    }

    @Test
    void secondConstructorShouldThrowExceptionWhenBetaIsGreaterThanOne() {
        assertThrows(IllegalArgumentException.class, () -> new Cell(State.SUSPECTED, 0.5, 1.1));
    }

    @Test
    void suspectedCellShouldStartBurningWhenProbabilityIsOne() {
        Cell cell = new Cell(State.SUSPECTED, 0.4, 0.5);

        boolean result = cell.startBurning(1.0);

        assertTrue(result);
        assertEquals(State.BURNING, cell.getState());
    }

    @Test
    void suspectedCellShouldNotStartBurningWhenProbabilityIsZero() {
        Cell cell = new Cell(State.SUSPECTED, 0.4, 0.5);

        boolean result = cell.startBurning(0.0);

        assertFalse(result);
        assertEquals(State.SUSPECTED, cell.getState());
    }

    @Test
    void startBurningShouldThrowExceptionWhenProbabilityIsNegative() {
        Cell cell = new Cell(State.SUSPECTED, 0.4, 0.5);

        assertThrows(IllegalArgumentException.class, () -> cell.startBurning(-0.1));
    }


    @Test
    void startBurningShouldThrowExceptionWhenProbabilityIsGreaterThanOne() {
        Cell cell = new Cell(State.SUSPECTED, 0.4, 0.5);

        assertThrows(IllegalArgumentException.class, () -> cell.startBurning(1.1));
    }

    @Test
    void burningCellShouldNotStartBurningAgain() {
        Cell cell = new Cell(State.BURNING, 0.4, 0.5);

        boolean result = cell.startBurning(1.0);

        assertFalse(result);
        assertEquals(State.BURNING, cell.getState());
    }

    @Test
    void deadCellShouldNotStartBurning() {
        Cell cell = new Cell(State.DEAD, 0.4, 0.5);

        boolean result = cell.startBurning(1.0);

        assertFalse(result);
        assertEquals(State.DEAD, cell.getState());
    }

    @Test
    void burningCellShouldBecomeDeadWhenAlfaIsOne() {
        Cell cell = new Cell(State.BURNING, 1.0, 0.5);

        boolean result = cell.burn();

        assertTrue(result);
        assertEquals(State.DEAD, cell.getState());
    }


    @Test
    void burningCellShouldNotBecomeDeadWhenAlfaIsZero() {
        Cell cell = new Cell(State.BURNING, 0.0, 0.5);

        boolean result = cell.burn();

        assertFalse(result);
        assertEquals(State.BURNING, cell.getState());
    }

    @Test
    void suspectedCellShouldNotBurn() {
        Cell cell = new Cell(State.SUSPECTED, 1.0, 0.5);

        boolean result = cell.burn();

        assertFalse(result);
        assertEquals(State.SUSPECTED, cell.getState());
    }

    @Test
    void deadCellShouldNotBurn() {
        Cell cell = new Cell(State.DEAD, 1.0, 0.5);

        boolean result = cell.burn();

        assertFalse(result);
        assertEquals(State.DEAD, cell.getState());
    }


    @Test
    void setStateShouldChangeCellState() {
        Cell cell = new Cell(0.4, 0.5);

        cell.setState(State.BURNING);

        assertEquals(State.BURNING, cell.getState());
    }


    @Test
    void getAlfaShouldReturnGivenAlfa() {
        Cell cell = new Cell(0.4, 0.5);

        assertEquals(0.4, cell.getAlfa());
    }

    @Test
    void getBetaShouldReturnGivenBeta() {
        Cell cell = new Cell(0.4, 0.5);

        assertEquals(0.5, cell.getBeta());
    }
}
