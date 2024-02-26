package dev.rlni.jlake;

import org.joml.Vector2i;

public abstract class Application {
    public record Properties(
        Vector2i windowSize,
        String windowTitle,
        String initialScene
    ) { }

    protected Graphics mGraphics;
    protected Scene mScene;

    protected Application(final Properties properties) {
        mGraphics = new Graphics(
            new Graphics.Properties(
                properties.windowSize,
                properties.windowTitle
            )
        );
        mScene = new Scene(
            properties.initialScene
        );
    }

    public void run() {
        do {
            final float timeStep = mGraphics.getTimeStep();

            this.update(timeStep);
            mScene.update(timeStep);
        } while (mGraphics.update());

        mScene.destroy();
        mGraphics.destroy();
    }

    protected void update(final float timeStep) {}
}
