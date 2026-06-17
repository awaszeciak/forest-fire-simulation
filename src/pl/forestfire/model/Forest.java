package pl.forestfire.model;

import java.util.Random;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Klasa przechowująca tablice drzew (Cell), składające się na cały las.
 *
 * <p>
 *     Las przechowywany jest jako dwuwymiarowa tablica obiektów {@link Cell}.
 *     Każda komórka może znajdować się w jednym z trzech stanów.
 *     {@link State#SUSPECTED}, {@link State#BURNING}, {@link State#DEAD}.
 * </p>
 *
 * <p>
 *     Klasa przechowuje również liczniki komórek w poszczególnych stanach,
 *     co pozwala szybko sprawdzac aktualny stan symulacji.
 * </p>
 */
public class Forest {
    /**
     * Konstruktor tworzący drzewo o konktretnym rozmiarze, oraz ustawia podane
     * prawdopodoniestwa oraz ustawia wszystkie komórki na SUSPECTED.
     * @param width szerokość lasu
     * @param height wysokość lasu
     * @param alfa podstawowe prawdopodobieństwo zapalnie komórki
     * @param beta podstawowe prawdopodobieństwo spalenia się komórki
     *
     * @throws IllegalArgumentException jeżeli {@code alfa} lub {@code beta} nie należą do przedziału [0, 1]
     * @throws IndexOutOfBoundsException jeżeli szerokość lub wysokość lasu jest mniejsza lub równa 0
     */
    public Forest(int width, int height, double alfa, double beta){
        if (alfa>1 || alfa<0)
        {
            throw new IllegalArgumentException("Alfa value must be between 0 and 1");
        }
        if (beta > 1 || beta <0)
        {
            throw new IllegalArgumentException("Beta value must be between 0 and 1");
        }
        if (width<=0 || height<=0)
        {
            throw new NegativeArraySizeException("arguments must be bigger than 0");
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

    /**
     * Konstruktor tworzący las na podstawie pliku o podanej nazwie.
     *
     * <p>
     *      Plik powinien zwierać kolejne komórki lasu zapisane w formacie tekstowym,
     *      gdzie symbole odpowiadają stanom komórek. Symbol {@code s} oznacza komórkę
     *      zdrową, {@code d} komórkę spaloną, a pozostałe wartości są traktowane jako
     *      komórki płonące.
     * </p>
     *
     * @param file_name nazwa pliku, z którego ma zostać wczytany las
     * @param alfa prawdopodobienstwo zapalenia się płonącej komórki
     * @param beta prawdodpodobieństwo spalenia się sąsiedniej komórki
     */
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
                if (elements.length != width)
                {
                    throw new RuntimeException("Zly rozmiar");
                }
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
                    else if (elements[x].equals("b")){
                        forest[x][y] = new Cell(State.BURNING, alfa, beta);
                        updateCounter(null, State.BURNING);
                    }
                    else
                    {
                        throw new IllegalArgumentException("wrong char in given file");
                    }
                }
                y++;
            }
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    /**
     * Aktualizacja liczników komórek w poszczególnych stanach.
     *
     * <p>
     *     Metoda zwiększa licznik nowego stanu oraz, jeżeli podano poprzedni stan,
     *     zmniejsza licznik poprzedniego stanu. Jest używana podczas tworzenia lasu
     *     oraz przy zmianie stanu konkretnej komórki.
     * </p>
     *
     * @param oldState poprzedni stan komórki; może być {@code null}, gdy komórka jest dodawana po raz pierwszy
     * @param newState nowy stan komórki
     */
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

    /**
     * Zwracanie szerokości lasu.
     * @return szerokość lasu
     */
    public int getWidth() {
        return width;
    }

    /**
     * Zwracanie wysokości lasu.
     * @return wysokość lasu
     */
    public int getHeight()
    {
        return height;
    }

    /**
     * Zwracanie całego lasu jako tablicy.
     * @return las
     */
    public Cell[][] getForest() {
        return forest;
    }

    /**
     * Zwracanie losowej komórki.
     * @return losowa komórka
     */
    public Cell getRandomCell()
    {
        Random random = new Random();
        return forest[random.nextInt(0, width)][random.nextInt(0, height)];
    }

    /**
     * Zwraca komórkę znajdującą się na podanych współrzędnych.
     * @param x współrzędna pozioma komórki
     * @param y współrzędna pionowa komórki
     * @return komórka znajdująca się na pozycji {@code x}, {@code y}
     * @throws IndexOutOfBoundsException jeżeli podane współrzędne znajdują się poza zakresem lasu
     */
    public Cell getCell(int x, int y)
    {
        if (x>=width || x<0 || y>=height || y<0)
        {
            throw new IndexOutOfBoundsException("Wrong argument");
        }
        return forest[x][y];
    }

    /**
     * Zwraca liczbę zdrowych komórek.
     * @return liczba komórek w stanie {@link State#SUSPECTED}
     */
    public int getSuspectedCounter()
    {
        return suspectedCounter;
    }

    /**
     * Zwraca liczbę aktualnie płonących komórek.
     * @return liczbę komórek w stanie {@link State#BURNING}
     */
    public int getBurningCounter()
    {
        return burningCounter;
    }

    /**
     * Zwraca liczbę spalonych komórek.
     * @return liczba komórek w stanie {@link State#DEAD}
     */
    public int getDeadCounter()
    {
        return deadCounter;
    }

    /**
     * Ustawianie konkretnej komórki na podany stan.
     *
     * <p>
     *     Przed zmianą stanu sprawdzana jest poprawność współrzędnych.
     *     Po zmianie aktualizowane są liczniki komórek w poszczególnych stanach.
     * </p>
     *
     * @param x współrzędna pozioma komórki
     * @param y współrzędna pionowa komórki
     * @param state nowy stan komórki
     * @throws IndexOutOfBoundsException jeżeli podane współrzędne znajdują się poza zakresem lasu
     */
    public void setCell(int x, int y, State state)
    {
        if (x>=width || x<0 || y>=height || y<0)
        {
            throw new IndexOutOfBoundsException("Wrong argument");
        }
        updateCounter(forest[x][y].getState(), state);
        forest[x][y].setState(state);

    }


    //tablica przechowujaca las i zmienne odpoiwadajac
    /**
     * Dwuwymiarowa tablica komórek reprezentująca las.
     */
    private Cell[][] forest;
    /**
     * Wysokość lasu.
     */
    private int height;
    /**
     * Szerokość lasu.
     */
    private int width;
    /**
     * Liczba zdrowych komórek, które mogą się zapalić.
     */
    private int suspectedCounter=0;
    /**
     * Liczba aktualnie płonących komórek.
     */
    private int burningCounter=0;
    /**
     * Liczba spalonych komórek.
     */
    private int deadCounter=0;

    /**
     * Całkowita liczba komórek w lesie.
     */
    private int N;
}
