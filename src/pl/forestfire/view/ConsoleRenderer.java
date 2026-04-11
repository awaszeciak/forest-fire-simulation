package pl.forestfire.view;
import pl.forestfire.model.*;

public class ConsoleRenderer {
    private int width=0;
    private int high=0;
    private String color="white";
    public ConsoleRenderer()//czyści terminal
    {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    private void moveDrawer(int x, int y)//przesuwa kursor na koordynaty (x,y)
    {
        int width_difference=x-width;
        int high_difference=y-high;

        if(width_difference!=0)
        {
            if(width_difference>0)
                System.out.printf("\033[%dC", width_difference);//idzie w prawo
            else
                System.out.printf("\033[%dD", -width_difference);//idzie w lewo
        }

        if(high_difference!=0)
        {
            if(high_difference>0)
                System.out.printf("\033[%dB", high_difference);//idzie w dół
            else
                System.out.printf("\033[%dA", -high_difference);//idzie w górę
        }

        width=x;
        high=y;
    }
    private void changeColor(String new_color)//zmienia kolor na podany
    {
        if(!color.equals(new_color))
        {
            color=new_color;
            switch(color)
            {
                case "green":
                    System.out.printf("\033[32m");
                    break;
            
                case "red":
                    System.out.printf("\033[31m");
                    break;

                case "black":
                    System.out.printf("\033[90m");
                    break;

                case "white":
                    System.out.printf("\033[0m");
                    break;
            }
        }
    }
    public void drawForest(Forest forest)//rysuja cały las
    {
        moveDrawer(0, 0);
        for(int y=0; y<forest.getHigh(); y++)//metody getHigh() nie ma jeszcze w lesie
        {
            for(int x=0; x<forest.getWidth(); x++)//metody getWidth() nie ma jeszcze w lesie
            {
                drawCell(forest, x, y);
            }
            System.out.println();
            ++high;
        }
        changeColor("white");
        width=0;
    }
    public void drawCell(Forest forest, int x, int y)//rysuje komórkę z lasu na pozycji [x][y]
    {
        moveDrawer(x, y);
        changeColor(forest.getCell(x, y).getState());//porblem z widocznością State (ograniczony do pakietu model), dla testu getState() zwraca String
        System.out.print("0");
        ++width;
    }
}