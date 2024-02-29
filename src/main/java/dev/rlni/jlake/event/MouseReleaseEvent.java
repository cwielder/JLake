package dev.rlni.jlake.event;

import org.lwjgl.glfw.GLFW;

public class MouseReleaseEvent extends MouseEvent {
    public MouseReleaseEvent(int button, int mods) {
        super(button, GLFW.GLFW_RELEASE, mods);
    }
}
