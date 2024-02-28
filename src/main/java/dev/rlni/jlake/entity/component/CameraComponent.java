package dev.rlni.jlake.entity.component;

import dev.rlni.jlake.Graphics;
import org.joml.*;

public abstract class CameraComponent extends EntityComponent {
    protected Matrix4f mProjection = new Matrix4f();
    protected Matrix4f mView = new Matrix4f();
    protected Matrix4f mViewProjection = new Matrix4f();
    protected Vector3f mPosition = new Vector3f();
    protected int mLayerHash = 0;
    protected boolean mDirty = true;

    protected CameraComponent(final Vector3f position, final Vector3f lookTarget, final Vector3f up) {
        this.setView(position, lookTarget, up);
    }

    public void setView(final Vector3f position, final Vector3f lookTarget, final Vector3f up) {
        mView.setLookAt(position, lookTarget, up);
        mPosition = position;
        mDirty = true;
    }

    public Matrix4f getProjection() {
        return mProjection;
    }

    public Matrix4f getView() {
        return mView;
    }

    public Matrix4f getViewProjection() {
        if (mDirty) {
            mDirty = false;
            mViewProjection.set(mProjection);
            mViewProjection.mul(mView);
        }

        return mViewProjection;
    }

    public Vector3f getPosition() {
        return mPosition;
    }

    public void setLayer(final String name) {
        mLayerHash = name.hashCode();
    }

    public int getLayerHash() {
        return mLayerHash;
    }

    public Vector2f unProject(final Vector2f screenPosition) {
        final Vector2i windowSize = Graphics.getFramebufferSize();
        final int[] viewport = new int[4];
        viewport[0] = 0;
        viewport[1] = 0;
        viewport[2] = windowSize.x;
        viewport[3] = windowSize.y;
        final Vector4f result = new Vector4f();
        mViewProjection.unproject(
            screenPosition.x,
            screenPosition.y,
            0.0f,
            viewport,
            result
        );

        return new Vector2f(result.x, result.y);
    }
}
