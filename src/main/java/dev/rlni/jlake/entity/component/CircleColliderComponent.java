package dev.rlni.jlake.entity.component;

import dev.rlni.jlake.entity.Entity;

import java.util.function.Function;

public class CircleColliderComponent extends ColliderComponent {
    private float mRadius;

    public CircleColliderComponent(final Entity parent, final Function<ColliderComponent, Void> collisionCallback, final float radius) {
        super(parent, collisionCallback);

        mRadius = radius;
    }

    public float getRadius() {
        return mRadius;
    }

    public void setRadius(final float radius) {
        mRadius = radius;
    }
}
