package io.muzoo.ooc.ecosystems.actor;

import io.muzoo.ooc.ecosystems.field.Field;
import io.muzoo.ooc.ecosystems.location.Location;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Tiger extends Predator {

    // Characteristics shared by all Tiger (static fields).

    // Tiger will eat both rabbits and foxes

    // The age at which a Tiger can start to breed.
    private static final int BREEDING_AGE = 10;
    // The age to which a tiger can live.
    private static final int MAX_AGE = 150;
    // The likelihood of a tiger breeding.
    private static final double BREEDING_PROBABILITY = 0.12;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 5;
    // The food value of a single rabbit. In effect, this is the
    // number of steps a tiger can go before it has to eat again.
    private static final int RABBIT_FOOD_VALUE = 4;
    // Fox is harder to catch than rabbit;
    private static final int FOX_FOOD_VALUE = 5;
    // A shared random number generator to control breeding.
    private static final Random rand = new Random();


    // The tiger's food level, which is increased by eating foxes or rabbits.
    private int foodLevel;


    public Tiger() {
        super();
        foodLevel = getFoodValue();
    }


    protected int getFoodValue() {
        return RABBIT_FOOD_VALUE + FOX_FOOD_VALUE;
    }


    public void randomizeFoodValue() {
        foodLevel = rand.nextInt(getFoodValue());

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
    public void giveBirth(Field updatedField, List<Actor> newTigers) {

        int births = breed();
        for (int b = 0; b < births; b++) {
            Location loc = updatedField.randomAdjacentLocation(getLocation());
            Actor newTiger = ActorFactory.createActor(Tiger.class, false);
            assert newTiger != null;
            newTiger.setLocation(loc);
            newTigers.add(newTiger);
            updatedField.place(this, loc);
        }
    }


    @Override
    protected int getBreedingAge() {
        return BREEDING_AGE;
    }


    /**
     * Make this fox more hungry. This could result in the fox's death.
     */
    protected void incrementHunger() {
        foodLevel--;
        if (foodLevel <= 0) {
            setAlive(false);
        }
    }


    @Override
    protected double getBreedingProb() {
        return BREEDING_PROBABILITY;
    }

    protected Location findFood(Field currentField, Location location) {

        Iterator adjacentLocations =
                currentField.adjacentLocations(location);
        while (adjacentLocations.hasNext()) {
            Location where = (Location) adjacentLocations.next();
            Actor actor = currentField.getActorAt(where);
            boolean isRabbit = actor instanceof Rabbit;
            boolean isFox = actor instanceof Fox;
            if (isRabbit) {

                Rabbit rabbit = (Rabbit) actor;
                if (rabbit.isAlive()) {
                    rabbit.setDead();
                    foodLevel = RABBIT_FOOD_VALUE;
                    return where;
                }
            } else if (isFox) {
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


}

