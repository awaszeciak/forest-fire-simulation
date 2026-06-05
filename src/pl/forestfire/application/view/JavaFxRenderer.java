package pl.forestfire.application.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import pl.forestfire.model.Forest;
import pl.forestfire.simulation.Simulation;

import java.util.Stack;

public class JavaFxRenderer implements Renderer{
    private GridPane forestGrid;
    private Rectangle[][] forestContainer;

    private Button start;
    private Button exit;
    private Button faster;
    private Button slower;

    private Label statistics;


    public JavaFxRenderer(Stage stage, Forest forest, Simulation simulation, long delay){
        start=new Button("▶ Start");
        exit=new Button("■ Wyjście");
        faster=new Button("+ Szybciej");
        slower=new Button("- Wolniej");
        statistics=new Label();
        drawForest(forest);

        Label title = new Label("Forestfire");
        title.setStyle("-fx-font-size: 32px;" + "-fx-font-weight: bold;" + "-fx-text-fill: #111827;");

        Label subtitle = new Label("Symulacja rozprzestrzeniania się pożaru w lesie");
        subtitle.setStyle("-fx-font-size: 16px;" + "-fx-font-fill: #6b7280;");

        HBox header = new HBox(25, title, subtitle);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(25, 35, 25, 35));
        header.setStyle("-fx-background-color: white;" + "-fx-border-color: transparent transparent #e5e7eb transparent;");

        StackPane forestCard = new StackPane(forestGrid);
        forestCard.setPadding(new Insets(15));
        forestCard.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 16;" +
                        "-fx-border-radius: 16;" +
                        "-fx-border-color: #e5e7eb"
        );

        VBox statsCard = createStatisticsCard();

        HBox content = new HBox(25, forestCard, statsCard);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(25));

        styleButtons();

        HBox buttons = new HBox(25, start, slower, faster, exit);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(20));
        buttons.setStyle("-fx-background-color: white;" +
                "-fx-border-color: #e5e7eb transparent transparent transparent;");

        BorderPane root = new BorderPane();
        root.setTop(header);
        root.setCenter(content);
        root.setBottom(buttons);
        root.setStyle("-fx-background-color: #f3f4f6");

        Scene scene = new Scene(root, 1100, 720);

        stage.setScene(scene);
        stage.setTitle("Forestfire");

        showStatistics(forest, simulation.getWindSpeed(), simulation.getWindAngle(), delay);


        stage.getIcons().add(new Image(getClass().getResourceAsStream("/trees.png")));
        stage.show();


    }

    private VBox createStatisticsCard() {
        Label statsTitle = new Label("Statystyki");
        statsTitle.setStyle("-fx-font-size: 22px;" + "-fx-font-weight: bold;" + "-fx-text-fill: #111827;");

        statistics.setStyle("-fx-font-size: 15px;" + "-fx-text-fill: #374151");

        Label legendTitle = new Label("Legenda");
        legendTitle.setStyle("-fx-font-size: 18px;" + "-fx-font-weight: bold;" + "-fx-text-fill: #111827;");

        VBox legend = new VBox(8,
                createLegendRow("green", "Zwykłe drzewa"),
                createLegendRow("red", "Płonące drzewa"),
                createLegendRow("black", "Spalone drzewa")
        );


        legend.setStyle("-fx-font-size: 15px;" + "-fx-text-fill: #374151;" + "-fx-line-spacing: 6;");

        VBox statsCard = new VBox(15, statsTitle, statistics, legendTitle, legend);
        statsCard.setPrefWidth(350);
        statsCard.setPadding(new Insets(25));
        statsCard.setStyle("-fx-background-color: white;" +
                "-fx-background-radius: 16;" +
                "-fx-border-radius: 16;" +
                "-fx-border-color: #e5e7eb;");
        return statsCard;
    }

    private HBox createLegendRow(String color, String text) {
        Rectangle square = new Rectangle(14,14);
        square.setFill(Paint.valueOf(color));
        square.setArcHeight(3);
        square.setArcWidth(3);

        Label label = new Label(text);
        label.setStyle("-fx-font-size: 15px;" + "-fx-text-fill: #374151;");

        HBox row = new HBox(10, square, label);
        row.setAlignment(Pos.CENTER_LEFT);

        return row;
    }

    private void styleButtons() {
        styleButton(start, "#22c55e", "#01152b", "#16a34a" );
        styleButton(slower, "#dbeafe", "#01152b", "#93c5fd" );
        styleButton(faster, "#fb923c", "#01152b", "#ea580c" );
        styleButton(exit, "#ef4444", "#01152b", "#dc2626" );
    }

    private void styleButton(Button button, String backgroundColor, String textColour, String borderColor) {
        button.setPrefWidth(160);
        button.setPrefHeight(45);

        button.setStyle(
                "-fx-background-color: " + backgroundColor + ";" +
                        "-fx-text-fill: " + textColour + ";" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-color: 10;" +
                        "-fx-border-radius: 10;" +
                        "-fx-border-color: " + borderColor + ";" +
                        "-fx-cursor: hand;"
        );
    }

    @Override
    public void drawForest(Forest forest) {
        forestGrid = new GridPane();
        forestGrid.setHgap(0);
        forestGrid.setVgap(0);
        forestGrid.setAlignment(Pos.CENTER);

        double maxGridWidth = 700;
        double maxGridHeight = 430;

        double cellWidth = maxGridWidth / forest.getWidth();
        double cellHeight = maxGridHeight / forest.getHeight();

        double cellSize = Math.min(cellWidth, cellHeight);

        forestContainer=new Rectangle[forest.getWidth()][forest.getHeight()];


        for (int x = 0; x < forest.getWidth(); x++)
            for (int y = 0; y < forest.getHeight(); y++) {
                forestContainer[x][y]=new Rectangle(cellSize + 0.3, cellSize + 0.3, Paint.valueOf(forest.getForest()[x][y].getState().getColor()));

//                forestContainer[x][y].setArcHeight(0);
//                forestContainer[x][y].setArcHeight(0);

                forestGrid.add(forestContainer[x][y], x, y);
            }
    }

    @Override
    public void drawCell(Forest forest, int x, int y) {
        forestContainer[x][y].setFill(Paint.valueOf(forest.getForest()[x][y].getState().getColor()));
    }

    @Override
    public void showStatistics(Forest forest, double speed, double angle, long delay) {
        statistics.setText("Zwykłe drzewa: "+forest.getSuspectedCounter()+"\n"+
                "Płonące drzewa: "+forest.getBurningCounter()+"\n"+
                "Spalone drzewa: "+forest.getDeadCounter()+"\n\n"+
                "Prędkość wiatru: "+speed+ "\n"+
                "Kierunek wiatru: "+directionString(angle)+"\n"+
                "Kąt wiatru względem wschodu: "+angle+"°\n"+
                "Opóźnienie: "+ delay + "ms"
        );
    }

    public Button getStart(){
        return start;
    }

    public Button getExit() {
        return exit;
    }

    public Button getFaster() {
        return faster;
    }

    public Button getSlower() {
        return slower;
    }
}
