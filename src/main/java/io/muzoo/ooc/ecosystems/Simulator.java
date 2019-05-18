package io.muzoo.ooc.ecosystems;

import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * A simple predator-prey simulator, based on a field containing
 * rabbits and foxes.
 *
 * @author David J. Barnes and Michael Kolling
 * @version 2002.10.28
 */
public class Simulator {
    // The private static final variables represent 
    // configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 10;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 10;
    // The probability that a fox will be created in any given grid position.
    private static final double FOX_CREATION_PROBABILITY = 0.06;
    // The probability that a rabbit will be created in any given grid position.
    private static final double RABBIT_CREATION_PROBABILITY = 0.2;
    // The probability that a tiger will be created in any given grid position
    private static final double TIGER_CREATION_PROBABILITY = 0.1;

    // The probability that a tiger will be created in any given grid position
    private static final double HUNTER_CREATION_PROBABILITY = 0.001;

    // The list of actor in the field
    private List<Actor> actors;
    // The list of actor just born
    private List<Actor> newActors;
    // The current state of the field.
    private Field field;
    // A second field, used to build the next stage of the simulation.
    private Field updatedField;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private SimulatorView view;

    /**
     * Construct a simulation field with default size.
     */
    public Simulator() {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }

    /**
     * Create a simulation field with the given size.
     *
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width) {
        if (width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }
        actors = new ArrayList<>();
        newActors = new ArrayList<>();
        field = new Field(depth, width);
        updatedField = new Field(depth, width);

        // Create a view of the state of each location in the field.
        view = new SimulatorView(depth, width);
        view.setColor(Fox.class, Color.blue);
        view.setColor(Rabbit.class, Color.orange);
        view.setColor(Tiger.class, Color.black);
        view.setColor(Hunter.class, Color.RED);

        // Setup a valid starting point.
        reset();
    }

    /**
     * Run the simulation from its current state for a reasonably long period,
     * e.g. 500 steps.
     */
    public void runLongSimulation() {
        simulate(500);
    }

    /**
     * Run the simulation from its current state for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     */
    public void simulate(int numSteps) {
        for (int step = 1; step <= numSteps && view.isViable(field); step++) {

            simulateOneStep();
        }

        view.printState(field);


    }

    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each
     * fox and rabbit.
     */
    public void simulateOneStep() {
        step++;
        newActors.clear();

        // Now it is a list of animal now no need to cast
        for (Iterator<Actor> iter = actors.iterator(); iter.hasNext(); ) {
            Actor actor = iter.next();

            actor.makeAction(field, updatedField, newActors);
            if (!actor.isAlive()) iter.remove();

        }
        // add new born animals to the list of animals
        actors.addAll(newActors);

        // Swap the field and updatedField at the end of the step.
        Field temp = field;
        field = updatedField;
        updatedField = temp;
        updatedField.clear();

        // display the new field on screen

        view.showStatus(step, field);
    }

    /**
     * Reset the simulation to a starting position.
     */
    public void reset() {
        step = 0;
        actors.clear();
        field.clear();
        updatedField.clear();
        populate(field);

        // Show the starting state in the view.
        view.showStatus(step, field);
    }

    /**
     * Populate a field with foxes, rabbits and tigers.
     *
     * @param field The field to be populated.
     */
    private void populate(Field field) {
        Random rand = new Random();
        field.clear();
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                if (rand.nextDouble() <= HUNTER_CREATION_PROBABILITY) {
                    Hunter hunter = new Hunter(field, new Location(row, col));
                    actors.add(hunter);
                    field.place(hunter, row, col);
                } else if (rand.nextDouble() <= TIGER_CREATION_PROBABILITY) {
                    Tiger tiger = new Tiger(true, field, new Location(row, col));
                    actors.add(tiger);
                    field.place(tiger, row, col);
                }
                else if (rand.nextDouble() <= FOX_CREATION_PROBABILITY) {
                    Fox fox = new Fox(true, field, new Location(row, col));
                    actors.add(fox);
                    field.place(fox, row, col);
                } else if (rand.nextDouble() <= RABBIT_CREATION_PROBABILITY) {
                    Rabbit rabbit = new Rabbit(true, field, new Location(row, col));
                    actors.add(rabbit);
                    field.place(rabbit, row, col);
                }
                // else leave the location empty.


            }
        }
        Collections.shuffle(actors);
    }
}
