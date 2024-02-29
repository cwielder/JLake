package dev.rlni.jlake.event;

import org.lwjgl.glfw.GLFW;

public class KeyRepeatEvent extends KeyEvent {
    public KeyRepeatEvent(int key, int scancode, int mods) {
        super(key, scancode, GLFW.GLFW_REPEAT, mods);
    }
}
