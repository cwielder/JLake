package dev.rlni.jlake;

import dev.rlni.jlake.entity.Entity;
import dev.rlni.jlake.entity.component.BoxColliderComponent;
import dev.rlni.jlake.entity.component.CameraComponent;
import dev.rlni.jlake.entity.component.CircleColliderComponent;
import dev.rlni.jlake.entity.component.EntityComponent;
import dev.rlni.jlake.event.IEvent;
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
    protected Physics mPhysics;

    private static final ArrayList<IEvent> sEventQueue = new ArrayList<>();

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
        mPhysics = new Physics(

        );

    }

    public static void raiseEvent(IEvent event) {
        sEventQueue.add(event);
    }

    public void run() {
        this.intermoduleDataTransfer();

        do {
            final float timeStep = mGraphics.getTimeStep();

            this.update(timeStep);
            this.handleEvents();
            mScene.update(timeStep);

            this.intermoduleDataTransfer();
        } while (mGraphics.update());

        mScene.destroy();
        mGraphics.destroy();
    }

    protected void update(final float timeStep) {}

    private void onEvent(final IEvent event) {
        // handle scene switch
    }

    private void handleEvents() {
        for (final IEvent event : sEventQueue) {
            this.onEvent(event);
            mScene.onEvent(event);
            mGraphics.onEvent(event);
        }
        sEventQueue.clear();
    }

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

        // Link physics and scene
        ArrayList<BoxColliderComponent> boxColliders = new ArrayList<>();
        ArrayList<CircleColliderComponent> circleColliders = new ArrayList<>();
        for (Entity entity : mScene.getEntities()) {
            ArrayList<EntityComponent> components = entity.getComponents();
            for (EntityComponent component : components) {
                if (component instanceof BoxColliderComponent) {
                    boxColliders.add((BoxColliderComponent) component);
                } else if (component instanceof CircleColliderComponent) {
                    circleColliders.add((CircleColliderComponent) component);
                }
            }
        }
        mPhysics.update(circleColliders, boxColliders);
    }
}
