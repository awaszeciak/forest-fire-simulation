package pl.forestfire.simulation;

import pl.forestfire.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasa odpowiedzialna za wykonywanie symulacji rozprzestrzeniania się pożaru lasu.
 * <p>
 *     Symulacja działa krokowo. W każdym kroku wyszukiwane są wszystkie aktualnie płonące komórki,
 *     następnie podejmowana jest próba wypalenia ich oraz zapalenia sąsiednich komórek. Prawdopodobieństwo
 *     zapłonu zależy od parametru beta komórki oraz od kierunku i prędkości wiatru.
 * </p>
 *
 */
public class Simulation {

    /**
     * Las, na którym wykonywana jest symulacja.
     */
    private final Forest forest;

    /**
     * Kierunek wiatru podany w stopniach względem wschodu.
     */
    private final double windAngle;

    /**
     * Prędkość wiatru podana w metrach na sekundę.
     */
    private final double windSpeed;


    /**
     * Tworzy nową symulację dla podanego lasu oraz konfiguracji.
     * @param forest las, na którym będzie wykonywana symulacja
     * @param config konfiguracja symulacji zawierająca między innymi dane o wietrze
     */
    public Simulation(Forest forest, SimulationConfig config) {
        this.forest = forest;
        this.windSpeed = config.windSpeed;
        this.windAngle = config.windAngle;
    }


    /**
     * Wykonuje pojedynczy krok symulacji.
     * <p>
     *     Metoda najpierw wyszukuje wszystkie komórki, które płoną na początku kroku.
     *     Następnie dla każdej z nich sprawdza, czy komórka się wypali czy może
     *     zapalić sąsiednie komórki. Uwzględniani są sąsiedzi w ośmiu kierunkach.
     * </p>
     * @return lista współrzędnych komórek, które zmieniły stan w danym kroku symulacji
     */

    public List<int[]> step() {
        List<int[]> burningCells = new ArrayList<>();
        List<int[]> changedCells = new ArrayList<>();


        for (int x = 0; x < forest.getWidth(); x++) {
            for (int y = 0; y < forest.getHeight(); y++) {
                if (forest.getCell(x, y).getState() == State.BURNING) {
                    burningCells.add(new int[]{x,y});
                }
            }
        }

        if (burningCells.isEmpty()) {
            return changedCells;
        }


        for (int[] pos : burningCells) {
            int x = pos[0];
            int y = pos[1];

            Cell cell = forest.getCell(x, y);


            if(cell.burn()){
                forest.updateCounter(State.BURNING, State.DEAD);
                changedCells.add(new int[]{x,y});
            }


            int[][] neighbours = {
                    {x+1, y+1},
                    {x-1, y-1},
                    {x-1, y+1},
                    {x+1, y-1},
                    {x+1, y},
                    {x-1, y},
                    {x, y+1},
                    {x, y-1}
            };


            for (int[] n : neighbours) {
                int nx = n[0];
                int ny = n[1];


                if (nx >= 0 && nx < forest.getWidth() &&
                        ny >= 0 && ny < forest.getHeight()) {

                    Cell neighbour = forest.getCell(nx, ny);


                    if (neighbour.getState() == State.SUSPECTED) {


                        double probability = calculateProbability(neighbour.getBeta(), x, y, nx, ny);


                        if(neighbour.startBurning(probability)) {
                            forest.updateCounter(State.SUSPECTED, State.BURNING);
                            changedCells.add(new int[]{nx,ny});
                        }
                    }
                }
            }
        }
        return changedCells;
    }

    /**
     * Oblicza prawdopodobieństwo zapalenia sąsiedniej komórki z uwzględnieniem wiatru.
     * <p>
     *     Jeżeli ogień rozprzestrzenia się zgodnie z kierunkiem wiatru, prawdopodobieństwo
     *     zapłonu wzrasta. Jeżeli rozprzestrzenia się przeciwnie do kierunku wiatru,
     *     prawdopodobieństwo maleje.
     * </p>
     * @param beta bazowe prawdopodobieństwo zapłonu komórki
     * @param x współrzędna x aktualnie płonącej komórki
     * @param y współrzędna y aktualnie płonącej komórki
     * @param nx współrzędna x sprawdzanej komórki sąsiedniej
     * @param ny współrzędna y sprawdzanej komórki sąsiedzniej
     * @return końcowe prawdopodobieństwo zapłonu ograniczone do przedziału od 0 do 1
     */
        private double calculateProbability(double beta, int x, int y, int nx, int ny) {
            int dx = nx - x;
            int dy = ny - y;


            double length = Math.sqrt(dx * dx + dy * dy);
            double dirX = dx / length;
            double dirY = dy / length;


            double rad = Math.toRadians(windAngle);
            double windX = Math.cos(rad);
            double windY = Math.sin(rad);


            double dot = dirX * windX + dirY * windY;


            double windCoefficient = 0.08;


            double factor = Math.exp(windCoefficient*windSpeed*dot);


            double probability = beta * factor;


            return Math.max(0.0, Math.min(probability, 1.0));
        }

    /**
     * Zwraca kierunek wiatru używany w symulacji.
     *
     * @return kierunek wiatru w stopniach względem wschodu
     */
    public double getWindAngle(){
            return windAngle;
    }


    /**
     * Zwraca prędkość wiatru używaną w symulacji.
     *
     * @return prędkość wiatru w metrach na sekundę
     */
    public double getWindSpeed(){
        return windSpeed;
    }

    /**
     * Zwraca las, na którym wykonywana jest symulacja.
     *
     * @return obiekt lasu używany w symulacji
     */
    public Forest getForest() { return forest; }
}
