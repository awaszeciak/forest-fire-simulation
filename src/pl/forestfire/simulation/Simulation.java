package pl.forestfire.simulation;

import pl.forestfire.model.*;

import java.util.ArrayList;
import java.util.List;

public class Simulation {

    private final Forest forest;

    private final double windAngle;
    private final double windSpeed;

    public Simulation(Forest forest, SimulationConfig config) {
        this.forest = forest;
        this.windSpeed = config.windSpeed;
        this.windAngle = config.windAngle;
    }


    public void step() {

        List<int[]> burningCells = new ArrayList<>();


        for (int x = 0; x < forest.getWidth(); x++) {
            for (int y = 0; y < forest.getHeight(); y++) {
                if (forest.getCell(x, y).getState() == State.BURNING) {
                    burningCells.add(new int[]{x,y});
                }
            }
        }


        if (burningCells.isEmpty()) {
            return;
        }


        for (int[] pos : burningCells) {
            int x = pos[0];
            int y = pos[1];

            Cell cell = forest.getCell(x, y);


            if(cell.burn())
                forest.updateCounter(State.BURNING, State.DEAD);


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


                        double probability = calculateProbability(neighbour.getAlfa(), x, y, nx, ny);


                        if(neighbour.startBurning(probability))
                            forest.updateCounter(State.SUSPECTED, State.BURNING);
                    }
                }
            }
        }
    }

        private double calculateProbability(double alfa, int x, int y, int nx, int ny) {

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


            double probability = alfa * factor;


            return Math.max(0.0, Math.min(probability, 1.0));
        }

        public double getWindAngle(){
            return windAngle;
        }

        public double getWindSpeed(){
            return windSpeed;
        }






}
