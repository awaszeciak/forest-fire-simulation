package pl.forestfire.application.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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


/**
 * Abstrakcyjna klasa odpowiedzialna za definiowanie graficznego wyświetlania symulacji pożaru lasu
 * z wykorzystaniem JavaFX.
 *
 * <p>
 *     Renderer tworzy okno aplikacji, planszę lasu, panel statystyk,
 *     legendę oraz przyciski sterujące symulacją. Odpowiada także za
 *     odświeżanie pojedynczych komórek oraz aktualizację statystyk
 *     widocznych w interfejsie graficznym.
 * </p>
 */
public abstract class JavaFxRenderer implements Renderer{

    /**
     * maksymalna szerokość lasu w pikselach
     */
    final double maxGridWidth = 700;

    /**
     * maksymalna wysokość lasu w pikselach
     */
    final double maxGridHeight = 430;

    /**
     * Przycisk uruchamiający symulację.
     */
    protected Button start;

    /**
     * Przycisk zamykający aplikację.
     */
    protected Button exit;

    /**
     * Przycisk zwiększający szybkość symulacji.
     */
    protected Button faster;

    /**
     * Przycisk zmniejszający szybkość symulacji.
     */
    protected Button slower;

    /**
     * Etykieta wyświetlająca aktualne statystyki symulacji.
     */
    protected Label statistics;


    /**
     * Tworzy graficzny widok symulacji.
     *
     * <p>
     *     Konstruktor przygotowuje przyciski, planszę lasu, panel statystyk,
     *     legendę oraz główną scenę aplikacji JavaFX.
     * </p>
     * @param stage główne okno aplikacji JavaFX
     * @param forest las, który ma zostać wyświetlony
     * @param simulation obiekt symulacji, z którego pobierane są informacje o wietrze
     * @param delay opóźnienie między kolejnymi krokami symulacji
     */
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

        StackPane forestCard = new StackPane(getNode());
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
        HBox.setHgrow(forestCard, Priority.ALWAYS);

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

    /**
     * Zwraca iterpretację wyświetlanego lasu przez daną klasę potomną
     *
     * @return zwraca Node do stworzonej graficznej interpretacji lasu
     */
    protected abstract Node getNode();

    /**
     * Tworzy panel statystyk widoczny po prawej stronie okna.
     *
     * <p>
     *     Panel zawiera tytuł, aktualne statystyki symulacji oraz legendę
     *     kolorów używanych do oznaczania stanów komórek.
     * </p>
     *
     * @return gotowy panel statystyk
     */
    protected VBox createStatisticsCard() {
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

    /**
     * Tworzy pojedynczy wiersz legendy.
     *
     * <p>
     *     Wiersz składa się z kolorowego kwadratu oraz opisu znaczenia danego koloru.
     * </p>
     * @param color kolor kwadratu w legendzie
     * @param text opis znaczenia koloru
     * @return wiersz legendy
     */

    protected HBox createLegendRow(String color, String text) {
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

    /**
     * Ustawia style wszystkich przycisków widocznych w interfejsie.
     */
    protected void styleButtons() {
        styleButton(start, "#22c55e", "#01152b", "#16a34a" );
        styleButton(slower, "#dbeafe", "#01152b", "#93c5fd" );
        styleButton(faster, "#fb923c", "#01152b", "#ea580c" );
        styleButton(exit, "#ef4444", "#01152b", "#dc2626" );
    }

    /**
     * Ustawia styl pojedynczego przycisku.
     * @param button przycisk, którego styl ma zostać ustawiony
     * @param backgroundColor kolor tła przycisku
     * @param textColour kolor tekstu przycisku
     * @param borderColor kolor obramowania przycisku
     */
    protected void styleButton(Button button, String backgroundColor, String textColour, String borderColor) {
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


    /**
     * Rysuje cały las w interfejsie graficznym.
     *
     * <p>
     *     Metoda mająca tworzyć obiekt garficznej interpretacji lasu oraz wyświetlla go na ekranie
     * </p>
     * @param forest las, który ma zostać narysowany
     */
    @Override
    public abstract void drawForest(Forest forest);

    /**
     * Odświeża pojedynczą komórkę lasu w interfejsie graficznym.
     * <p>
     *     Kolor prostokąta zostaje ustawiony zgodnie z aktualnym stanem
     *     odpowiadającej mu komórki.
     * </p>
     *
     * @param forest las z którego pochodzi komórka.
     * @param x położenie komórki na osi X.
     * @param y położenie komórki na osi Y.
     */
    @Override
    public abstract void drawCell(Forest forest, int x, int y);


    /**
     * Aktualizuje tekst statystyk widocznych w interfejsie graficznym.
     *
     * <p>
     *     Wyświetlana jest liczba zdrowych, płonących i spalonych drzew,
     *     a także prędkość wiatru, kierunek wiatru, kąt wiatru oraz aktualnie
     *     opóźnienie symulacji.
     * </p>
     * @param forest las, którego statystyki mają zostać wyświetlone
     * @param speed prędkość wiatru
     * @param angle kąt kierunku wiatru względem wschodu
     * @param delay opóżnienie między krokami symulacji
     */
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

    /**
     * Zwraca przycisk uruchamiania symulacji.
     * @return przycisk Start
     */
    public Button getStart(){
        return start;
    }

    /**
     * Zwraca przycisk zamykania aplikacji.
     * @return przycisk Exit
     */
    public Button getExit() {
        return exit;
    }

    /**
     * Zwraca przycisk zwiększania szybkości symulacji.
     * @return przycisk Faster
     */
    public Button getFaster() {
        return faster;
    }

    /**
     * Zwraca przycisk zmniejszania szybkości symulacji.
     * @return przycisk Slower
     */
    public Button getSlower() {
        return slower;
    }
}
