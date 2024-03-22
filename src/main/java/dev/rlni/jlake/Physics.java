package dev.rlni.jlake;

import dev.rlni.jlake.entity.component.BoxColliderComponent;
import dev.rlni.jlake.entity.component.CircleColliderComponent;
import dev.rlni.jlake.graphics.Renderer;
import dev.rlni.jlake.graphics.shape.Line;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.Collection;

public class Physics {
    public void update(Collection<CircleColliderComponent> circleColliders, Collection<BoxColliderComponent> boxColliders) {
        this.debugDraw(boxColliders);

        // circle-circle collision
        for (var circleCollider : circleColliders) {
            for (var otherCircleCollider : circleColliders) {
                if (circleCollider == otherCircleCollider) continue;

                final var circleColliderPosition = circleCollider.getPosition();
                final var otherCircleColliderPosition = otherCircleCollider.getPosition();

                final float distance = circleColliderPosition.distance(otherCircleColliderPosition);

                if (distance < circleCollider.getRadius() + otherCircleCollider.getRadius()) {
                    circleCollider.callback(otherCircleCollider);
                }
            }
        }

        // circle-box collision
        for (var circleCollider : circleColliders) {
            for (var boxCollider : boxColliders) {
                Vector2f closestPoint = new Vector2f();
                closestPoint.x = Utils.clamp(circleCollider.getPosition().x, boxCollider.getPosition().x - boxCollider.getSize().x, boxCollider.getPosition().x + boxCollider.getSize().x);
                closestPoint.y = Utils.clamp(circleCollider.getPosition().y, boxCollider.getPosition().y - boxCollider.getSize().y, boxCollider.getPosition().y + boxCollider.getSize().y);

                Vector2f circleToClosest = new Vector2f(closestPoint.sub(circleCollider.getPosition()));

                if (circleToClosest.length() < circleCollider.getRadius()) {
                    circleCollider.callback(boxCollider);
                    boxCollider.callback(circleCollider);
                }
            }
        }

        // box-box collision
        for (var boxCollider : boxColliders) {
            for (var otherBoxCollider : boxColliders) {
                if (boxCollider == otherBoxCollider) continue;

                final var boxColliderPosition = boxCollider.getPosition();
                final var boxColliderSize = boxCollider.getSize();

                final var topLeft = new Vector2f(boxColliderPosition.x - boxColliderSize.x, boxColliderPosition.y + boxColliderSize.y);
                final var topRight = new Vector2f(boxColliderPosition.x + boxColliderSize.x, boxColliderPosition.y + boxColliderSize.y);
                final var bottomLeft = new Vector2f(boxColliderPosition.x - boxColliderSize.x, boxColliderPosition.y - boxColliderSize.y);

                final var otherBoxColliderPosition = otherBoxCollider.getPosition();
                final var otherBoxColliderSize = otherBoxCollider.getSize();

                final var otherTopLeft = new Vector2f(otherBoxColliderPosition.x - otherBoxColliderSize.x, otherBoxColliderPosition.y + otherBoxColliderSize.y);
                final var otherTopRight = new Vector2f(otherBoxColliderPosition.x + otherBoxColliderSize.x, otherBoxColliderPosition.y + otherBoxColliderSize.y);
                final var otherBottomRight = new Vector2f(otherBoxColliderPosition.x + otherBoxColliderSize.x, otherBoxColliderPosition.y - otherBoxColliderSize.y);

                if (
                    (topLeft.x < otherTopRight.x && topRight.x > otherTopLeft.x) &&
                    (topLeft.y > otherBottomRight.y && bottomLeft.y < otherTopRight.y)
                ) {
                    boxCollider.callback(otherBoxCollider);
                }
            }
        }
    }

    private void debugDraw(Collection<BoxColliderComponent> boxColliders) {
        for (var boxCollider : boxColliders) {
            final var boxColliderPosition = boxCollider.getPosition();
            final var boxColliderSize = boxCollider.getSize();

            final var topLeft = new Vector2f(boxColliderPosition.x - boxColliderSize.x, boxColliderPosition.y + boxColliderSize.y);
            final var topRight = new Vector2f(boxColliderPosition.x + boxColliderSize.x, boxColliderPosition.y + boxColliderSize.y);
            final var bottomLeft = new Vector2f(boxColliderPosition.x - boxColliderSize.x, boxColliderPosition.y - boxColliderSize.y);
            final var bottomRight = new Vector2f(boxColliderPosition.x + boxColliderSize.x, boxColliderPosition.y - boxColliderSize.y);

            Renderer.getInstance().drawLine("main", new Line(topLeft, topRight, new Vector4f(1.0f, 0.0f, 0.0f, 1.0f)));
            Renderer.getInstance().drawLine("main", new Line(topRight, bottomRight, new Vector4f(1.0f, 0.0f, 0.0f, 1.0f)));
            Renderer.getInstance().drawLine("main", new Line(bottomRight, bottomLeft, new Vector4f(1.0f, 0.0f, 0.0f, 1.0f)));
            Renderer.getInstance().drawLine("main", new Line(bottomLeft, topLeft, new Vector4f(1.0f, 0.0f, 0.0f, 1.0f)));
            Renderer.getInstance().drawLine("main", new Line(topRight, bottomLeft, new Vector4f(1.0f, 0.0f, 0.0f, 1.0f)));
        }
    }
}
