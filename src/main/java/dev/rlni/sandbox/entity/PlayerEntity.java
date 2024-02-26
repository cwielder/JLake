package dev.rlni.sandbox.entity;

import dev.rlni.jlake.entity.Entity;
import org.joml.Vector2f;

public class PlayerEntity extends Entity {
    public record Data(
        Vector2f position
    ) { }

    public PlayerEntity() {
        System.out.println("PlayerEntity created");
    }

    @Override
    public void destroy() {
        System.out.println("PlayerEntity destroyed");
    }

    @Override
    public void init(Object properties) {
        Data data = (Data) properties;
        System.out.println("PlayerEntity initialized with position: " + data.position().x + ", " + data.position().y);
    }

    @Override
    public void update(final float timeStep) {
        System.out.println("PlayerEntity updated");
    }

    @Override
    public Class<?> getDataStructure() {
        return Data.class;
    }
}
