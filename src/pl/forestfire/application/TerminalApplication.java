package pl.forestfire.application;

import pl.forestfire.forestwriter.ForestWriter;
import pl.forestfire.model.Forest;
import pl.forestfire.model.State;
import pl.forestfire.simulation.Simulation;
import pl.forestfire.simulation.SimulationConfig;
import pl.forestfire.application.view.ConsoleRenderer;
import pl.forestfire.application.view.StepsFilePrinter;

public class TerminalApplication {
    public static void startApp() throws InterruptedException {
        SimulationConfig config;
        try {
            config = new SimulationConfig("../config.properties");
        } catch (Exception e) {
            config = new SimulationConfig("config.properties");
        }

        Forest forest = new Forest(config.width, config.height, config.alfa, config.beta);
        Simulation simulation = new Simulation(forest, config);
        ConsoleRenderer view = ConsoleRenderer.getConsoleRenderer();


        forest.getRandomCell().setState(State.BURNING);
        forest.updateCounter(State.SUSPECTED, State.BURNING);
        view.drawForest(forest);

        while (forest.getBurningCounter() > 0) {
            StepsFilePrinter.saveStep(forest);
            for (int[] tab:simulation.step()) {
                view.drawCell(forest, tab[0], tab[1]);
            }

            view.showStatistics(forest, simulation.getWindSpeed(), simulation.getWindAngle(), config.delay);

            Thread.sleep(config.delay);
        }
        StepsFilePrinter.saveStep(forest);
    }
}
