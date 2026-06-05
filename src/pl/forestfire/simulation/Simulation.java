package pl.forestfire.simulation;

import pl.forestfire.model.*;

import java.util.ArrayList;
import java.util.List;

public class Simulation {

    private final Forest forest;

    private final double windAngle; //stopnie wzgledem wschodu
    private final double windSpeed; // m/s

    public Simulation(Forest forest, SimulationConfig config) {
        this.forest = forest;
        this.windSpeed = config.windSpeed;
        this.windAngle = config.windAngle;
    }


    public List<int[]> step() {
        // lista komorek ktore plona na poczatku danego kroku symulacji
        List<int[]> burningCells = new ArrayList<>();
        //lista komorek, które uległy zmianie
        List<int[]> changedCells = new ArrayList<>();

        // wyszukanie wszystkich aktualnie plynacych komorek
        for (int x = 0; x < forest.getWidth(); x++) {
            for (int y = 0; y < forest.getHeight(); y++) {
                if (forest.getCell(x, y).getState() == State.BURNING) {
                    burningCells.add(new int[]{x,y});
                }
            }
        }

        // jesli zadna komorka nie plonie krok symulacji sie konczy
        if (burningCells.isEmpty()) {
            return changedCells;
        }

        // przetwarzanie kazdej komorki ktora plonela na poczatku kroku
        for (int[] pos : burningCells) {
            int x = pos[0];
            int y = pos[1];

            Cell cell = forest.getCell(x, y);

            // proba wypalenia aktualnie plonacej komorki
            if(cell.burn()){
                forest.updateCounter(State.BURNING, State.DEAD);
                changedCells.add(new int[]{x,y});
            }

            // wspolrzedne osmiu sasiadow komorki
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

            // sprawdzenie kazdego sasiada
            for (int[] n : neighbours) {
                int nx = n[0];
                int ny = n[1];

                // pominiecie sasiadow znajdujacych sie poza plansza
                if (nx >= 0 && nx < forest.getWidth() &&
                        ny >= 0 && ny < forest.getHeight()) {

                    Cell neighbour = forest.getCell(nx, ny);

                    //tylko niezapalone drzewa moga zaczac sie palic
                    if (neighbour.getState() == State.SUSPECTED) {

                        // obliczenie prawdopodobienstwa zaplonu z uwzglednieniem wiatru
                        double probability = calculateProbability(neighbour.getBeta(), x, y, nx, ny);

                        // proba zapalenia sasiedniej komorki
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

        private double calculateProbability(double beta, int x, int y, int nx, int ny) {
            // wektor od plonacej komorki do sprawdzanego sasiada
            int dx = nx - x;
            int dy = ny - y;

            // normalizacja wektora kierunku rozprzestrzeniania ognia
            double length = Math.sqrt(dx * dx + dy * dy);
            double dirX = dx / length;
            double dirY = dy / length;

            // zmiana kata wiatru ze stopni na wektor jednostkowy
            double rad = Math.toRadians(windAngle);
            double windX = Math.cos(rad);
            double windY = Math.sin(rad);

            // zgodnosc kierunku rozprzestrzeniania ognia z kierunkiem wiatru
            // dot = 1  -> zgodnie z wiatrem
            // dot = 0  -> prostopadle do wiatru
            // dot = -1  -> przeciwnie do wiatru
            double dot = dirX * windX + dirY * windY;

            // wspolczynnik okreslajacy, jak mocno wiatr wplywa na zaplon
            double windCoefficient = 0.08;

            // modyfikacja prawdopodobienstwa zaplonu zaleznie od predkosci i kierunku wiatru
            double factor = Math.exp(windCoefficient*windSpeed*dot);

            // koncowe prawdopodobienstwo zapalenia komorki
            double probability = beta * factor;

            // ograniczenie prawdopodobienstwa do zakresu od 0 do 1
            return Math.max(0.0, Math.min(probability, 1.0));
        }

        public double getWindAngle(){
            return windAngle;
        }

        public double getWindSpeed(){
            return windSpeed;
        }

        public Forest getForest() { return forest; }
}
