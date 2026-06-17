package pl.forestfire.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy klasy {@link Forest}.
 *
 * <p>
 *     Sprawdzają poprawne tworzenie lasu, inicjalizację komórek,
 *     walidację parametrów, dostęp do komórek oraz aktualizację liczników
 *     stanów komórek.
 * </p>
 */
public class ForestTest {
    private Forest forest;

    @BeforeEach
    void setUp()
    {
        forest = new Forest(10, 10, 1, 1);
    }


    @Test
    void constructorTest()
    {
        for (int i=0; i<10; i++)
        {
            for (int j=0; j<10; j++) {
                assertEquals(State.SUSPECTED, forest.getCell(i, j).getState());
            }
        }

        assertThrows(IllegalArgumentException.class, ()->new Forest(10, 10, 0, -1));
        assertThrows(IllegalArgumentException.class, ()->new Forest(10, 10, -1, 0));

        assertThrows(NegativeArraySizeException.class, ()-> new Forest(10, -1, 1, 1));
        assertThrows(NegativeArraySizeException.class, ()-> new Forest(-10, 1, 1, 1));
    }


    @Test
    void shouldLoadForestFromFile()  {
        File file = new File("test.csv");
        try {
            Files.writeString(file.toPath(),
                    "s,d,b\n" +
                            "d,s,b\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Forest forest = new Forest("test.csv", 0.5, 0.3);

        assertEquals(3, forest.getWidth());
        assertEquals(2, forest.getHeight());

        assertEquals(State.SUSPECTED,
                forest.getCell(0, 0).getState());

        assertEquals(State.DEAD,
                forest.getCell(1, 0).getState());

        assertEquals(State.BURNING,
                forest.getCell(2, 0).getState());

        assertEquals(State.DEAD,
                forest.getCell(0, 1).getState());

        assertEquals(State.SUSPECTED,
                forest.getCell(1, 1).getState());

        assertEquals(State.BURNING,
                forest.getCell(2, 1).getState());
    }

    @Test
    void shouldThrowIfNullGiven()
    {
        assertThrows(NullPointerException.class, () -> new Forest(null, 0, 0));
    }

    @Test
    void constructorShouldThrowWithWrongArguments()
    {
        assertThrows(IllegalArgumentException.class, () -> new Forest("test.csv", -1, 0));
    }

    @Test
    void constructorTestWithWrongFile()
    {
        File file = new File("test.csv");
        try {
            Files.writeString(file.toPath(),
                    "s,a,b\n" +
                            "d,s,b\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertThrows(IllegalArgumentException.class, () -> new Forest("test.csv", 1, 1));

        File file2 = new File("test2.csv");
        try {
            Files.writeString(file2.toPath(),
                    "s,a\n" +
                            "d,s,b\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertThrows(IllegalArgumentException.class, () -> new Forest("test2.csv", 1, 1));

    }

    @Test
    void updateCounterTest()
    {
        int s = forest.getSuspectedCounter();
        int d = forest.getDeadCounter();
        int b = forest.getBurningCounter();

        forest.updateCounter(State.SUSPECTED, State.BURNING);

        assertEquals(s-1, forest.getSuspectedCounter());
        assertEquals(d, forest.getDeadCounter());
        assertEquals(b+1, forest.getBurningCounter());
    }


    @Test
    void getWidthTest()
    {
        assertEquals(10, forest.getWidth());
    }


    @Test
    void getHeightTest()
    {
        assertEquals(10, forest.getHeight());
    }


    @Test
    void getDeadCounterTest()
    {
        int D=0;
        for (int i = 0; i<forest.getHeight(); i++)
        {
            for (int j=0; j< forest.getWidth(); j++)
            {
                if(forest.getCell(j, i).getState() == State.DEAD)
                {
                    D++;
                }
            }
        }
        assertEquals(D, forest.getDeadCounter());
    }


    @Test
    void getBurningCounterTest()
    {
        int B=0;
        for (int i = 0; i<forest.getHeight(); i++)
        {
            for (int j=0; j< forest.getWidth(); j++)
            {
                if(forest.getCell(j, i).getState() == State.BURNING)
                {
                    B++;
                }
            }
        }
        assertEquals(B, forest.getBurningCounter());
    }


    @Test
    void getSuspecetedCounterTest()
    {
        int S=0;
        for (int i = 0; i<forest.getHeight(); i++)
        {
            for (int j=0; j< forest.getWidth(); j++)
            {
                if(forest.getCell(j, i).getState() == State.SUSPECTED)
                {
                    S++;
                }
            }
        }
        assertEquals(S, forest.getSuspectedCounter());
    }


    @Test
    void setCellTest() {
        forest.setCell(5, 5, State.BURNING);

        assertEquals(State.BURNING,forest.getCell(5,5).getState());

        assertThrows(NullPointerException.class, ()-> forest.setCell(5, 5, null));

        assertThrows(IndexOutOfBoundsException.class, ()->forest.setCell(20, 10, State.BURNING));
        assertThrows(IndexOutOfBoundsException.class, ()->forest.setCell(-10, 5, State.BURNING));
    }

    @Test
    void constructorShouldThrowExceptionWhenAlfaOrBetaIsGreaterThanOne() {
        assertThrows(IllegalArgumentException.class, () -> new Forest(10, 10, 1.1, 0.4));
        assertThrows(IllegalArgumentException.class, () -> new Forest(10, 10, 0.5, 1.1));
    }


    @Test
    void constructorShouldCreateCorrectNumberOfCells() {
        assertEquals(10, forest.getWidth());
        assertEquals(10, forest.getHeight());
        assertEquals(100, forest.getSuspectedCounter());
        assertEquals(0, forest.getBurningCounter());
        assertEquals(0, forest.getDeadCounter());
    }


    @Test
    void getCellShouldThrowExceptionForWrongArguments() {
        assertThrows(IndexOutOfBoundsException.class, () -> forest.getCell(10, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> forest.getCell(0, 10));
        assertThrows(IndexOutOfBoundsException.class, () -> forest.getCell(-1, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> forest.getCell(0, -1));
    }


    @Test
    void getForestShouldReturnArrayWithCorrectSize() {
        Cell[][] cells = forest.getForest();

        assertNotNull(cells);
        assertEquals(10, cells.length);
        assertEquals(10, cells[0].length);
    }


    @Test
    void setCellShouldUpdateCounters() {
        forest.setCell(5, 5, State.BURNING);

        assertEquals(99, forest.getSuspectedCounter());
        assertEquals(1, forest.getBurningCounter());
        assertEquals(0, forest.getDeadCounter());

        forest.setCell(5, 5, State.DEAD);

        assertEquals(99, forest.getSuspectedCounter());
        assertEquals(0, forest.getBurningCounter());
        assertEquals(1, forest.getDeadCounter());
    }

    @Test
    void getRandomCellShouldReturnNotNullCell() {
        Cell cell = forest.getRandomCell();

        assertNotNull(cell);
    }

    @Test
    void countersShouldNotChangeIfGivenSameTypes()
    {
        int s = forest.getSuspectedCounter();
        int b = forest.getBurningCounter();
        int d = forest.getDeadCounter();

        forest.updateCounter(State.BURNING, State.BURNING);

        assertEquals(s, forest.getSuspectedCounter());
        assertEquals(b, forest.getBurningCounter());
        assertEquals(d, forest.getDeadCounter());
    }

    @Test
    void countersShouldMatchRealStatesInArray() {
        forest.setCell(0, 0, State.BURNING);
        forest.setCell(1, 0, State.DEAD);
        forest.setCell(2, 0, State.BURNING);

        int suspected = 0;
        int burning = 0;
        int dead = 0;

        for (int x = 0; x < forest.getWidth(); x++) {
            for (int y = 0; y < forest.getHeight(); y++) {
                if (forest.getCell(x, y).getState() == State.SUSPECTED) {
                    suspected++;
                } else if (forest.getCell(x, y).getState() == State.BURNING) {
                    burning++;
                } else if (forest.getCell(x, y).getState() == State.DEAD) {
                    dead++;
                }
            }
        }

        assertEquals(suspected, forest.getSuspectedCounter());
        assertEquals(burning, forest.getBurningCounter());
        assertEquals(dead, forest.getDeadCounter());
    }

    @Test
    void setCellToSameStateShouldNotChangeCounters() {
        int suspected = forest.getSuspectedCounter();
        int burning = forest.getBurningCounter();
        int dead = forest.getDeadCounter();

        forest.setCell(0, 0, State.SUSPECTED);

        assertEquals(suspected, forest.getSuspectedCounter());
        assertEquals(burning, forest.getBurningCounter());
        assertEquals(dead, forest.getDeadCounter());
    }

    @Test
    void constructorShouldThrowWhenCsvFileIsEmpty() throws IOException {
        File file = new File("empty.csv");
        Files.writeString(file.toPath(), "");

        assertThrows(RuntimeException.class,
                () -> new Forest("empty.csv", 0.5, 0.5));
    }
}
