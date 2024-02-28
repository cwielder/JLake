package dev.rlni.jlake.graphics;

import org.lwjgl.opengl.GL46;

public class PrimitiveShape {
    public static void init() {
        final int stride = 2 * Float.BYTES;

        // Quad

        final float[] quadVertices = {
            -1.0f, -1.0f,  // bottom left
            1.0f, -1.0f,  // bottom right
            1.0f,  1.0f,  // top right
            -1.0f,  1.0f,  // top left
        };

        final int[] quadIndices = {
            0, 1, 2, // first triangle
            2, 3, 0  // second triangle
        };

        sQuadVBO = GL46.glCreateBuffers();
        GL46.glNamedBufferData(sQuadVBO, quadVertices, GL46.GL_STATIC_DRAW);

        sQuadEBO = GL46.glCreateBuffers();
        GL46.glNamedBufferData(sQuadEBO, quadIndices, GL46.GL_STATIC_DRAW);

        sQuadVAO = GL46.glCreateVertexArrays();
        GL46.glVertexArrayVertexBuffer(sQuadVAO, 0, sQuadVBO, 0, stride);
        GL46.glVertexArrayAttribFormat(sQuadVAO, 0, 2, GL46.GL_FLOAT, false, 0);
        GL46.glEnableVertexArrayAttrib(sQuadVAO, 0);
        GL46.glVertexArrayElementBuffer(sQuadVAO, sQuadEBO);
    }

    static private int sQuadVBO = 0, sQuadEBO = 0, sQuadVAO = 0;
    public static int getQuadVBO() {
        return sQuadVBO;
    }

    public static int getQuadEBO() {
        return sQuadEBO;
    }

    public static int getQuadVAO() {
        return sQuadVAO;
    }
}
