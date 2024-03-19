package dev.rlni.jlake;

import dev.rlni.jlake.entity.component.BoxColliderComponent;
import dev.rlni.jlake.entity.component.CircleColliderComponent;
import org.joml.Vector2f;

import java.util.Collection;

public class Physics {
    public void update(final float timeStep, final Collection<CircleColliderComponent> circleColliders, final Collection<BoxColliderComponent> boxColliders) {
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
                final var circleColliderPosition = circleCollider.getPosition();
                final var boxColliderPosition = boxCollider.getPosition();

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
                final var otherBoxColliderPosition = otherBoxCollider.getPosition();

                final var boxColliderSize = boxCollider.getSize();
                final var otherBoxColliderSize = otherBoxCollider.getSize();

                final var boxColliderMin = new Vector2f(boxColliderPosition.sub(boxColliderSize));
                final var boxColliderMax = new Vector2f(boxColliderPosition.add(boxColliderSize));

                final var otherBoxColliderMin = new Vector2f(otherBoxColliderPosition.sub(otherBoxColliderSize));
                final var otherBoxColliderMax = new Vector2f(otherBoxColliderPosition.add(otherBoxColliderSize));

                if (
                    boxColliderMin.x < otherBoxColliderMax.x && boxColliderMax.x > otherBoxColliderMin.x &&
                    boxColliderMin.y < otherBoxColliderMax.y && boxColliderMax.y > otherBoxColliderMin.y
                ) {
                    boxCollider.callback(otherBoxCollider);
                }
            }
        }
    }
}
