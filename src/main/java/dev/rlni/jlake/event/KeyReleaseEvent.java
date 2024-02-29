package dev.rlni.jlake.event;

import org.lwjgl.glfw.GLFW;

public class KeyReleaseEvent extends KeyEvent {
    public KeyReleaseEvent(int key, int scancode, int mods) {
        super(key, scancode, GLFW.GLFW_RELEASE, mods);
    }
}
