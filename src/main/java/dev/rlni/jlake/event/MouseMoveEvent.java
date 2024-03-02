package dev.rlni.jlake.event;

import org.joml.Vector2d;

public class MouseMoveEvent implements IEvent {
    private final double mX;
    private final double mY;

    public MouseMoveEvent(double x, double y) {
        mX = x;
        mY = y;
    }

    public double getX() {
        return mX;
    }

    public double getY() {
        return mY;
    }
}
