package dev.rlni.jlake;

import dev.rlni.jlake.graphics.LayerStack;
import dev.rlni.jlake.graphics.PrimitiveShape;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryUtil;

public final class Graphics {
    public record Properties(
        Vector2i windowSize,
        String windowTitle
    ) { }

    private float mTimeStep = 0.0f, mLastFrameTime = 0.0f;
    private final LayerStack mLayerStack;

    public float getTimeStep() {
        return mTimeStep;
    }

    public Graphics(final Properties properties) {
        boolean success = GLFW.glfwInit();
        assert success : "Failed to initialize GLFW";

        GLFW.glfwWindowHint(GLFW.GLFW_CLIENT_API, GLFW.GLFW_OPENGL_API);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 6);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);

        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_DEBUG_CONTEXT, GLFW.GLFW_TRUE);

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

        GL46.glEnable(GL46.GL_DEBUG_OUTPUT);
        GL46.glEnable(GL46.GL_DEBUG_OUTPUT_SYNCHRONOUS);
        GL46.glDebugMessageCallback((source, type, id, severity, length, message, userParam) -> {
            String messageString = MemoryUtil.memUTF8(message);

            if (severity == GL46.GL_DEBUG_SEVERITY_HIGH) {
                throw new RuntimeException("OpenGL Error: " + messageString);
            } else {
                System.out.println("OpenGL Message: " + messageString);
            }
        }, 0);

        PrimitiveShape.init();
        mLayerStack = new LayerStack(properties.windowSize);
    }

    public void destroy() {
        mLayerStack.destroy();

        GLFW.glfwDestroyWindow(GLFW.glfwGetCurrentContext());
        GLFW.glfwTerminate();
    }

    public boolean update() {
        long window = GLFW.glfwGetCurrentContext();

        GLFW.glfwPollEvents();
        GLFW.glfwSwapBuffers(window);

        final float time = (float) GLFW.glfwGetTime();
        final float frameTime = time - mLastFrameTime;
        mTimeStep = Math.min(frameTime, 0.0333f); // TODO: ??
        mLastFrameTime = time;

        mLayerStack.drawLayers();

        return !GLFW.glfwWindowShouldClose(window);
    }

    public LayerStack getLayerStack() {
        return mLayerStack;
    }

    public static Vector2i getFramebufferSize() {
        int[] width = new int[1];
        int[] height = new int[1];
        GLFW.glfwGetFramebufferSize(GLFW.glfwGetCurrentContext(), width, height);
        return new Vector2i(width[0], height[0]);
    }
}
