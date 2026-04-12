package pl.forestfire.simulation;

import pl.forestfire.model.*;

import java.util.Random;

public class Simulation {

    private Forest forest;
    private Random random = new Random();

    public Simulation(Forest forest) {
        this.forest = forest;
    }

    public Forest getForest() {
        return forest;
    }

    public void step() {
        int x = random.nextInt(forest.getWidth());
        int y = random.nextInt(forest.getHeight());

        Cell cell = forest.getCell(x, y);

        if (cell.getState() == State.BURNING) {
            double r = random.nextDouble();

            if (r < 0.1) {
                cell.setState(State.DEAD);
            }
        } else {
            int[][] neighbours = {
                    {x+1, y},
                    {x-1, y},
                    {x, y+1},
                    {x, y-1}
            };

            int[] n = neighbours[random.nextInt(neighbours.length)];
            int nx = n[0];
            int ny = n[1];

            if (nx >= 0 && nx < forest.getWidth() &&
                ny >= 0 && ny < forest.getHeight()) {

                Cell neighbour = forest.getCell(nx, ny);

                if (neighbour.getState() == State.SUSPECTED) {
                    double r = random.nextDouble();
                    if (r < 1.0) {
                        neighbour.setState(State.BURNING);
                    }
                }
            }
        }

    }


}
