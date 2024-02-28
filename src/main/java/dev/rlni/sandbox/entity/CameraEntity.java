package dev.rlni.sandbox.entity;

import dev.rlni.jlake.Graphics;
import dev.rlni.jlake.entity.Entity;
import dev.rlni.jlake.entity.component.CameraComponent;
import dev.rlni.jlake.entity.component.OrthographicCameraComponent;
import dev.rlni.jlake.entity.component.SpriteComponent;
import dev.rlni.jlake.graphics.Texture;
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
    public Class<?> getDataStructure() {
        return Data.class;
    }
}
