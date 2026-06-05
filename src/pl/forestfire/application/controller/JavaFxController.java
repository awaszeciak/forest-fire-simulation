package pl.forestfire.application.controller;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import pl.forestfire.application.view.JavaFxRenderer;
import pl.forestfire.application.view.StepsFilePrinter;
import pl.forestfire.model.Forest;
import pl.forestfire.simulation.Simulation;

public class JavaFxController {
    private final Simulation simulation;
    private final JavaFxRenderer view;
    private long delay;
    private Timeline step;

    public JavaFxController(Simulation simulation, JavaFxRenderer view, long delay){
        this.simulation=simulation;
        this.view=view;
        this.delay= delay ;

        step=new Timeline(new KeyFrame(Duration.millis(delay), e -> {timelineStep();}));
        step.setCycleCount(Timeline.INDEFINITE);

        attachEvents();
    }

    private void attachEvents(){
        view.getStart().setOnAction(e -> step.play());

        view.getExit().setOnAction(e -> System.exit(0));

        view.getFaster().setOnAction(e -> {faster(); step.play();});

        view.getSlower().setOnAction(e -> {slower(); step.play();});
    }

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

    private void slower(){
        step.stop();
        delay*=2;
        step=new Timeline(new KeyFrame(Duration.millis(delay), e -> {timelineStep();}));
        step.setCycleCount(Timeline.INDEFINITE);
    }

    private void faster(){
        step.stop();
        if(delay>=4)
            delay=delay/2;
        step=new Timeline(new KeyFrame(Duration.millis(delay), e -> {timelineStep();}));
        step.setCycleCount(Timeline.INDEFINITE);
    }
}
