package dev.rlni.sandbox.entity;

import dev.rlni.jlake.entity.Entity;
import dev.rlni.jlake.entity.component.BoxColliderComponent;
import dev.rlni.jlake.entity.component.SpriteComponent;
import dev.rlni.jlake.graphics.Texture;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class FruitEntity extends Entity {
    public record Data(
        Vector2f position
    ) { }

    private Vector3f mPosition = null;

    private static final float cFallSpeed = 1.0f;
    private static final float cSize = 0.2f;

    @Override
    public void init(Object properties) {
        Data data = (Data) properties;
        mPosition = new Vector3f(data.position(), 0.0f);

        SpriteComponent spriteComponent = new SpriteComponent("textures/apple.png", "main", Texture.FilterMode.LINEAR);
        spriteComponent.setLayer("main");
        this.addComponent("sprite", spriteComponent);

        BoxColliderComponent circleColliderComponent = new BoxColliderComponent(this, (otherCollider)-> {
            if (otherCollider.getParent() instanceof PlayerEntity) {
                this.setAlive(false);
            }
            return null;
        }, new Vector2f(cSize - 0.05f, cSize - 0.05f));
        this.addComponent("collider", circleColliderComponent);
    }

    @Override
    public void update(float timeStep) {
        ((SpriteComponent) this.getComponent("sprite")).setMatrix(new Matrix4f().translate(mPosition).scale(cSize));
        ((BoxColliderComponent) this.getComponent("collider")).setPosition(new Vector2f(mPosition.x, mPosition.y));

        mPosition.y -= cFallSpeed * timeStep;

        if (mPosition.y < -1.0f) {
            this.setAlive(false);
            System.out.println("Missed fruit");
        }
    }

    @Override
    public Class<?> getDataStructure() {
        return Data.class;
    }
}

