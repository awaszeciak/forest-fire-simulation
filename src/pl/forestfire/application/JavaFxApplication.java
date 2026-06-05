package pl.forestfire.application;

import pl.forestfire.application.controller.JavaFxController;
import pl.forestfire.application.view.JavaFxRenderer;
import pl.forestfire.model.Forest;
import pl.forestfire.model.State;
import pl.forestfire.simulation.Simulation;
import pl.forestfire.simulation.SimulationConfig;

import javafx.application.Application;
import javafx.stage.Stage;

public class JavaFxApplication extends Application {

    @Override
    public void start(Stage stage) {
        SimulationConfig config;
        try {
            config = new SimulationConfig("../config.properties");
        } catch (Exception e) {
            config = new SimulationConfig("config.properties");
        }
        Forest forest = new Forest(config.width, config.height, config.alfa, config.beta);
        forest.getRandomCell().setState(State.BURNING);
        forest.updateCounter(State.SUSPECTED, State.BURNING);

        Simulation simulation = new Simulation(forest, config);
        JavaFxRenderer view = new JavaFxRenderer(stage, forest, simulation, config.delay);
        new JavaFxController(simulation, view, config.delay);
    }

    public static void startApp(String[] args){
        launch(args);
    }
}
