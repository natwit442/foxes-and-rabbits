package io.muzoo.ooc.ecosystems.actor;

import io.muzoo.ooc.ecosystems.field.Field;
import io.muzoo.ooc.ecosystems.location.Location;
import io.muzoo.ooc.ecosystems.simulator.Simulator;

import java.util.List;

public abstract class Predator extends Animal {

    public Predator() {
        super();
    }

    protected abstract int getFoodValue() ;
    public abstract void randomizeFoodValue();



    @Override
    public void makeAction(Field updatedField, Field currentField, List<Actor> newActor) {
        incrementAge();
        incrementHunger();
        hunt(updatedField, currentField, newActor);
    }

    protected abstract void incrementHunger();


    public void hunt(Field updatedField, Field currentField, List<Actor> newPredators) {


        if (isAlive()) {
            giveBirth(updatedField, newPredators);

            Location newLocation = findFood(currentField, getLocation());
            if (newLocation == null) {  // no food found - move randomly
                newLocation = updatedField.freeAdjacentLocation(getLocation());
            }
            if (newLocation != null) {
                setLocation(newLocation);
                updatedField.place(this, newLocation);
            } else {
                // can neither move nor stay - overcrowding - all locations taken
                setDead();
            }
        }
    }


    // Each predator has different kind of food
    protected abstract Location findFood(Field currentField, Location location);


}
