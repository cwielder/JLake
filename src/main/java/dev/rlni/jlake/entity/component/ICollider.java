package dev.rlni.jlake.entity.component;

import org.joml.Vector2f;

public interface ICollider {
    Vector2f getPosition();
    void setPosition(final Vector2f position);
}
