package dev.rlni.jlake.graphics;

import dev.rlni.jlake.entity.component.CameraComponent;
import org.joml.Vector2i;

import java.util.ArrayList;

public class Layer {
    protected CameraComponent mCamera = null;
    protected final ArrayList<IDrawable> mDrawables = new ArrayList<>();
    protected GraphicsContext mGraphicsContext = new GraphicsContext();
    protected final String mName;

    public Layer(final String name) {
        mGraphicsContext
            .depth(GraphicsContext.DepthFunction.LESS_OR_EQUAL, true);

        mName = name;
    }

    public void destroy() {
        mDrawables.clear();
        mGraphicsContext = null;
        mCamera = null;
    }

    public void draw(final IDrawable.RenderInfo renderInfo) {
        mGraphicsContext.apply();

        for (IDrawable drawable : mDrawables) {
            drawable.draw(renderInfo);
        }
    }

    public void resize(final Vector2i size) { }

    public void setCamera(final CameraComponent camera) {
        mCamera = camera;
    }
    public CameraComponent getCamera() {
        return mCamera;
    }

    public String getName() {
        return mName;
    }
}
