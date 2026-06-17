package pl.forestfire.application.controller;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import pl.forestfire.application.view.JavaFxRenderer;
import pl.forestfire.application.view.StepsFilePrinter;
import pl.forestfire.model.Forest;
import pl.forestfire.simulation.Simulation;

/**
 * Klasa odpowiadająca komunikację interfejsu JavaFx z logiką symulacji.
 */
public class JavaFxController {

    /**
     * Logika symulacji.
     */
    private final Simulation simulation;

    /**
     * Interfejs aplikacji.
     */
    private final JavaFxRenderer view;

    /**
     * Opóźnienie symulacji.
     */
    private long delay;

    /**
     * Linia czasowa reprezentująca akcję podjętą w kolejnych krokach działąnia aplikacji w czasie.
     */
    private Timeline step;

    /**
     * Konstruktor tworzący klasę, otrzymujący odpoweinie klasy logiki i interfejsu, którymi ma zarządzać
     * @param simulation  klasa zarządzająca logiką
     * @param view klasa zarządzająca interfejsem
     * @param delay opóźnienie symulacji
     */
    public JavaFxController(Simulation simulation, JavaFxRenderer view, long delay){
        this.simulation=simulation;
        this.view=view;
        this.delay= delay ;

        step=new Timeline(new KeyFrame(Duration.millis(delay), e -> {timelineStep();}));
        step.setCycleCount(Timeline.INDEFINITE);

        attachEvents();
    }

    /**
     * Definiuje działanie przycisków do rozpoczęcia, przyśpieszenia i zwolnienia symulacji oraz zamknięcia aplikacji..
     */
    private void attachEvents(){
        view.getStart().setOnAction(e -> step.play());

        view.getExit().setOnAction(e -> System.exit(0));

        view.getFaster().setOnAction(e -> {faster(); step.play();});

        view.getSlower().setOnAction(e -> {slower(); step.play();});
    }

    /**
     * Wywołuje oraz przekazuje informację z kroku symulacji do interfejsu, w celu jej wypiania.
     */
    private void timelineStep(){
        Forest forest=simulation.getForest();
        StepsFilePrinter.saveStep(forest);
        if(forest.getBurningCounter()==0){
            step.stop();
            view.getFaster().setOnAction(e -> {});
            view.getSlower().setOnAction(e -> {});
            view.getStart().setOnAction(e -> {});
            return;
        }

        for(int[] tab:simulation.step()){
            view.drawCell(forest, tab[0], tab[1]);
        }
        view.showStatistics(forest, simulation.getWindSpeed(), simulation.getWindAngle(), delay);
    }

    /**
     * Zwiększa opóźnienie symulacji.
     */
    private void slower(){
        step.stop();
        delay*=2;
        step=new Timeline(new KeyFrame(Duration.millis(delay), e -> {timelineStep();}));
        step.setCycleCount(Timeline.INDEFINITE);
    }

    /**
     * Zmniejsza opóźnienie symulacji.
     */
    private void faster(){
        step.stop();
        if(delay>=4)
            delay=delay/2;
        step=new Timeline(new KeyFrame(Duration.millis(delay), e -> {timelineStep();}));
        step.setCycleCount(Timeline.INDEFINITE);
    }
}
