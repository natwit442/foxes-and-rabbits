package io.muzoo.ooc.ecosystems;

import java.util.List;

public abstract class Predator extends Animal {


    public Predator(Location location, Field field) {
        super(location, field);
    }

    /**
     * This is what the tiger does most of the time: it hunts for
     * rabbits or foxes. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param updatedField The field to transfer to.
     */
    public void hunt(Field updatedField, List<Actor> newPredators) {


        if (isAlive()) {

            giveBirth(updatedField , newPredators);


            // Move towards the source of food if found.
            Location newLocation = findFood(getField(), getLocation());
            if (newLocation == null) {  // no food found - move randomly
                newLocation = updatedField.freeAdjacentLocation(getLocation());
            }
            if (newLocation != null) {
                setLocation(newLocation);
                updatedField.place(this, newLocation);
            } else {
                // can neither move nor stay - overcrowding - all locations taken
                setAlive(false);
            }
        }
    }


    public abstract void giveBirth(Field updatedField, List<Actor> newPredators);


    // Each predator has different kind of food
    protected abstract Location findFood(Field field, Location location);




}
