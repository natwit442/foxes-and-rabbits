package io.muzoo.ooc.ecosystems;

import io.muzoo.occ.ecosystems.blueprints.Animal;

import java.util.List;

public abstract class Predator extends Animal {

    /**
     * This is what the tiger does most of the time: it hunts for
     * rabbits or foxes. In the process, it might breed, die of hunger,
     * or die of old age.
     *
     * @param currentField The field currently occupied.
     * @param updatedField The field to transfer to.
     */
    public void hunt(Field currentField, Field updatedField, List<Actor> newPredators) {


        if (isAlive()) {
            // New tigers are born into adjacent locations.
            int births = breed();
            for (int b = 0; b < births; b++) {

                try {
                    Predator newPredator = getClass().newInstance();
                    newPredators.add(newPredator);
                    Location loc = updatedField.randomAdjacentLocation(getLocation());
                    newPredator.setLocation(loc);
                    updatedField.place(newPredator, loc);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            // Move towards the source of food if found.
            Location newLocation = findFood(currentField, getLocation());
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

    protected abstract int breed();

    // Each predator has different kind of food
    protected abstract Location findFood(Field field, Location location);

}
