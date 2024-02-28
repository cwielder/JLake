package dev.rlni.jlake.graphics;

import org.lwjgl.opengl.GL46;

import java.util.HashMap;
import java.util.Map;

public final class LayerStack {
    private final Map<Integer, Layer> mLayers = new HashMap<>();

    public void destroy() {
        this.clearLayers();
    }

    public <T extends Layer> T addLayer(final T layer) {
        mLayers.put(layer.getName().hashCode(), layer);
        return layer;
    }

    public <T extends Layer> T getLayer(final String name) {
        return this.getLayer(name.hashCode());
    }

    public <T extends Layer> T getLayer(final int hash) {
        return (T) mLayers.get(hash);
    }

    public void removeLayer(final String name) {
        mLayers.remove(name.hashCode());
    }

    public void clearLayers() {
        for (Layer layer : mLayers.values()) {
            layer.destroy();
        }

        mLayers.clear();
    }

    public void pushDrawable(final IDrawable drawable) {
        this.getLayer(drawable.getLayerHash()).mDrawables.add(drawable);
    }

    public void drawLayers() {
        GL46.glClearColor(1.0f, 0.3f, 0.3f, 1.0f);
        GL46.glClear(GL46.GL_COLOR_BUFFER_BIT | GL46.GL_DEPTH_BUFFER_BIT);

        for (Layer layer : mLayers.values()) {
            IDrawable.RenderInfo renderInfo = new IDrawable.RenderInfo(
                layer.getCamera()
            );

            layer.draw(renderInfo);
            layer.mDrawables.clear();
        }
    }
}
