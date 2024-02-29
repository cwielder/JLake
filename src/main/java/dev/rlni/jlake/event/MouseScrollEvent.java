package dev.rlni.jlake.event;

import org.joml.Vector2d;

public class MouseScrollEvent implements IEvent {
    private final double mXOffset;
    private final double mYOffset;

    public MouseScrollEvent(double xOffset, double yOffset) {
        mXOffset = xOffset;
        mYOffset = yOffset;
    }

    public Vector2d getOffset() {
        return new Vector2d(mXOffset, mYOffset);
    }
}
