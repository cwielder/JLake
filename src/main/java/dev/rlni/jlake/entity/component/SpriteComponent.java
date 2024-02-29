package dev.rlni.jlake.entity.component;

import dev.rlni.jlake.graphics.IDrawable;
import dev.rlni.jlake.graphics.PrimitiveShape;
import dev.rlni.jlake.graphics.ShaderProgram;
import dev.rlni.jlake.graphics.Texture;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL46;

public class SpriteComponent implements EntityComponent, IDrawable {
    private int mLayerHash = 0;
    private Matrix4f mMatrix = new Matrix4f();
    private final Texture mTexture;
    private final ShaderProgram mShaderProgram;

    public SpriteComponent(final String texturePath, final String layer, final Texture.FilterMode filterMode) {
        mTexture = new Texture(texturePath, filterMode);
        mShaderProgram = new ShaderProgram("jlake/shaders/sprite.vsh", "jlake/shaders/sprite.fsh");
        this.setLayer(layer);
    }

    public void destroy() {
        mTexture.destroy();
        mShaderProgram.destroy();
    }

    public void setMatrix(Matrix4f matrix) {
        mMatrix = matrix;
    }
    public Matrix4f getMatrix() {
        return mMatrix;
    }

    public void setLayer(final String name) {
        mLayerHash = name.hashCode();
    }

    @Override
    public void draw(final RenderInfo renderInfo) {
        mTexture.bind(0);

        mShaderProgram.bind();
        mShaderProgram.setMat4(0, mMatrix);
        mShaderProgram.setMat4(1, renderInfo.camera().getViewProjection());

        GL46.glBindVertexArray(PrimitiveShape.getQuadVAO());
        GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, PrimitiveShape.getQuadEBO());
        GL46.glDrawElements(GL46.GL_TRIANGLES, 6, GL46.GL_UNSIGNED_INT, 0);
    }

    @Override
    public int getLayerHash() {
        return mLayerHash;
    }
}
