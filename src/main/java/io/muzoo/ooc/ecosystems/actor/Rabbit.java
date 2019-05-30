package io.muzoo.ooc.ecosystems.actor;

import io.muzoo.ooc.ecosystems.field.Field;
import io.muzoo.ooc.ecosystems.location.Location;
import io.muzoo.ooc.ecosystems.simulator.Simulator;

import java.util.List;
import java.util.Random;

/**
 * A simple model of a rabbit.
 * Rabbits age, move, breed, and die.
 *
 * @author David J. Barnes and Michael Kolling
 * @version 2002.10.28
 */
public class Rabbit extends Animal {
    // Characteristics shared by all rabbits (static fields).

    // The age at which a rabbit can start to breed.
    private static final int BREEDING_AGE = 5;
    // The age to which a rabbit can live.
    private static final int MAX_AGE = 100;
    // The likelihood of a rabbit breeding.
    private static final double BREEDING_PROBABILITY = 0.35;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 5;
    // A shared random number generator to control breeding.

    // Individual characteristics (instance fields).

    // The rabbit's age.


    @Override
    protected double getBreedingProb() {
        return BREEDING_PROBABILITY;
    }

    public Rabbit() {
        super();
    }

    /**
     * This is what the rabbit does most of the time - it runs
     * around. Sometimes it will breed or die of old age.

     * @param newRabbits   A list to add newly born rabbits to.
     */
    public void run(Field updatedField, List<Actor> newRabbits) {
        incrementAge();
        if (isAlive()) {
            giveBirth(updatedField, newRabbits);
            Location newLocation = updatedField.freeAdjacentLocation(getLocation());
            // Only transfer to the updated field if there was a free location
            if (newLocation != null) {
                setLocation(newLocation);
                updatedField.place(this, newLocation);
            } else {
                // can neither move nor stay - overcrowding - all locations taken
                setDead();
            }
        }
    }

    @Override
    protected int getMaxLitterSize() {
        return MAX_LITTER_SIZE;
    }

    @Override
    protected int getMaxAge() {
        return MAX_AGE;
    }

    @Override
    protected int getBreedingAge() {
        return BREEDING_AGE;
    }

    @Override
    public void giveBirth(Field updatedField, List<Actor> newRabbits) {
        int births = breed();
        for (int b = 0; b < births; b++) {
            Location loc = updatedField.randomAdjacentLocation(getLocation());
            Rabbit newRabbit = new Rabbit();
            newRabbit.setLocation(loc);
            newRabbits.add(newRabbit);

            updatedField.place(newRabbit, loc);
        }
    }

    @Override
    public void makeAction(Field updatedField, Field currentField, List<Actor> newActor) {
        run(updatedField, newActor);
    }

}
