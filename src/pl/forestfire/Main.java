package pl.forestfire;

import pl.forestfire.model.Forest;
import pl.forestfire.model.State;
import pl.forestfire.simulation.Simulation;
import pl.forestfire.view.ConsoleRenderer;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Forest forest = new Forest(20, 10);
        Simulation simulation = new Simulation(forest);
        ConsoleRenderer view = new ConsoleRenderer();


        forest.getRandomCell().setState(State.BURNING);

        while(true) {
            simulation.step();
            view.drawForest(forest);

            Thread.sleep(200);
        }
    }
}
