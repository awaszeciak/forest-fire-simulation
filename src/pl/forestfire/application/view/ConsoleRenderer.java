package pl.forestfire.application.view;
import pl.forestfire.model.*;

/**
 * Klasa zarządzająca wyświetlaniem symulacji w termianlu oraz zarządzaniem pozycją, kolorem i widocznością kursora.
 *
 * <p>
 *     Renderer zarządza rysowaniem lasu, pojedynczych komórek oraz statystyk symulacji.
 *     Dodatkowo obsługuje pozycję kursora, kolor tekstu oraz ukrywanie
 *     i przywracanie widoczności kursora w terminalu.
 * </p>
 */
public class ConsoleRenderer implements Renderer{

    /**
     * Prywatny konstruktor, który czyści ekran oraz ukrywa kursor w termialu.
     */
    private ConsoleRenderer() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::clean));//zapewnia, że przy zamknięciu programu wykona się clean()
        System.out.print("\033[H\033[2J");//czyszczenie terminala
        System.out.print("\033[?25l");//ukrycie kursora
        System.out.flush();
    }

    /**
     * Zwraca instancję klasy (Singleton).
     *
     * @return jedyna instancja klasy {@link ConsoleRenderer}
     */
    public static ConsoleRenderer getConsoleRenderer() {
        if(consolerenderer==null)
            consolerenderer=new ConsoleRenderer();
        return consolerenderer;
    }

    /**
     * Przenosi kursor do nowej linii.
     */
    public void newLine() {
        System.out.println();
        ++height;
        width=0;
    }

    /**
     * Przesuwa kursor na koordynaty (x,y).
     * @param x pozycja na osi X, na którą należy przenieść kursor.
     * @param y pozycja na osi Y, na którą należy przenieść kursor.
     */
    private void moveDrawer(int x, int y) {
        int width_difference=x-width;
        int height_difference=y-height;

        if(width_difference!=0) {
            if(width_difference>0)
                System.out.printf("\033[%dC", width_difference);//idzie w prawo
            else
                System.out.printf("\033[%dD", -width_difference);//idzie w lewo
        }

        if(height_difference!=0) {
            if(height_difference>0)
                System.out.printf("\033[%dB", height_difference);//idzie w dół
            else
                System.out.printf("\033[%dA", -height_difference);//idzie w górę
        }

        width=x;
        height=y;
    }

    /**
     * Zmienia kolor tekstu używanego do rysowania w terminalu.
     * @param new_color nazwa koloru, na jaki zmienić kursor (obsługuje green, red, black, white).
     */
    private void changeColor(String new_color) {
        if(!color.equals(new_color)) {
            color=new_color;
            switch(color) {
                case "green":
                    System.out.print("\033[32m");
                    break;
            
                case "red":
                    System.out.print("\033[31m");
                    break;

                case "black":
                    System.out.print("\033[90m");
                    break;

                case "white":
                    System.out.print("\033[0m");
                    break;
            }
        }
    }


    /**
     * Rysuje cały las w lewym górnym rogu terminala.
     *
     * @param forest las, który ma zostać narysowany
     */
    @Override
    public void drawForest(Forest forest) {
        moveDrawer(0, 0);
        for(int y=0; y<forest.getHeight(); y++) {
            for(int x=0; x<forest.getWidth(); x++) {
                drawCell(forest, x, y);
            }
            newLine();
        }
        changeColor("white");
    }

    /**
     * Wypisuje w terminalu daną komórkę.
     * @param forest las z którego pochodzi komórka.
     * @param x położenie komórki na osi X.
     * @param y położenie komórki na osi Y.
     *
     */
    @Override
    public void drawCell(Forest forest, int x, int y) {
        moveDrawer(x, y);
        changeColor(forest.getCell(x, y).getState().getColor());
        System.out.print("0");
        ++width;
    }

    /**
     * Przywraca widoczność kursora oraz kolor pisanego tekstu w terminalu przy zakończeniu programu.
     */
    public void clean() {
        System.out.flush();
        System.out.print("\033[u");//przywraca zapisaną pozycję kursora
        changeColor("white");
        System.out.print("\033[?25h");//przywraca kursor
        System.out.flush();
    }

    /**
     * Metoda odpowiadająca za wyświetlenie pod lasem statystyk symulacji.
     * @param forest las, którego statystyki mają zostać wyświetlone.
     * @param speed prędkość wiatru.
     * @param angle kąt kierunku wiatru względem wschodu
     * @param delay opóżnienie między krokami symulacji.
     *
     */
    @Override
    public void showStatistics(Forest forest, double speed, double angle, long delay) {
        moveDrawer(0, forest.getHeight());
        changeColor("white");
        System.out.printf("Zwykłe drzewa:  %-12d",forest.getSuspectedCounter());
        newLine();
        System.out.printf("Płonące drzewa: %-12d", forest.getBurningCounter());
        newLine();
        System.out.printf("Spalone drzewa: %-12d", forest.getDeadCounter());
        newLine();
        System.out.printf("Prędkość wiatru: %-12.2f", speed);
        newLine();
        System.out.print("Kierunek wiatru: "+directionString(angle)+" ");
        newLine();
        System.out.printf("Kąt wiatru względem wschodu: %-12.2f", angle);
        newLine();
        System.out.printf("Opóźnienie:  %-12d", delay);
        newLine();
        System.out.print("\033[s");//zapisuje pozycję kursora
    }

    /**
     * Obecna pozycja na osi X.
     */
    private int width=0;

    /**
     * Obecna pozycja na osi Y.
     */
    private int height=0;

    /**
     * Obecny kolor kursora.
     */
    private String color="white";

    /**
     * Instancja klasy (Singleton).
     */
    private static ConsoleRenderer consolerenderer;
}