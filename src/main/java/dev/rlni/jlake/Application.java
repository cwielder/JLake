package dev.rlni.jlake;

import dev.rlni.jlake.entity.Entity;
import dev.rlni.jlake.entity.component.CameraComponent;
import dev.rlni.jlake.entity.component.EntityComponent;
import dev.rlni.jlake.graphics.IDrawable;
import org.joml.Vector2i;

import java.util.ArrayList;

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
        this.intermoduleDataTransfer();

        do {
            final float timeStep = mGraphics.getTimeStep();

            this.update(timeStep);
            mScene.update(timeStep);

            this.intermoduleDataTransfer();
        } while (mGraphics.update());

        mScene.destroy();
        mGraphics.destroy();
    }

    protected void update(final float timeStep) {}

    private void intermoduleDataTransfer() {
        // Link graphics and scene

        for (Entity entity : mScene.getEntities()) {
            ArrayList<EntityComponent> drawableComponents = entity.getComponents(IDrawable.class);
            for (EntityComponent it : drawableComponents) {
                IDrawable drawable = (IDrawable) it;
                mGraphics.getLayerStack().pushDrawable(drawable);
            }

            ArrayList<EntityComponent> cameraComponents = entity.getComponents(CameraComponent.class);
            for (EntityComponent it : cameraComponents) {
                CameraComponent camera = (CameraComponent) it;
                mGraphics.getLayerStack().getLayer(camera.getLayerHash()).setCamera(camera);
            }
        }
    }
}
