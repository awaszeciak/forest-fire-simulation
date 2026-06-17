package pl.forestfire.forestwriter;

import pl.forestfire.model.*;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Klasa odpowiadająca za zapis przekazanego lasu do pliku CSV.
 *
 * <p>
 *     Zapisuje komórki lasu w postaci znaków odpowiadających ich stanom.
 *     Kolejne komórki w wierszu oddzielone są przecinkami, a każdy wiersz lasu
 *     zapisywany jest w osobnej linii pliku.
 * </p>
 */
public class ForestWriter
{
    /**
     *
     * Statyczna metoda odpowiadająca za zapis lasu do pliku.
     *
     * <p>
     *     Dla każdej komórki pobierany jest znak odpowiadający jej aktualnemu stanowi,
     *     na przykład {@code s}, {@code b} lub {@code d}. Dzięki temu
     *     zapisany plik może później reprezentować końcowy stan symulacji.
     * </p>
     *
     * @param file_name nazwa pliku, do którego ma zostać zapisany las
     * @param forest las, którego stan ma zostać zapisany do pliku
     */
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