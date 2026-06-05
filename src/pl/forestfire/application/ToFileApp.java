package pl.forestfire.application;

import pl.forestfire.application.view.ConsoleRenderer;
import pl.forestfire.application.view.StepsFilePrinter;
import pl.forestfire.forestwriter.ForestWriter;
import pl.forestfire.model.Forest;
import pl.forestfire.model.State;
import pl.forestfire.simulation.Simulation;
import pl.forestfire.simulation.SimulationConfig;

public class ToFileApp {
    public static void startApp() throws InterruptedException {
        SimulationConfig config;
        try {
            config = new SimulationConfig("../config.properties");
        } catch (Exception e) {
            config = new SimulationConfig("config.properties");
        }

        Forest forest = new Forest(config.width, config.height, config.alfa, config.beta);
        Simulation simulation = new Simulation(forest, config);

        forest.getRandomCell().setState(State.BURNING);
        forest.updateCounter(State.SUSPECTED, State.BURNING);

        while (forest.getBurningCounter() > 0) {
            StepsFilePrinter.saveStep(forest);

            simulation.step();
        }
        StepsFilePrinter.saveStep(forest);
        ForestWriter.WriteToCsv("file.csv", forest);
    }
}
