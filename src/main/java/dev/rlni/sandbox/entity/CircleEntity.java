package dev.rlni.sandbox.entity;

import dev.rlni.jlake.entity.Entity;
import dev.rlni.jlake.entity.component.CircleColliderComponent;
import dev.rlni.jlake.entity.component.OrthographicCameraComponent;
import dev.rlni.jlake.entity.component.SpriteComponent;
import dev.rlni.jlake.event.IEvent;
import dev.rlni.jlake.event.MouseMoveEvent;
import dev.rlni.jlake.graphics.Texture;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.concurrent.atomic.AtomicReference;

public class CircleEntity extends Entity {
    public record Data(
        Vector2f position
    ) { }

    private Vector3f mPosition = null;

    @Override
    public void init(Object properties) {
        Data data = (Data) properties;
        mPosition = new Vector3f(data.position(), 0.0f);

        SpriteComponent spriteComponent = new SpriteComponent("textures/circle.png", "main", Texture.FilterMode.LINEAR);
        spriteComponent.setLayer("main");
        this.addComponent("sprite", spriteComponent);

        CircleColliderComponent circleColliderComponent = new CircleColliderComponent(this, (c)-> null, 0.4f);
        this.addComponent("circleCollider", circleColliderComponent);
    }

    @Override
    public void update(final float timeStep) {
        ((SpriteComponent) this.getComponent("sprite")).setMatrix(new Matrix4f().translate(mPosition).scale(0.4f));
        ((CircleColliderComponent) this.getComponent("circleCollider")).setPosition(new Vector2f(mPosition.x, mPosition.y));
    }

    @Override
    public void onEvent(final IEvent event) {
        if (event instanceof MouseMoveEvent e) {
            AtomicReference<CameraEntity> cameraEntity = new AtomicReference<>(null);
            mScene.getEntities().forEach(entity -> {
                if (entity instanceof CameraEntity) {
                    cameraEntity.set((CameraEntity) entity);
                }
            });

            final Vector2f screenPos = new Vector2f(((OrthographicCameraComponent) cameraEntity.get().getComponent("camera")).unProject(new Vector2f((float) e.getX(), (float) e.getY())));
            //mPosition.x = screenPos.x;
            //mPosition.y = -screenPos.y;
        }
    }

    @Override
    public Class<?> getDataStructure() {
        return Data.class;
    }
}

