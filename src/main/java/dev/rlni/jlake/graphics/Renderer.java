package dev.rlni.jlake.graphics;

import dev.rlni.jlake.Application;
import dev.rlni.jlake.graphics.shape.Line;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL46;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public final class Renderer {
    private record LineQueueEntry(
        int layerHash,
        Line shape
    ) { }

    private final ShaderProgram mLineShader;
    private final int mLineVAO, mLineVBO;
    private final Map<Integer, ArrayList<LineQueueEntry>> mLines = new TreeMap<>();

    public Renderer() {
        // Line

        final float[] lineVertices = {
            0.0f, 0.0f,
            1.0f, 1.0f
        };

        mLineVBO = GL46.glCreateBuffers();
        GL46.glNamedBufferData(mLineVBO, lineVertices, GL46.GL_STATIC_DRAW);

        mLineVAO = GL46.glCreateVertexArrays();
        GL46.glVertexArrayVertexBuffer(mLineVAO, 0, mLineVBO, 0, 2 * Float.BYTES);
        GL46.glVertexArrayAttribFormat(mLineVAO, 0, 2, GL46.GL_FLOAT, false, 0);
        GL46.glEnableVertexArrayAttrib(mLineVAO, 0);

        mLineShader = new ShaderProgram("jlake/shaders/line.vsh", "jlake/shaders/line.fsh");
    }

    public void destroy() {
        // Line
        mLineShader.destroy();
        GL46.glDeleteVertexArrays(mLineVAO);
        GL46.glDeleteBuffers(mLineVBO);
    }

    public static Renderer getInstance() {
        return Application.getInstance().getGraphics().getRenderer();
    }

    public void drawLine(String layer, Line shape) {
        mLines.computeIfAbsent(layer.hashCode(), k -> new ArrayList<>()).add(new LineQueueEntry(layer.hashCode(), shape));
    }

    public void render(int layerHash, Matrix4f vp) {
        ArrayList<LineQueueEntry> lines = mLines.get(layerHash);

        mLineShader.bind();
        for (var line : lines) {
            mLineShader.setMat4(0, vp);
            mLineShader.setVec2(1, line.shape().start());
            mLineShader.setVec2(2, line.shape().end());
            mLineShader.setVec4(3, line.shape().color());

            GL46.glBindVertexArray(mLineVAO);
            GL46.glDrawArrays(GL46.GL_LINES, 0, 2);
        }
        lines.clear();
    }
}
