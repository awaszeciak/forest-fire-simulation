package pl.forestfire.view;
import pl.forestfire.model.*;

public class ConsoleRenderer {

    private ConsoleRenderer()
    {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> clean()));
        System.out.print("\033[H\033[2J");
        System.out.print("\033[?25l");
        System.out.flush();
    }

    public static ConsoleRenderer getConsoleRenderer()
    {
        if(consolerenderer==null)
            consolerenderer=new ConsoleRenderer();
        return consolerenderer;
    }


    public void newLine()
    {
        System.out.println();
        ++height;
        width=0;
    }


    private void moveDrawer(int x, int y)
    {
        int width_difference=x-width;
        int height_difference=y-height;

        if(width_difference!=0)
        {
            if(width_difference>0)
                System.out.printf("\033[%dC", width_difference);
            else
                System.out.printf("\033[%dD", -width_difference);
        }

        if(height_difference!=0)
        {
            if(height_difference>0)
                System.out.printf("\033[%dB", height_difference);
            else
                System.out.printf("\033[%dA", -height_difference);
        }

        width=x;
        height=y;
    }


    private void changeColor(String new_color)
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

    public void drawForest(Forest forest)
    {
        moveDrawer(0, 0);
        for(int y=0; y<forest.getHeight(); y++)
        {
            for(int x=0; x<forest.getWidth(); x++)
            {
                drawCell(forest, x, y);
            }
            newLine();
        }
        changeColor("white");
    }


    public void drawCell(Forest forest, int x, int y)
    {
        moveDrawer(x, y);
        changeColor(forest.getCell(x, y).getState().getColor());
        System.out.print("0");
        ++width;
    }


    public void clean()
    {
        System.out.flush();
        System.out.printf("\033[u");
        changeColor("white");
        System.out.print("\033[?25h");
    }


    private String directionString(double angle)
    {
        String direction=new String();
        if(angle>180.0)
            direction+="N";

        if(angle>0&&angle<180.0)
            direction+="S";

        if(angle>90.0&&angle<270.0)
            direction+="W";

        if((angle<90.0&&angle>=0.0)||(angle>270.0&&angle<360.0))
            direction+="E";

        return direction;
    }


    public void showStatistics(Forest forest, double speed, double angle)
    {     
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
        System.out.printf("\033[s");
    }

    private int width=0;
    private int height=0;
    private String color="white";
    private static ConsoleRenderer consolerenderer;
}