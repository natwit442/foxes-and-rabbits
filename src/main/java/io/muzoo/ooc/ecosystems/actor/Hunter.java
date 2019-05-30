package io.muzoo.ooc.ecosystems.actor;

import io.muzoo.ooc.ecosystems.field.Field;
import io.muzoo.ooc.ecosystems.location.Location;
import io.muzoo.ooc.ecosystems.simulator.Simulator;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Hunter extends Predator {
    // This hunter is immortal;
    // Characteristics shared by all hunters (static fields).
    // The age at which a fox can start to breed.

    private static final int BREEDING_AGE = 300;
    // The age to which a hunter can live.
    private static final int MAX_AGE = Integer.MAX_VALUE;
    // The likelihood of a hunter breeding.
    private static final double BREEDING_PROBABILITY = 0.4;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 5;
    // The food value of all Actors. In effect, this is the
    // number of steps a hunter can go before it has to eat again.
    private static final int RABBIT_FOOD_VALUE = 100;
    private static final int FOX_FOOD_VALUE = 100;
    private static final int TIGER_FOOD_VALUE = 100;
    // A shared random number generator to control breeding.
    private static final Random rand = new Random();


    // The hunter's food level, which is increased by eating others.
    private int foodLevel;

    /**
     * Create a hunter. A hunter is always created as a new born (age zero
     * and not hungry) or with random age.
     */


    public Hunter() {
        super();
        foodLevel = getFoodValue();
    }

    @Override
    protected double getBreedingProb() {
        return BREEDING_PROBABILITY;
    }


    protected int getFoodValue() {
        return RABBIT_FOOD_VALUE + FOX_FOOD_VALUE + TIGER_FOOD_VALUE;
    }


    public void randomizeFoodValue() {
        foodLevel = rand.nextInt(getFoodValue());

    }

    /**
     * Make this hunter more hungry. This could result in the others's death.
     */
    protected void incrementHunger() {
        foodLevel--;
        if (foodLevel <= 0) {
            foodLevel = RABBIT_FOOD_VALUE + FOX_FOOD_VALUE + TIGER_FOOD_VALUE;
        }
    }


    protected Location findFood(Field currentField, Location location) {

        Iterator<Location> adjacentLocations = currentField.adjacentLocations(location);
        while (adjacentLocations.hasNext()) {
            Location where = adjacentLocations.next();
            Actor actor = currentField.getActorAt(where);
            if (actor instanceof Rabbit) {
                Rabbit rabbit = (Rabbit) actor;
                if (rabbit.isAlive()) {
                    rabbit.setDead();
                    foodLevel = RABBIT_FOOD_VALUE;
                    return where;
                }
            } else if (actor instanceof Tiger) {
                Tiger tiger = (Tiger) actor;
                if (tiger.isAlive()) {
                    tiger.setDead();
                    foodLevel = TIGER_FOOD_VALUE;
                    return where;
                }

            } else if (actor instanceof Fox) {
                Fox fox = (Fox) actor;
                if (fox.isAlive()) {
                    fox.setDead();
                    foodLevel = FOX_FOOD_VALUE;
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
            Actor newHunter = ActorFactory.createActor(Hunter.class, false);

            assert newHunter != null;
            newHunter.setLocation(loc);
            newPredators.add(newHunter);
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
    protected int getMaxAge() {
        return MAX_AGE;
    }

    @Override
    public boolean isAlive() {
        return true;
    }


}
