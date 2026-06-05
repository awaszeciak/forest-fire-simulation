package pl.forestfire.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ForestTest {
    private Forest forest;

    @BeforeEach
    void setUp()
    {
        forest = new Forest(10, 10, 1, 1);
    }
    @Test
    void ConstructorTest()
    {
        for (int i=0; i<10; i++)
        {
            for (int j=0; j<10; j++)
            {
                assertEquals(State.SUSPECTED, forest.getCell(i, j).getState());
            }
        }

        assertThrows(IllegalArgumentException.class, ()->new Forest(10, 10, 0, -1));
        assertThrows(IllegalArgumentException.class, ()->new Forest(10, 10, -1, 0));

        assertThrows(IndexOutOfBoundsException.class, ()-> new Forest(10, -1, 1, 1));
        assertThrows(IndexOutOfBoundsException.class, ()-> new Forest(-10, 1, 1, 1));
    }

    @Test
    void ConstructorFromFileTest()
    {
        assertThrows(NullPointerException.class, ()->new Forest(null, 0, 0));

        //Forest forestFromFile = new Forest("../test/pl/forestfire/model", 1, 1);
    }

    @Test
    void upadeteCounterTest()
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
}
