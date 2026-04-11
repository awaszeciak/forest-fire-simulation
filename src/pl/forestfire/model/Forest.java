package pl.forestfire.model;

public class Forest {
    Forest(int width, int height){
        forest = new Cell[width][height];

        for (int x=0; x<width; x++)
        {
            for (int y=0; y<height; y++)
            {
                forest[x][y] = new Cell();
            }
        }
        this.height = height;
        this.width = width;
    }

    //zwaracenie wyskosci i szerokosci lasu
    public int getWidth() {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    //tablica przechowujaca las i zmienne odpoiwadajac
    private Cell[][] forest;
    private int height;
    private int width;
}
