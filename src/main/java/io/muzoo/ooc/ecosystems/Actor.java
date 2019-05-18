package io.muzoo.ooc.ecosystems;

import java.util.List;

public abstract class Actor {




    private boolean alive;

    private Location location;


    public Actor() {
        alive = true;


    }

    public Location getLocation() {
        return location;
    }


    public void setAlive(boolean alive) {
        this.alive = alive;
    }




    public boolean isAlive() {
        return alive;
    }



    protected void setDead() {
        setAlive(false);
    }
    /**
     * Set the animal's location.
     *
     * @param row The vertical coordinate of the location.
     * @param col The horizontal coordinate of the location.
     */
    protected void setLocation(int row, int col) {
        this.location = new Location(row, col);
    }

    /**
     * Set the fox's location.
     *
     * @param location The fox's location.
     *
     *
     */
    protected void setLocation(Location location) {
        this.location = location;
    }


    // This is the bridge for the simulator to call
    protected abstract void makeAction(Field currentField, Field updateField, List<Actor> newActor);

}
