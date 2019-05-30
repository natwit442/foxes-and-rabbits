package io.muzoo.ooc.ecosystems.actor;


import io.muzoo.ooc.ecosystems.field.Field;
import io.muzoo.ooc.ecosystems.location.Location;


import java.util.List;
import java.util.Random;

public abstract class Actor {
    private static Random random = new Random();
    private boolean alive;
    private Location location;
    private int age;

    public Actor() {
        alive = true;
        age = 0;
    }


    /**
     * Increase the age. This could result in the fox's death.
     */
    protected void incrementAge() {
        setAge(getAge() + 1);
        if (getAge() > getMaxAge()) {
            setDead();
        }
    }

    protected abstract double getBreedingProb();

    /**
     * Generate a number representing the number of births,
     * if it can breed.
     *
     * @return The number of births (may be zero).
     */
    protected int breed() {
        int births = 0;
        if (canBreed() && random.nextDouble() <= getBreedingProb()) {
            births = random.nextInt(getMaxLitterSize()) + 1;
        }
        return births;
    }

    protected abstract int getMaxLitterSize();

    protected abstract int getMaxAge();

    protected boolean canBreed() {
        return age >= getBreedingAge();
    }

    protected abstract int getBreedingAge();

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public abstract void giveBirth(Field updateField, List<Actor> newActor);


    public void setAgeAtRandom() {
        setAge(random.nextInt(getMaxAge()));

    }



    public Location getLocation() {
        return location;
    }

    /**
     * Set the fox's location.
     *
     * @param location The fox's location.
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    protected void setDead() {
        setAlive(false);
    }

    // This is the bridge for the simulator to call
    public abstract void makeAction(Field updatedField, Field currentField, List<Actor> newActor);

}
