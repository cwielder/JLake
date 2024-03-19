package dev.rlni.jlake.entity.component;

import dev.rlni.jlake.entity.Entity;
import org.joml.Vector2f;

import java.util.function.Function;

public abstract class ColliderComponent implements EntityComponent {
    private final Vector2f mPosition = new Vector2f();
    private Function<ColliderComponent, Void> mCollisionCallback = null;
    private Entity mParent = null;

    protected ColliderComponent(final Entity parent, final Function<ColliderComponent, Void> collisionCallback) {
        mParent = parent;
        mCollisionCallback = collisionCallback;
    }

    @Override
    public void destroy() {
        mCollisionCallback = null;
        mParent = null;
    }

    public Vector2f getPosition() {
        return mPosition;
    }

    public void setPosition(final Vector2f position) {
        mPosition.set(position);
    }

    public void callback(final ColliderComponent other) {
        mCollisionCallback.apply(other);
    }

    public Entity getParent() {
        return mParent;
    }
}
