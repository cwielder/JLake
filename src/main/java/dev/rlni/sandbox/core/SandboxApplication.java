package dev.rlni.sandbox.core;

import dev.rlni.jlake.Application;
import dev.rlni.jlake.graphics.Layer;
import org.joml.Vector2i;
import org.lwjgl.opengl.GL46;

public class SandboxApplication extends Application {
    public static void main(String[] args) {
        var properties = new Properties(
            new Vector2i(1366, 768),
            "JLake Sandbox",
            "scenes/example_scene.json"
        );

        new SandboxApplication(properties).run();
    }

    protected SandboxApplication(Properties properties) {
        super(properties);

        mGraphics.getLayerStack().addLayer(new Layer("main"));
    }

    @Override
    protected void update(final float timeStep) {

    }
}
