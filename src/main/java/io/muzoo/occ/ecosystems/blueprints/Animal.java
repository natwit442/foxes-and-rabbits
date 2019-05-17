package io.muzoo.occ.ecosystems.blueprints;

import io.muzoo.ooc.ecosystems.Field;

import java.util.List;


public interface Animal {

    boolean isAlive();

    void makeAction(Field currentField, Field updateField, List<Animal> newAnimal);


}
