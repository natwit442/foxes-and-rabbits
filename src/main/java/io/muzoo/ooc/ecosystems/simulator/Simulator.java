package io.muzoo.ooc.ecosystems.simulator;

import io.muzoo.ooc.ecosystems.actor.*;
import io.muzoo.ooc.ecosystems.field.Field;
import io.muzoo.ooc.ecosystems.location.Location;

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
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


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

            actor.makeAction(updatedField, field, newActors);
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
        populate();

        // Show the starting state in the view.
        view.showStatus(step, field);
        view.printState(field);
    }

    private void populate() {
        field.clear();
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Actor actor = ActorFactory.randomizeActor();

                if (actor != null) {
                    actors.add(actor);
                    actor.setLocation(new Location(row, col));
                    field.place(actor, row, col);
                }
            }
        }
        System.out.println(actors.size());
        Collections.shuffle(actors);
    }
}
