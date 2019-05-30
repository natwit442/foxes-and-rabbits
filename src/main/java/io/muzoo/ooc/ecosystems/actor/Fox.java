package io.muzoo.ooc.ecosystems.actor;


import io.muzoo.ooc.ecosystems.field.Field;
import io.muzoo.ooc.ecosystems.location.Location;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * A simple model of a fox.
 * Foxes age, move, eat rabbits, be eaten by Tiger and die.
 *
 * @author David J. Barnes and Michael Kolling
 * @version 2002.10.28
 */
public class Fox extends Predator {


    // Characteristics shared by all foxes (static fields).
    // The age at which a fox can start to breed.

    private static final int BREEDING_AGE = 10;
    // The age to which a fox can live.
    private static final int MAX_AGE = 150;
    // The likelihood of a fox breeding.
    private static final double BREEDING_PROBABILITY = 0.2;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 7;
    // The food value of a single rabbit. In effect, this is the
    // number of steps a fox can go before it has to eat again.
    private static final int RABBIT_FOOD_VALUE = 6;
    // A shared random number generator to control breeding.
    private static final Random rand = new Random();

    // Individual characteristics (instance fields).


    // The fox's food level, which is increased by eating rabbits.
    private int foodLevel;

    public Fox() {
        super();
        foodLevel = getFoodValue();

    }

    @Override
    protected double getBreedingProb() {
        return BREEDING_PROBABILITY;
    }


    protected int getFoodValue() {
        return RABBIT_FOOD_VALUE;
    }

    public void randomizeFoodValue() {
        foodLevel = rand.nextInt(getFoodValue());
    }


    @Override
    protected int getMaxAge() {
        return MAX_AGE;
    }

    /**
     * Make this fox more hungry. This could result in the fox's death.
     */
    protected void incrementHunger() {
        foodLevel--;
        if (foodLevel <= 0) {

            setDead();
        }
    }

    /**
     * Tell the fox to look for rabbits adjacent to its current location.
     *
     * @param currentField
     * @param location     Where in the field it is located.
     * @return Where food was found, or null if it wasn't.
     */
    protected Location findFood(Field currentField, Location location) {

        Iterator<Location> adjacentLocations = currentField.adjacentLocations(location);

        while (adjacentLocations.hasNext()) {
            Location where = adjacentLocations.next();
            Actor actor = currentField.getActorAt(where);

            if (actor instanceof Rabbit) {
                Rabbit rabbit = (Rabbit) actor;
                if (rabbit.isAlive()) {
                    rabbit.setDead();
                    foodLevel = getFoodValue();
                    return where;
                }
            }
        }
        return null;
    }

    @Override
    public void giveBirth(Field updatedField, List<Actor> newPredators) {

        int births = breed();
        for (int b = 0; b < births; b++) {
            Location loc = updatedField.randomAdjacentLocation(getLocation());
            Actor newFox = ActorFactory.createActor(Fox.class, false);
            assert newFox != null;
            newFox.setLocation(loc);
            newPredators.add(newFox);
            updatedField.place(this, loc);
        }
    }


    @Override
    protected int getBreedingAge() {
        return BREEDING_AGE;
    }

    @Override
    protected int getMaxLitterSize() {
        return MAX_LITTER_SIZE;
    }


    @Override
    public void makeAction(Field updatedField, Field currentField, List<Actor> newActor) {
        incrementAge();
        incrementHunger();
        hunt(updatedField, currentField, newActor);

    }

}
