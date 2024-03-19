package dev.rlni.jlake.entity.component;

import dev.rlni.jlake.entity.Entity;
import org.joml.Vector2f;

import java.util.function.Function;

public class BoxColliderComponent extends ColliderComponent {
    private final Vector2f mSize = new Vector2f();

    public BoxColliderComponent(final Entity parent, final Function<ColliderComponent, Void> collisionCallback, final Vector2f size) {
        super(parent, collisionCallback);

        mSize.set(size);
    }

    public Vector2f getSize() {
        return mSize;
    }

    public void setSize(final Vector2f size) {
        mSize.set(size);
    }
}
