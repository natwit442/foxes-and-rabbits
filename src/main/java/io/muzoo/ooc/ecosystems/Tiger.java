package io.muzoo.ooc.ecosystems;

import io.muzoo.occ.ecosystems.blueprints.Actor;
import io.muzoo.occ.ecosystems.blueprints.Animal;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Tiger implements Animal {

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
    // Whether the tiger is alive or not.
    private boolean alive;
    // The tiger's position
    private Location location;
    // The tiger's food level, which is increased by eating foxes or rabbits.
    private int foodLevel;


    /**
     * Create a tiger. A tiger can be created as a new born (age zero
     * and not hungry) or with random age.
     *
     * @param randomAge If true, the fox will have random age and hunger level.
     */
    public Tiger(boolean randomAge) {
        age = 0;
        alive = true;
        if (randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(RABBIT_FOOD_VALUE);


        }else {
            // Leave age at 0
            foodLevel = RABBIT_FOOD_VALUE;
        }
    }


    /**
     * This is what the tiger does most of the time: it hunts for
     * rabbits or foxes. In the process, it might breed, die of hunger,
     * or die of old age.
     *
     * @param currentField The field currently occupied.
     * @param updatedField The field to transfer to.
     * @param newTigers     A list to add newly born tigers to.
     */
    public void huntRabbitAndFox(Field currentField, Field updatedField, List<Actor> newTigers) {
        incrementAge();
        incrementHunger();

        if (isAlive()) {
            // New tigers are born into adjacent locations.
            int births = breed();
            for (int b = 0; b < births; b++) {
                Tiger newTiger = new Tiger(false);
                newTigers.add(newTiger);
                Location loc = updatedField.randomAdjacentLocation(location);
                newTiger.setLocation(loc);
                updatedField.place(newTiger, loc);
            }

            // Move towards the source of food if found.
            Location newLocation = findFood(currentField, location);
            if (newLocation == null) {  // no food found - move randomly
                newLocation = updatedField.freeAdjacentLocation(location);
            }
            if (newLocation != null) {
                setLocation(newLocation);
                updatedField.place(this, newLocation);
            } else {
                // can neither move nor stay - overcrowding - all locations taken
                alive = false;
            }
        }
    }


    /**
     * Increase the age. This could result in the tiger's death.
     */
    private void incrementAge() {
        age++;
        if (age > MAX_AGE) {
            alive = false;
        }
    }


    /**
     * Make this fox more hungry. This could result in the fox's death.
     */
    private void incrementHunger() {
        foodLevel--;
        if (foodLevel <= 0) {
            alive = false;
        }
    }


    /**
     * Tell the tiger to look for rabbits or foxes adjacent to its current location.
     *
     * @param field    The field in which it must look.
     * @param location Where in the field it is located.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood(Field field, Location location) {
        Iterator adjacentLocations =
                field.adjacentLocations(location);
        while (adjacentLocations.hasNext()) {
            Location where = (Location) adjacentLocations.next();
            Actor actor = field.getActorAt(where);
            boolean isRabbit = actor instanceof Rabbit;
            boolean isFox = actor instanceof  Fox;
            if (isRabbit) {

                Rabbit rabbit = (Rabbit) actor;
                if (rabbit.isAlive()) {
                    rabbit.setEaten();
                    foodLevel = RABBIT_FOOD_VALUE;
                    return where;
                }
            }
            else if (isFox){
                Fox fox = (Fox) actor;
                if (fox.isAlive()) {
                    fox.setEaten();
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
    private int breed() {
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
    public boolean isAlive() {
        return alive;
    }


    /**
     * Tell the tiger that it's dead now :(
     */
    public void setEaten() {
        alive = false;
    }


    /**
     * Set the animal's location.
     *
     * @param row The vertical coordinate of the location.
     * @param col The horizontal coordinate of the location.
     */
    public void setLocation(int row, int col) {
        this.location = new Location(row, col);
    }

    /**
     * Set the fox's location.
     *
     * @param location The fox's location.
     */
    public void setLocation(Location location) {
        this.location = location;
    }


    @Override
    public void makeAction(Field currentField, Field updateField, List<Actor> newActor) {
        huntRabbitAndFox(currentField, updateField, newActor);
    }
}

