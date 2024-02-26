package dev.rlni.sandbox.core;

import dev.rlni.jlake.Application;
import org.joml.Vector2i;
import org.lwjgl.opengl.GL46;

public class SandboxApplication extends Application {
    public static void main(String[] args) {
        var properties = new Properties(
            new Vector2i(1366/2, 768/2),
            "JLake Sandbox",
            "example_scene"
        );

        new SandboxApplication(properties).run();
    }

    protected SandboxApplication(Properties properties) {
        super(properties);
    }

    @Override
    protected void update(final float timeStep) {
        GL46.glClearColor(1.0f, 0.3f, 0.4f, 1.0f);
        GL46.glClear(GL46.GL_COLOR_BUFFER_BIT);
    }
}
