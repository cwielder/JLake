package dev.rlni.jlake.graphics;

import org.joml.Vector2i;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL46;

import java.util.HashMap;
import java.util.Map;

public final class LayerStack {
    private final Map<Integer, Layer> mLayers = new HashMap<>();
    private final Framebuffer mFramebuffer;
    private final ShaderProgram mCompositorShader = new ShaderProgram("jlake/shaders/compositor.vsh", "jlake/shaders/compositor.fsh");
    private final GraphicsContext mGraphicsContext = new GraphicsContext();

    public LayerStack(final Vector2i size) {
        mFramebuffer = new Framebuffer(size);
        mFramebuffer.addTextureBuffer(Texture.Format.RGBA16F, Texture.FilterMode.LINEAR);
        mFramebuffer.finalizeAttachments();

        mGraphicsContext
            .blend(false)
            .cull(false)
            .depth(false);
    }

    public void destroy() {
        this.clearLayers();
        mFramebuffer.destroy();
        mCompositorShader.destroy();
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
        Framebuffer.getBackbuffer().clear(new Vector4f(0.0f, 0.0f, 0.0f, 1.0f), Framebuffer.Component.COLOR, 0);
        Framebuffer.getBackbuffer().clear(new Vector4f(1.0f, 1.0f, 1.0f, 1.0f), Framebuffer.Component.DEPTH, 0);

        mFramebuffer.bind(Framebuffer.BindMode.DRAW);
        mFramebuffer.clear(new Vector4f(1.0f, 0.3f, 0.3f, 0.3f), Framebuffer.Component.COLOR, 0);
        mFramebuffer.clear(new Vector4f(1.0f, 1.0f, 1.0f, 1.0f), Framebuffer.Component.DEPTH, 0);

        for (Layer layer : mLayers.values()) {
            final IDrawable.RenderInfo renderInfo = new IDrawable.RenderInfo(
                mFramebuffer,
                layer.getCamera()
            );

            layer.draw(renderInfo);
            layer.mDrawables.clear();
        }

        mGraphicsContext.apply();

        Framebuffer.getBackbuffer().bind(Framebuffer.BindMode.DRAW);
        mCompositorShader.bind();
        mFramebuffer.getTextureBuffer(0).bind(0);
        GL46.glBindVertexArray(PrimitiveShape.getQuadVAO());
        GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, PrimitiveShape.getQuadEBO());
        GL46.glDrawElements(GL46.GL_TRIANGLES, 6, GL46.GL_UNSIGNED_INT, 0);
    }
}
