package pl.forestfire.application.view;

import java.io.FileWriter;
import java.io.IOException;
import pl.forestfire.model.Forest;

/**
 * Klasa odpowiadająca za zapis ilości drzew różnych stanów do pliku steps.csv.
 */
public class StepsFilePrinter {

    /**
     * Licznik ilości kroków.
     */
    static private int step=0;

    /**
     * Plik, do którego zapisywane będą kroki symulacji.
     */
    static private FileWriter file;

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(StepsFilePrinter::close));
        try {
            file = new FileWriter("steps.csv");
            file.write("step,suspected,burning,dead\n");
        } 
        catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * Zapisuje statystyki lasu do pliku.
     * @param forest las, którego ilość drzew różnych stanów ma zostać zapisana.
     */
    public static void saveStep(Forest forest) {
        try {
            file.write(step+","+forest.getSuspectedCounter()+","+forest.getBurningCounter()+","+forest.getDeadCounter()+"\n");
            ++step;
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Zamyka plik steps.csv.
     */
    public static void close() {
        try {
            file.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
