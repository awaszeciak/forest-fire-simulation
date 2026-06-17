package pl.forestfire.simulation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.forestfire.model.Forest;
import pl.forestfire.model.State;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy jednostkowe klasy {@link Simulation}
 *
 * <p>
 *     Sprawdzają poprawność inicjalizacji symulacji oraz działanie pojedynczego
 *     kroku symulacji, w tym spalanie płonących komórek, zapalanie sąsiadów,
 *     obsługę komórek brzegowych oraz aktualizację liczników stanów lasu.
 * </p>
 */
public class SimulationTest {

    private Forest forest;
    private SimulationConfig config;
    private Simulation simulation;

    @BeforeEach
    void setUp() {
        forest = new Forest(5, 5, 1.0, 1.0);

        config = new SimulationConfig("config.properties");
        config.windAngle = 0.0;
        config.windSpeed = 0.0;

        simulation = new Simulation(forest, config);
    }


    @Test
    void constructorTest() {
        assertSame(forest, simulation.getForest());
        assertEquals(0.0, simulation.getWindAngle());
        assertEquals(0.0, simulation.getWindSpeed());
    }


    @Test
    void stepShouldReturnEmptyListWhenNoCellIsBurning() {
        List<int[]> changedCells = simulation.step();

        assertTrue(changedCells.isEmpty());
        assertEquals(25, forest.getSuspectedCounter());
        assertEquals(0, forest.getBurningCounter());
        assertEquals(0, forest.getDeadCounter());
    }


    @Test
    void stepShouldBurnBurningCellWhenAlfaIsOne() {
        forest.setCell(2, 2, State.BURNING);

        List<int[]> changedCells = simulation.step();

        assertEquals(State.DEAD, forest.getCell(2, 2).getState());
        assertFalse(changedCells.isEmpty());
        assertEquals(1, forest.getDeadCounter());
    }


    @Test
    void stepShouldIgniteNeighboursWhenBetaIsOne() {
        forest.setCell(2, 2, State.BURNING);

        simulation.step();

        assertEquals(State.BURNING, forest.getCell(1,1).getState());
        assertEquals(State.BURNING, forest.getCell(1,2).getState());
        assertEquals(State.BURNING, forest.getCell(1,3).getState());
        assertEquals(State.BURNING, forest.getCell(2,1).getState());
        assertEquals(State.BURNING, forest.getCell(2,3).getState());
        assertEquals(State.BURNING, forest.getCell(3,1).getState());
        assertEquals(State.BURNING, forest.getCell(3,2).getState());
        assertEquals(State.BURNING, forest.getCell(3,3).getState());

    }


    @Test
    void stepShouldNotIgniteNeighboursWhenBetaIsZero() {
        Forest forest = new Forest(5, 5, 1.0, 0.0);

        SimulationConfig config = new SimulationConfig("config.properties");
        config.windAngle = 0.0;
        config.windSpeed = 0.0;

        Simulation simulation = new Simulation(forest, config);

        forest.setCell(2, 2, State.BURNING);

        simulation.step();

        assertEquals(State.SUSPECTED, forest.getCell(1,1).getState());
        assertEquals(State.SUSPECTED, forest.getCell(1,2).getState());
        assertEquals(State.SUSPECTED, forest.getCell(1,3).getState());
        assertEquals(State.SUSPECTED, forest.getCell(2,1).getState());
        assertEquals(State.SUSPECTED, forest.getCell(2,3).getState());
        assertEquals(State.SUSPECTED, forest.getCell(3,1).getState());
        assertEquals(State.SUSPECTED, forest.getCell(3,2).getState());
        assertEquals(State.SUSPECTED, forest.getCell(3,3).getState());
    }


    @Test
    void stepShouldWorkForBurningCellInCorner() {
        forest.setCell(0, 0, State.BURNING);

        simulation.step();

        assertEquals(State.DEAD, forest.getCell(0,0).getState());

        assertEquals(State.BURNING, forest.getCell(1, 0).getState());
        assertEquals(State.BURNING, forest.getCell(0,1).getState());
        assertEquals(State.BURNING, forest.getCell(1,1).getState());

    }


    @Test
    void stepShouldReturnChangedCells() {
        forest.setCell(2,2, State.BURNING);

        List<int[]> changedCells = simulation.step();

        assertFalse(changedCells.isEmpty());
    }


    @Test
    void getForestShouldReturnForestFromConstructor() {
        assertSame(forest, simulation.getForest());
    }


    @Test
    void getWindAngleShouldReturnValueFromConfig() {
        assertEquals(0.0, simulation.getWindAngle());
    }

    @Test
    void getWindSpeedShouldReturnValueFromConfig() {
        assertEquals(0.0, simulation.getWindSpeed());
    }

    @Test
    void stepShouldChangeOnlyBurningCellWhenBetaIsZeroAndAlfaIsOne() {
        Forest localForest = new Forest(3, 3, 1.0, 0.0);

        SimulationConfig localConfig = new SimulationConfig("config.properties");
        localConfig.windAngle = 0.0;
        localConfig.windSpeed = 0.0;

        Simulation localSimulation = new Simulation(localForest, localConfig);

        localForest.setCell(1, 1, State.BURNING);

        List<int[]> changedCells = localSimulation.step();

        assertEquals(State.DEAD, localForest.getCell(1, 1).getState());

        assertEquals(State.SUSPECTED, localForest.getCell(0, 0).getState());
        assertEquals(State.SUSPECTED, localForest.getCell(0, 1).getState());
        assertEquals(State.SUSPECTED, localForest.getCell(0, 2).getState());
        assertEquals(State.SUSPECTED, localForest.getCell(1, 0).getState());
        assertEquals(State.SUSPECTED, localForest.getCell(1, 2).getState());
        assertEquals(State.SUSPECTED, localForest.getCell(2, 0).getState());
        assertEquals(State.SUSPECTED, localForest.getCell(2, 1).getState());
        assertEquals(State.SUSPECTED, localForest.getCell(2, 2).getState());

        assertEquals(1, changedCells.size());
        assertEquals(8, localForest.getSuspectedCounter());
        assertEquals(0, localForest.getBurningCounter());
        assertEquals(1, localForest.getDeadCounter());
    }

    @Test
    void newlyIgnitedCellsShouldNotBurnInTheSameStep() {
        Forest localForest = new Forest(3, 3, 1.0, 1.0);

        SimulationConfig localConfig = new SimulationConfig("config.properties");
        localConfig.windAngle = 0.0;
        localConfig.windSpeed = 0.0;

        Simulation localSimulation = new Simulation(localForest, localConfig);

        localForest.setCell(1, 1, State.BURNING);

        localSimulation.step();

        assertEquals(State.DEAD, localForest.getCell(1, 1).getState());

        assertEquals(State.BURNING, localForest.getCell(0, 0).getState());
        assertEquals(State.BURNING, localForest.getCell(0, 1).getState());
        assertEquals(State.BURNING, localForest.getCell(0, 2).getState());
        assertEquals(State.BURNING, localForest.getCell(1, 0).getState());
        assertEquals(State.BURNING, localForest.getCell(1, 2).getState());
        assertEquals(State.BURNING, localForest.getCell(2, 0).getState());
        assertEquals(State.BURNING, localForest.getCell(2, 1).getState());
        assertEquals(State.BURNING, localForest.getCell(2, 2).getState());
    }
}
