package pl.forestfire.application.view;

import java.io.FileWriter;
import java.io.IOException;
import pl.forestfire.model.Forest;

public class StepsFilePrinter {

    static private int step=0;
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

    public static void saveStep(Forest forest) {
        try {
            file.write(step+","+forest.getSuspectedCounter()+","+forest.getBurningCounter()+","+forest.getDeadCounter()+"\n");
            ++step;
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void close() {
        try {
            file.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
