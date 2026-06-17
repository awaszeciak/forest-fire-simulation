package pl.forestfire.simulation;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Klasa odpowiedzialna za odczyt konfiguracji symulacji z pliku.
 * <p>
 *     Plik konfiguracyjny powinien zawierać parametry opisujące rozmiar lasu,
 *     opóźnienie między krokami symulacji, parametry wiatru oraz wartości
 *     współczynników alfa i beta.
 * </p>
 */

public class SimulationConfig {

    /**
     * Szerokość lasu, czyli liczba komórek w poziomie.
     */
    public final int width;

    /**
     * Wysokość lasu, czyli liczba komórek w pionie.
     */
    public final int height;

    /**
     * Opóźnienie między kolejnymi krokami symulacji podane w milisekundach.
     */
    public final int delay;

    /**
     * Kierunek wiatru podany w stopniach względem wschodu.
     */
    public double windAngle;

    /**
     * Prędkość wiatru podana w metrach na sekundę.
     */
    public double windSpeed;

    /**
     * Prawdopodobieństwo wypalenia się płonącej komórki.
     */
    public final double alfa;

    /**
     * Bazowe prawdopodobieństwo zapłonu komórki sąsiadującej z ogniem.
     */
    public final double beta;

    /**
     * Tworzy konfigurację symulacji na podstawie danych odczytanych z pliku.
     * <p>
     * W pliku powinny znajdować się właściwości:
     * {@code width}, {@code height}, {@code delay}, {@code windAngle},
     * {@code windSpeed}, {@code alfa}, {@code beta}.
     * </p>
     * @param fileName nazwa lub ścieżka do pliku konfiguracyjnego
     * @throws RuntimeException jeśli plik konfiguracyjny nie może zostać odczytany
     */
    public SimulationConfig(String fileName) {

        if (fileName == null) {
            throw new NullPointerException("Config file path cannot be null");
        }

        Properties properties = new Properties();

        try {
            properties.load(new FileReader(fileName));

            width = Integer.parseInt(properties.getProperty("width"));
            height = Integer.parseInt(properties.getProperty("height"));
            delay = Integer.parseInt(properties.getProperty("delay"));

            windAngle = Double.parseDouble(properties.getProperty("windAngle"));
            windSpeed = Double.parseDouble(properties.getProperty("windSpeed"));

            alfa = Double.parseDouble(properties.getProperty("alfa"));
            beta = Double.parseDouble(properties.getProperty("beta"));

            validate();

        } catch (IOException e) {
            throw new RuntimeException("Nie udalo sie wczytac pliku konfiguracyjnego");
        }

    }
    private void validate() {
        if (width <= 0) {
            throw new IllegalArgumentException("Width must be positive");
        }
        if (height <= 0) {
            throw new IllegalArgumentException("Height must be positive");
        }
        if (delay < 0) {
            throw new IllegalArgumentException("Delay cannot be nagative");
        }
        if (alfa < 0 || alfa > 1) {
            throw new IllegalArgumentException("Alfa must be between 0 and 1");
        }
        if (beta < 0 || beta > 1) {
            throw new IllegalArgumentException("Beta must be between 0 and 1");
        }
        if (windSpeed < 0) {
            throw new IllegalArgumentException("Wind speed cannot be negative");
        }
        if (windAngle < 0 || windAngle >= 360) {
            throw new IllegalArgumentException("Wind angle must be in range [0, 360)");
        }
    }
}
