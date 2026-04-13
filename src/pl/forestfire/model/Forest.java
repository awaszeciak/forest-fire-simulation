package pl.forestfire.model;

import java.util.Random;

public class Forest {
    public Forest(int width, int height){
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

        N = width*height;
        S = N;
        B = 0;
        D = 0;
    }

    //zwaracenie wyskosci i szerokosci lasu
    public int getWidth() {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public Cell[][] getForest() {
        return forest;
    }

    public Cell getRandomCell()
    {
        Random random = new Random();
        return forest[random.nextInt(0, width)][random.nextInt(0, height)];
    }

    public Cell getCell(int x, int y)
    {
        if (x>=width || x<0 || y>=height || y<0)
        {
            throw new IndexOutOfBoundsException("Wrong argument");
        }
        return forest[x][y];
    }

    public void setCell(int x, int y, State state)
    {
        if (x>=width || x<0 || y>=height || y<0)
        {
            throw new IndexOutOfBoundsException("Wrong argument");
        }
        forest[x][y].setState(state);
    }

    //tablica przechowujaca las i zmienne odpoiwadajac
    private Cell[][] forest;
    private int height;
    private int width;

    private int N;
    private int S;
    private int B;
    private int D;
}
