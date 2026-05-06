package pl.forestfire.forestwriter;

import pl.forestfire.model.*;

import java.io.FileWriter;
import java.io.IOException;


public class ForestWriter
{
    public static void WriteToCsv(String file_name, Forest forest)
    {
        try (FileWriter file = new FileWriter(file_name))
        {
            for(int y=0; y<forest.getHeight(); y++)
            {
                for(int x=0; x<forest.getWidth(); x++)
                {
                    file.append(forest.getCell(x, y).getState().getChar());
                    if(x<forest.getWidth()-1)
                    {
                        file.append(",");
                    }
                }
                file.append("\n");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}