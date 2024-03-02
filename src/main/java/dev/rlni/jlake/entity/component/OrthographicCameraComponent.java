package dev.rlni.jlake.entity.component;

import org.joml.Vector3f;

public class OrthographicCameraComponent extends CameraComponent {
    public OrthographicCameraComponent(final Vector3f position, final Vector3f lookTarget, final Vector3f up, final float top, final float bottom, final float left, final float right, final float near, final float far) {
        super(position, lookTarget, up);

        this.setProjection(top, bottom, left, right, near, far);
    }

    public void setProjection(final float top, final float bottom, final float left, final float right, final float near, final float far) {
        mProjection.identity().ortho(left, right, bottom, top, near, far);

        mDirty = true;
    }
}
