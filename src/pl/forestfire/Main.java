package pl.forestfire;

import pl.forestfire.model.Forest;
import pl.forestfire.model.State;
import pl.forestfire.simulation.Simulation;
import pl.forestfire.simulation.SimulationConfig;
import pl.forestfire.view.ConsoleRenderer;
import pl.forestfire.view.StepsFilePrinter;
import pl.forestfire.forestwriter.ForestWriter;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        SimulationConfig config = new SimulationConfig("config.properties");

        Forest forest = new Forest(config.width, config.height, config.alfa, config.beta);
        Simulation simulation = new Simulation(forest, config);
        ConsoleRenderer view = ConsoleRenderer.getConsoleRenderer();


        forest.getRandomCell().setState(State.BURNING);
        forest.updateCounter(State.SUSPECTED, State.BURNING);

        while (forest.getBurningCounter() > 0) {
            simulation.step();
            view.drawForest(forest);
            view.showStatistics(forest, simulation.getWindSpeed(), simulation.getWindAngle());
            StepsFilePrinter.saveStep(forest);

            Thread.sleep(config.delay);
        }
        StepsFilePrinter.close();
        ForestWriter.WriteToCsv("file.csv", forest);

        forest = new Forest("file.csv", config.alfa, config.beta);
        ForestWriter.WriteToCsv("file2.csv", forest);

    }
}
