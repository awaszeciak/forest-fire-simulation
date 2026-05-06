package pl.forestfire.model;

import java.util.Random;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class Forest {
    public Forest(int width, int height, double alfa, double beta){
        if (width<=0 || height<=0)
        {
            throw new IndexOutOfBoundsException("arguments must be bigger than 0");
        }

        forest = new Cell[width][height];

        for (int x=0; x<width; x++)
        {
            for (int y=0; y<height; y++)
            {
                forest[x][y] = new Cell(alfa, beta);
                updateCounter(null, forest[x][y].getState());
            }
        }
        this.height = height;
        this.width = width;

        N = width*height;
    }

    public Forest(String file_name, double alfa, double beta)
    {
        int y=0;
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(file_name))) {
            line = br.readLine();
            width = (line.split(",")).length;

            while ((line = br.readLine()) != null) {
                y++;
            }

        } catch (IOException e){
            e.printStackTrace();
        }

        height = y+1;
        forest = new Cell[width][y+1];

        y=0;
        try (BufferedReader br = new BufferedReader(new FileReader(file_name))) {
            while ((line = br.readLine()) != null) {
                String[] elements = line.split(",");
                for (int x = 0; x<width; x++)
                {
                    if (elements[x].equals("s"))
                    {
                        forest[x][y] = new Cell(State.SUSPECTED, alfa, beta);
                        updateCounter(null, State.SUSPECTED);
                    } else if (elements[x].equals("d")) {
                        forest[x][y] = new Cell(State.DEAD, alfa, beta);
                        updateCounter(null, State.DEAD);
                    }
                    else {
                        forest[x][y] = new Cell(State.BURNING, alfa, beta);
                        updateCounter(null, State.BURNING);
                    }
                }
                y++;
            }
        } catch (IOException e){
            e.printStackTrace();
        }

    }


    public void updateCounter(State oldState, State newState)
    {
        switch(newState)
        {
            case SUSPECTED: 
                suspectedCounter++;
                break;
            case BURNING: 
                burningCounter++;
                break;
            case DEAD: 
                deadCounter++;
                break;
        }

        if(oldState!=null)
        {
            switch(oldState)
            {
                case BURNING: 
                    burningCounter--;
                    break;
                case DEAD: 
                    deadCounter--;
                    break;
                case SUSPECTED: 
                    suspectedCounter--;
                    break;
            }
        }
    }


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

    public int getSuspectedCounter()
    {
        return suspectedCounter;
    }

    public int getBurningCounter()
    {
        return burningCounter;
    }

    public int getDeadCounter()
    {
        return deadCounter;
    }

    public void setCell(int x, int y, State state)
    {
        if (x>=width || x<0 || y>=height || y<0)
        {
            throw new IndexOutOfBoundsException("Wrong argument");
        }
        updateCounter(forest[x][y].getState(), state);
        forest[x][y].setState(state);
        
    }



    private Cell[][] forest;
    private int height;
    private int width;
    private int suspectedCounter=0;
    private int burningCounter=0;
    private int deadCounter=0;

    private int N;
}
