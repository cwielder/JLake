package dev.rlni.jlake.event;

import org.lwjgl.glfw.GLFW;

public class KeyPressEvent extends KeyEvent {
    public KeyPressEvent(int key, int scancode, int mods) {
        super(key, scancode, GLFW.GLFW_PRESS, mods);
    }
}
