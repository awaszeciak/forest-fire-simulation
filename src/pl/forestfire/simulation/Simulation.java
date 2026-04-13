package pl.forestfire.simulation;

import pl.forestfire.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simulation {

    private final Forest forest;
    private final Random random = new Random();

    public Simulation(Forest forest) {
        this.forest = forest;
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

        int[] pos = burningCells.get(random.nextInt(burningCells.size()));
        int x = pos[0];
        int y = pos[1];

        Cell cell = forest.getCell(x, y);

        cell.burn();

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

        int[] n = neighbours[random.nextInt(neighbours.length)];
        int nx = n[0];
        int ny = n[1];

        if (nx >= 0 && nx < forest.getWidth() &&
                ny >= 0 && ny < forest.getHeight()) {

            Cell neighbour = forest.getCell(nx, ny);

            if (neighbour.getState() == State.SUSPECTED) {
                neighbour.startBurning();
            }
        }
    }


}
