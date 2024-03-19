package dev.rlni.sandbox.entity.component;

import dev.rlni.jlake.Graphics;
import dev.rlni.jlake.entity.component.EntityComponent;
import dev.rlni.jlake.event.IEvent;
import dev.rlni.jlake.event.WindowResizeEvent;
import dev.rlni.jlake.graphics.*;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.opengl.GL46;

public class BackgroundRendererComponent implements EntityComponent, IDrawable {
    private final Framebuffer mWorkBuffer = new Framebuffer(Graphics.getFramebufferSize());
    private final ShaderProgram mShaderProgram = new ShaderProgram("jlake/shaders/compositor.vsh", "shaders/background.fsh");
    private float mTime = 0.0f;

    public BackgroundRendererComponent() {
        mWorkBuffer.addTextureBuffer(Texture.Format.RGB8, Texture.FilterMode.LINEAR);
        mWorkBuffer.finalizeAttachments();
    }

    @Override
    public void destroy() {
        mWorkBuffer.destroy();
        mShaderProgram.destroy();
    }

    public void update(final float timeStep) {
        mTime += timeStep;
    }

    @Override
    public void draw(RenderInfo renderInfo) {
        mShaderProgram.bind();
        mShaderProgram.setFloat("uTime", mTime);
        mShaderProgram.setVec2("uResolution", new Vector2f(renderInfo.framebuffer().getSize()));
        mWorkBuffer.bind(Framebuffer.BindMode.DRAW);
        renderInfo.framebuffer().getTextureBuffer(0).bind(0);

        GL46.glBindVertexArray(PrimitiveShape.getQuadVAO());
        GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, PrimitiveShape.getQuadEBO());

        GL46.glDrawElements(GL46.GL_TRIANGLES, 6, GL46.GL_UNSIGNED_INT, 0);

        Framebuffer.blit(
            mWorkBuffer, renderInfo.framebuffer(),
            new Vector2i(0, 0), mWorkBuffer.getSize(),
            new Vector2i(0, 0), renderInfo.framebuffer().getSize(),
            Framebuffer.Component.COLOR,
            Texture.FilterMode.LINEAR
        );

        // Remember to re-bind the framebuffer after we are done.
        renderInfo.framebuffer().bind(Framebuffer.BindMode.DRAW);
    }

    public void onEvent(final IEvent event) {
        if (event instanceof WindowResizeEvent e) {
            mWorkBuffer.resize(e.getSize());
        }
    }

    @Override
    public int getLayerHash() {
        return "background".hashCode();
    }
}
