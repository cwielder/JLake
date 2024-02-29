package dev.rlni.jlake.event;

import org.joml.Vector2i;

public class WindowResizeEvent implements IEvent {
    private final Vector2i mSize;

    public WindowResizeEvent(Vector2i size) {
        mSize = size;
    }

    public Vector2i getSize() {
        return mSize;
    }
}
