package dev.rlni.sandbox.entity;

import dev.rlni.jlake.entity.Entity;
import dev.rlni.jlake.entity.component.BoxColliderComponent;
import dev.rlni.jlake.entity.component.OrthographicCameraComponent;
import dev.rlni.jlake.entity.component.SpriteComponent;
import dev.rlni.jlake.event.IEvent;
import dev.rlni.jlake.event.KeyPressEvent;
import dev.rlni.jlake.event.KeyReleaseEvent;
import dev.rlni.jlake.event.MouseMoveEvent;
import dev.rlni.jlake.graphics.Texture;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.concurrent.atomic.AtomicReference;

public class PlayerEntity extends Entity {
    public record Data(
        Vector2f position
    ) { }

    private Vector3f mPosition = null;
    private int mMoveDirection = 0;
    private int mScore = 0;

    private static final float cMoveSpeed = 5.0f;

    @Override
    public void init(Object properties) {
        Data data = (Data) properties;
        mPosition = new Vector3f(data.position(), 0.0f);

        SpriteComponent spriteComponent = new SpriteComponent("textures/player.png", "main", Texture.FilterMode.LINEAR);
        spriteComponent.setLayer("main");
        this.addComponent("sprite", spriteComponent);

        BoxColliderComponent boxColliderComponent = new BoxColliderComponent(this, (c)-> {
            mScore++;
            System.out.println("Score: " + mScore);
            return null;
        }, new Vector2f(0.4f, 0.4f));
        this.addComponent("boxCollider", boxColliderComponent);
    }

    @Override
    public void update(final float timeStep) {
        mPosition.x += mMoveDirection * cMoveSpeed * timeStep;

        ((SpriteComponent) this.getComponent("sprite")).setMatrix(new Matrix4f().translate(mPosition).scale(0.4f));
        ((BoxColliderComponent) this.getComponent("boxCollider")).setPosition(new Vector2f(mPosition.x, mPosition.y - 0.09f));
    }

    @Override
    public void onEvent(final IEvent event) {
        if (event instanceof KeyPressEvent e) {
            if (e.getKey() == GLFW.GLFW_KEY_LEFT || e.getKey() == GLFW.GLFW_KEY_A) {
                mMoveDirection = -1;
            } else if (e.getKey() == GLFW.GLFW_KEY_RIGHT || e.getKey() == GLFW.GLFW_KEY_D) {
                mMoveDirection = 1;
            }
        } else if (event instanceof KeyReleaseEvent e) {
            if (e.getKey() == GLFW.GLFW_KEY_LEFT || e.getKey() == GLFW.GLFW_KEY_A) {
                if (mMoveDirection == -1) {
                    mMoveDirection = 0;
                }
            } else if (e.getKey() == GLFW.GLFW_KEY_RIGHT || e.getKey() == GLFW.GLFW_KEY_D) {
                if (mMoveDirection == 1) {
                    mMoveDirection = 0;
                }
            }
        } else if (event instanceof MouseMoveEvent e) {
            AtomicReference<CameraEntity> cameraEntity = new AtomicReference<>(null);
            mScene.getEntities().forEach(entity -> {
                if (entity instanceof CameraEntity) {
                    cameraEntity.set((CameraEntity) entity);
                }
            });

            final Vector2f screenPos = new Vector2f(((OrthographicCameraComponent) cameraEntity.get().getComponent("camera")).unProject(new Vector2f((float) e.getX(), (float) e.getY())));
            mPosition.x = screenPos.x;
        }
    }

    @Override
    public Class<?> getDataStructure() {
        return Data.class;
    }
}
