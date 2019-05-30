package io.muzoo.ooc.ecosystems.actor;

import java.util.HashMap;
import java.util.Map;

public class ActorFactory {

    // A hashmap storing actor's creation probability
    private static final Map<Class, Double> CREATION_PROBABILITY = new HashMap<Class, Double>() {
        {
            put(Fox.class, 0.3);
            put(Rabbit.class, 0.2);
            put(Tiger.class, 0.4);
            put(Hunter.class, 0.2);
        }

    };


    /**
     * Randomize the actor instance according to CREATION_PROBABILITY
     *
     * @return an randomized Actor
     */
    public static Actor randomizeActor() {
        double random = Math.random();
        for (Map.Entry<Class, Double> entry : CREATION_PROBABILITY.entrySet()) {
            Class type = entry.getKey();
            Double prob = entry.getValue();
            if (random < prob) {
                return createActor(type, true);
            } else {
                random -= prob;
            }
        }
        return null;
    }


    /**
     * @param type actor of type Actor
     * @return An actor
     */
    public static Actor createActor(Class type, boolean randomAge) {

        try {

            Actor a = (Actor) type.newInstance();
            if (randomAge) {
                a.setAgeAtRandom();
                if (a.getClass().getSuperclass() == Predator.class) {
                    ((Predator) a).randomizeFoodValue();
                }

            }
            return a;

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;


    }
}
