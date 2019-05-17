package io.muzoo.occ.ecosystems.blueprints;

import io.muzoo.ooc.ecosystems.Field;

import java.util.List;


public abstract class Animal {


    abstract public void makeAction(Field currentField, Field updateField, List<Animal> newAnimal);


}
