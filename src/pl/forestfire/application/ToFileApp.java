package pl.forestfire.application;

import pl.forestfire.application.view.StepsFilePrinter;
import pl.forestfire.forestwriter.ForestWriter;
import pl.forestfire.model.Forest;
import pl.forestfire.model.State;
import pl.forestfire.simulation.Simulation;
import pl.forestfire.simulation.SimulationConfig;

/**
 * Klasa odpowiadająca za symulację z zapisem do pliku csv, oraz zapisem kroków
 * bez wyświetlania.
 *
 * <p>
 *     Symulacja wykonywana jest krok po kroku, a kolejne stany są zapisywane do pliku.
 *     Po zakończeniu działania programu końcowy stan zostaje zapisywany do pliku CSV.
 * </p>
 */
public class ToFileApp {

    /**
     * Rozpoczęcie symulacji pożaru lasu z zapisem wyników do pliku.
     *
     * <p>
     *     Metoda wczytuje konfigurację z pliku {@code config.properties}, tworzy las
     *     oraz obiekt symulacji. Następnie losowo wybiera jedną komórkę początkową,
     *     ustawia ją jako płonącą i wykonuje kolejne kroki symulacji, aż do momentu,
     *     gdy w lesie nie pozostanie żadna płonąca komórka.
     * </p>
     * <p>
     *     Każdy krok symulacji jest zapisywany za pomocą klasy
     *     {@link StepsFilePrinter}, a po zakończeniu końcowy stan lasu zostaje zapisany
     *     do pliku CSV za pomocą klasy {@link ForestWriter}.
     * </p>
     *
     * @param name nazwa pliku do którego mają zostać zapisane dane
     * @throws InterruptedException wyjątek związany z możliwością przerwania działania programu
     */
    public static void startApp(String name) throws InterruptedException {
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
        ForestWriter.WriteToCsv(name, forest);
    }
}
