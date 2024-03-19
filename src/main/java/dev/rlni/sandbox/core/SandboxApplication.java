package dev.rlni.sandbox.core;

import dev.rlni.jlake.Application;
import dev.rlni.jlake.graphics.Layer;
import dev.rlni.jlake.graphics.LayerStack;
import org.joml.Vector2i;

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

        LayerStack layers = mGraphics.getLayerStack();

        layers.addLayer(new Layer("background"));
        layers.addLayer(new Layer("main"));
    }

    @Override
    protected void update(final float timeStep) {

    }
}
