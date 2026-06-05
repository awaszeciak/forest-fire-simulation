package pl.forestfire.simulation;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

// klasa odpowiadajaca za odczyt danych z pliku
public class SimulationConfig {
    public final int width;
    public final int height;
    public final int delay;
    public final double windAngle;
    public final double windSpeed;
    public final double alfa;
    public final double beta;

    public SimulationConfig(String fileName) {
        Properties properties = new Properties();

        try {
            properties.load(new FileReader(fileName));
        } catch (IOException e) {
            throw new RuntimeException("Nie udalo sie wczytab pliku konfiguracyjnego");
        }

        width = Integer.parseInt(properties.getProperty("width"));
        height = Integer.parseInt(properties.getProperty("height"));
        delay = Integer.parseInt(properties.getProperty("delay"));

        windAngle = Double.parseDouble(properties.getProperty("windAngle"));
        windSpeed = Double.parseDouble(properties.getProperty("windSpeed"));

        alfa = Double.parseDouble(properties.getProperty("alfa"));
        beta = Double.parseDouble(properties.getProperty("beta"));
    }
}
