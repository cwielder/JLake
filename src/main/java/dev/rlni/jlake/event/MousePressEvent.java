package dev.rlni.jlake.event;

import org.lwjgl.glfw.GLFW;

public class MousePressEvent extends MouseEvent {
    public MousePressEvent(int button, int mods) {
        super(button, GLFW.GLFW_PRESS, mods);
    }
}
