package pl.forestfire.model;

import java.util.Random;

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

    public Cell[][] getForest() {
        return forest;
    }

    public Cell getRandomCel()
    {
        Random random;
        return forest[random.nextInt(0, width)][random.nextInt(0, height)];
    }

    public Cell getCell(int x, int y)
    {
        if (x>=width || x<0 || y>=height || y<0)
        {
            throw new Exception("Wrong argumengt");
        }
        return forest[x][y];
    }

    //tablica przechowujaca las i zmienne odpoiwadajac
    private Cell[][] forest;
    private int height;
    private int width;
}
