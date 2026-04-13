package pl.forestfire.view;
import pl.forestfire.model.*;

public class ConsoleRenderer {
    private int width=0;
    private int height=0;
    private String color="white";
    public ConsoleRenderer()//czyści terminal
    {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    private void moveDrawer(int x, int y)//przesuwa kursor na koordynaty (x,y)
    {
        int width_difference=x-width;
        int height_difference=y-height;

        if(width_difference!=0)
        {
            if(width_difference>0)
                System.out.printf("\033[%dC", width_difference);//idzie w prawo
            else
                System.out.printf("\033[%dD", -width_difference);//idzie w lewo
        }

        if(height_difference!=0)
        {
            if(height_difference>0)
                System.out.printf("\033[%dB", height_difference);//idzie w dół
            else
                System.out.printf("\033[%dA", -height_difference);//idzie w górę
        }

        width=x;
        height=y;
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
        for(int y=0; y<forest.getHeight(); y++)
        {
            for(int x=0; x<forest.getWidth(); x++)
            {
                drawCell(forest, x, y);
            }
            System.out.println();
            ++height;
        }
        changeColor("white");
        width=0;
    }
    public void drawCell(Forest forest, int x, int y)//rysuje komórkę z lasu na pozycji [x][y]
    {
        moveDrawer(x, y);
        changeColor(forest.getCell(x, y).getState().getColor());//problem z widocznością State (ograniczony do pakietu model)
        System.out.print("0");
        ++width;
    }
}