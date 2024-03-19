package dev.rlni.sandbox.entity;

import dev.rlni.jlake.entity.Entity;
import org.joml.Vector2f;

public class FruitSpawnerEntity extends Entity {
    public record Data() { }

    private float mTimeSinceLastSpawn = 0.0f;

    static final float cTimeBetweenSpawns = 0.5f;

    @Override
    public void update(final float timeStep) {
        mTimeSinceLastSpawn += timeStep;

        if (mTimeSinceLastSpawn > cTimeBetweenSpawns) {
            mTimeSinceLastSpawn = 0.0f;

            Vector2f childPos = new Vector2f(
                (float) Math.random() * 2.0f - 1.0f,
                1.0f
            );

            mScene.spawnEntity(FruitEntity.class, new FruitEntity.Data(childPos));
        }
    }

    @Override
    public Class<?> getDataStructure() {
        return Data.class;
    }
}
