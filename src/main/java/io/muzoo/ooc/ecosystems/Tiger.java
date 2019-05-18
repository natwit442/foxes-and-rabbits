package io.muzoo.ooc.ecosystems;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Tiger extends Predator {

    // Characteristics shared by all Tiger (static fields).

    // Tiger will eat both rabbits and foxes

    // The age at which a Tiger can start to breed.
    private static final int BREEDING_AGE = 10;
    // The age to which a tiger can live.
    private static final int MAX_AGE = 200;
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


    // The tiger's age.
    private int age;

    // The tiger's food level, which is increased by eating foxes or rabbits.
    private int foodLevel;

    public Tiger() {
        super();
        age =0;
    }

    /**
     * Create a tiger. A tiger can be created as a new born (age zero
     * and not hungry) or with random age.
     *
     * @param randomAge If true, the fox will have random age and hunger level.
     */
    public Tiger(boolean randomAge) {
        age = 0;
        if (randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(RABBIT_FOOD_VALUE);


        } else {
            // Leave age at 0
            foodLevel = RABBIT_FOOD_VALUE;
        }
    }




    /**
     * Increase the age. This could result in the tiger's death.
     */
    private void incrementAge() {
        age++;
        if (age > MAX_AGE) {
            setAlive(false);
        }
    }


    /**
     * Make this fox more hungry. This could result in the fox's death.
     */
    private void incrementHunger() {
        foodLevel--;
        if (foodLevel <= 0) {
            setAlive(false);
        }
    }


    /**
     * Tell the tiger to look for rabbits or foxes adjacent to its current location.
     *
     * @param field    The field in which it must look.
     * @param location Where in the field it is located.
     * @return Where food was found, or null if it wasn't.
     */
    protected Location findFood(Field field, Location location) {
        Iterator adjacentLocations =
                field.adjacentLocations(location);
        while (adjacentLocations.hasNext()) {
            Location where = (Location) adjacentLocations.next();
            Actor actor = field.getActorAt(where);
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

    @Override
    public void makeAction(Field currentField, Field updateField, List<Actor> newActor) {
        incrementAge();
        incrementHunger();
        hunt(currentField, updateField, newActor);
    }
}

