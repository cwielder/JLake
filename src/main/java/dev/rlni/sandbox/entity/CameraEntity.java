package dev.rlni.sandbox.entity;

import dev.rlni.jlake.Graphics;
import dev.rlni.jlake.entity.Entity;
import dev.rlni.jlake.entity.component.CameraComponent;
import dev.rlni.jlake.entity.component.OrthographicCameraComponent;
import dev.rlni.jlake.event.IEvent;
import dev.rlni.jlake.event.WindowResizeEvent;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class CameraEntity extends Entity {
    public record Data(
        Vector2f position
    ) { }

    private Vector3f mPosition = null;
    private final Vector3f mLookTarget = new Vector3f(0.0f, 0.0f, 1.0f);

    @Override
    public void init(Object properties) {
        Data data = (Data) properties;
        mPosition = new Vector3f(data.position(), 0.0f);

        final float aspectRatio = (float) Graphics.getFramebufferSize().x / Graphics.getFramebufferSize().y;
        CameraComponent cameraComponent = new OrthographicCameraComponent(
            mPosition,
            mLookTarget,
            new Vector3f(0.0f, 1.0f, 0.0f),
            1.0f, -1.0f, aspectRatio, -aspectRatio, -1.0f, 1.0f
        );
        cameraComponent.setLayer("main");
        this.addComponent("camera", cameraComponent);
    }

    @Override
    public void update(final float timeStep) {
        ((CameraComponent) this.getComponent("camera")).setView(mPosition, mLookTarget, new Vector3f(0.0f, 1.0f, 0.0f));
    }

    @Override
    public void onEvent(final IEvent event) {
        if (event instanceof WindowResizeEvent e) {
            final float aspectRatio = (float) e.getSize().x / e.getSize().y;
            ((OrthographicCameraComponent) this.getComponent("camera")).setProjection(
                1.0f, -1.0f, aspectRatio, -aspectRatio, -1.0f, 1.0f
            );
        }
    }

    public OrthographicCameraComponent getCameraComponent() {
        return (OrthographicCameraComponent) this.getComponent("camera");
    }

    @Override
    public Class<?> getDataStructure() {
        return Data.class;
    }
}
