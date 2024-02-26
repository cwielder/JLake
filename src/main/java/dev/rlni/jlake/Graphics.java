package dev.rlni.jlake;

import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;

public class Graphics {
    public record Properties(
        Vector2i windowSize,
        String windowTitle
    ) { }

    public Graphics(final Properties properties) {
        boolean success = GLFW.glfwInit();
        assert success : "Failed to initialize GLFW";

        GLFW.glfwWindowHint(GLFW.GLFW_CLIENT_API, GLFW.GLFW_OPENGL_API);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 6);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);

        long window = GLFW.glfwCreateWindow(
            properties.windowSize.x,
            properties.windowSize.y,
            properties.windowTitle,
            0,
            0
        );
        assert window != 0 : "Failed to create window";

        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities();
    }

    public void destroy() {
        GLFW.glfwDestroyWindow(GLFW.glfwGetCurrentContext());
        GLFW.glfwTerminate();
    }

    public boolean update() {
        long window = GLFW.glfwGetCurrentContext();

        GLFW.glfwPollEvents();
        GLFW.glfwSwapBuffers(window);

        return !GLFW.glfwWindowShouldClose(window);
    }
}
