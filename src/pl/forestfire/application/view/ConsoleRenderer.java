package pl.forestfire.application.view;
import pl.forestfire.model.*;

public class ConsoleRenderer implements Renderer{

    //czyści terminal i ukrywa kursor
    private ConsoleRenderer() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::clean));//zapewnia, że przy zamknięciu programu wykona się clean()
        System.out.print("\033[H\033[2J");//czyszczenie terminala
        System.out.print("\033[?25l");//ukrycie kursora
        System.out.flush();
    }

    public static ConsoleRenderer getConsoleRenderer() {
        if(consolerenderer==null)
            consolerenderer=new ConsoleRenderer();
        return consolerenderer;
    }

    //przechodzi do nowej linii
    public void newLine() {
        System.out.println();
        ++height;
        width=0;
    }

    //przesuwa kursor na koordynaty (x,y)
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
        // System.out.printf("\033[%d;%dH", y,x); //działa, ale powoduje migotanie ekranu, za to eliminując potrzebę zmiennych width i height

        width=x;
        height=y;
    }

    //zmienia kolor na podany
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

    //rysuje cały las
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

    //rysuje komórkę z lasu na pozycji [x][y]
    @Override
    public void drawCell(Forest forest, int x, int y) {
        moveDrawer(x, y);
        changeColor(forest.getCell(x, y).getState().getColor());
        System.out.print("0");
        ++width;
    }

    //przywraca kursor przy zakończeniu programu lub zamknięciu ctrl+c
    public void clean() {
        System.out.flush();
        System.out.print("\033[u");//przywraca zapisaną pozycję kursora
        changeColor("white");
        System.out.print("\033[?25h");//przywraca kursor
        System.out.flush();
    }

    //wyświetla ilość płonących, niepłonących oraz spalonych drzew pod lasem i inne statystyki
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

    private int width=0;
    private int height=0;
    private String color="white";
    private static ConsoleRenderer consolerenderer;
}