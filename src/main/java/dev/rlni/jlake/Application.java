package dev.rlni.jlake;

import org.joml.Vector2i;

public abstract class Application {
    public record Properties(
        Vector2i windowSize,
        String windowTitle
    ) { }

    protected Graphics mGraphics;

    protected Application(final Properties properties) {
        mGraphics = new Graphics(
            new Graphics.Properties(
                properties.windowSize,
                properties.windowTitle
            )
        );
    }

    public void run() {
        do {
            this.update(mGraphics.getTimeStep());
        } while (mGraphics.update());

        mGraphics.destroy();
    }

    abstract protected void update(final float timeStep);
}
