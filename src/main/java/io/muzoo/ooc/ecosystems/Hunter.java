package io.muzoo.ooc.ecosystems;

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
    private static final int RABBIT_FOOD_VALUE = 10;
    private static final int FOX_FOOD_VALUE = 10;
    private static final int TIGER_FOOD_VALUE = 10;
    // A shared random number generator to control breeding.
    private static final Random rand = new Random();

    // Immortal! Always alive/**/


    // Individual characteristics (instance fields).

    // The hunters's age.
    private int age;
    // The hunter's food level, which is increased by eating others.
    private int foodLevel;

    /**
     * Create a hunter. A hunter is always created as a new born (age zero
     * and not hungry) or with random age.
     */


    public Hunter() {
        age = 0;
        foodLevel = RABBIT_FOOD_VALUE + TIGER_FOOD_VALUE + FOX_FOOD_VALUE;
    }


    /**
     * Increase the age. This could result in the fox's death.
     */
    private void incrementAge() {
        age++;
    }


    /**
     * Make this hunter more hungry. This could result in the others's death.
     */
    private void incrementHunger() {
        foodLevel--;
        if (foodLevel <= 0) {
            foodLevel = RABBIT_FOOD_VALUE + FOX_FOOD_VALUE + TIGER_FOOD_VALUE;
        }
    }

    /**
     * Tell the fox to look for rabbits adjacent to its current location.
     *
     * @param field    The field in which it must look.
     * @param location Where in the field it is located.
     * @return Where food was found, or null if it wasn't.
     */
    protected Location findFood(Field field, Location location) {


        Iterator<Location> adjacentLocations =
                field.adjacentLocations(location);
        while (adjacentLocations.hasNext()) {
            Location where = adjacentLocations.next();
            Actor actor = field.getActorAt(where);
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

    /**
     * Generate a number representing the number of births,
     * if it can breed.
     *
     * @return The number of births (may be zero).
     */
    protected int breed() {
        int births = 0;
        if (canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }


    /**
     * A fox can breed if it has reached the breeding age.
     */
    private boolean canBreed() {
        return age >= BREEDING_AGE;
    }

    /**
     * Check whether the fox is alive or not.
     *
     * @return True if the fox is still alive.
     */


    @Override
    public boolean isAlive() {
        return true;
    }

    @Override
    public void makeAction(Field currentField, Field updateField, List<Actor> newActor) {
        incrementAge();
        incrementHunger();
        hunt(currentField, updateField, newActor);

    }
}
