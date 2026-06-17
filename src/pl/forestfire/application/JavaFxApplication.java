package pl.forestfire.application;


import pl.forestfire.application.controller.JavaFxController;
import pl.forestfire.application.view.JavaFxRenderer;
import pl.forestfire.application.view.JavaFxRendererCanvas;
import pl.forestfire.application.view.JavaFxRendererGridPane;
import pl.forestfire.application.view.JavaFxRendererPixelWriter;
import pl.forestfire.model.Forest;
import pl.forestfire.model.State;
import pl.forestfire.simulation.Simulation;
import pl.forestfire.simulation.SimulationConfig;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Klasa odpowiadająca za uruchomienie aplikacji, wykorzystująca JavaFX.
 *
 * <p>
 *     Wczytuje konfigurację symulacji, tworzy las oraz obiekt symulacji,
 *     a następnie inicjalizuje widok i kontroler interfejsu graficznego.
 * </p>
 *
 * <p>
 *     Jeżeli rozmiar lasu przekracza dopuszczalny limit dla trybu GUI,
 *     program wypisuje komunikat w konsoli i kończy działanie.
 * </p>
 */
public class JavaFxApplication extends Application {

    /**
     * Uruchamianie aplikacji JavaFX.
     *
     * <p>
     *     Tworzy podstawowe elementy symulacji: konfigurację, las, symulację,
     *     widok oraz kontroler. Losowo wybrana komórka zostaje ujawniona jako
     *     początkowo płonąca.
     * </p>
     *
     * @param stage główne okno aplikacji JavaFX
     */
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

        JavaFxRenderer view;
        if(forest.getHeight()>285||forest.getWidth()>285)
            view = new JavaFxRendererPixelWriter(stage, forest, simulation, config.delay);
        else
            view = new JavaFxRendererCanvas(stage, forest, simulation, config.delay);
        new JavaFxController(simulation, view, config.delay);
    }


    /**
     * Uruchamia aplikację JavaFX.
     *
     * <p>
     *     Metoda wywołuje mechanizm {@link #launch(String...)} klasy
     *     {@link Application} i jest używana po wybraniu trybu GUI.
     * </p>
     * @param args argumenty przekazane przy uruchomieniu programu
     */
    public static void startApp(String[] args){
        launch(args);
    }
}
