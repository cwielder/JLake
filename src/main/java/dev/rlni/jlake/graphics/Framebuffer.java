package dev.rlni.jlake.graphics;

import org.joml.Vector2i;
import org.joml.Vector4f;
import org.joml.Vector4i;
import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

public class Framebuffer {
    public enum BindMode {
        DRAW,
        READ,
        WRITE
    }

    public enum Component {
        COLOR(GL46.GL_COLOR_BUFFER_BIT),
        DEPTH(GL46.GL_DEPTH_BUFFER_BIT),
        STENCIL(GL46.GL_STENCIL_BUFFER_BIT);

        private final int mValue;

        Component(final int value) {
            mValue = value;
        }

        public int getValue() {
            return mValue;
        }
    }

    private int mId;
    private Vector2i mSize;
    private ArrayList<Texture> mTextureBuffers = new ArrayList<>(8);
    private Texture mDepthStencilBuffer = null;
    private boolean mFinalized = false;

    private static final Framebuffer sBackbuffer = new Framebuffer();

    public Framebuffer(final Vector2i size) {
        mSize = size;

        mId = GL46.glCreateFramebuffers();
        assert mId != GL46.GL_NONE : "Failed to create framebuffer";
    }

    public void destroy() {
        assert mId != GL46.GL_NONE : "Cannot destroy backbuffer!";

        GL46.glDeleteFramebuffers(mId);

        for (Texture texture : mTextureBuffers) {
            texture.destroy();
        }

        if (mDepthStencilBuffer != null) {
            mDepthStencilBuffer.destroy();
        }

        mTextureBuffers.clear();
        mDepthStencilBuffer = null;
    }

    public int getId() {
        return mId;
    }

    public Vector2i getSize() {
        return mSize;
    }

    public Texture getTextureBuffer(final int index) {
        return mTextureBuffers.get(index);
    }

    public Texture getDepthStencilBuffer() {
        assert mDepthStencilBuffer != null : "Framebuffer does not have a depth/stencil attachment!";

        return mDepthStencilBuffer;
    }

    public void bind(final BindMode mode) {
        switch (mode) {
            case DRAW -> GL46.glBindFramebuffer(GL46.GL_FRAMEBUFFER, mId);
            case READ -> GL46.glBindFramebuffer(GL46.GL_READ_FRAMEBUFFER, mId);
            case WRITE -> GL46.glBindFramebuffer(GL46.GL_DRAW_FRAMEBUFFER, mId);
        }
    }

    public void clear(final Vector4f value, final Component components, final int drawBuffer) {
        FloatBuffer buffer = MemoryUtil.memAllocFloat(4);
        value.get(buffer);

        if ((components.getValue() & Component.COLOR.getValue()) != 0) {
            GL46.glClearNamedFramebufferfv(mId, GL46.GL_COLOR, drawBuffer, buffer);
        }
        if ((components.getValue() & Component.DEPTH.getValue()) != 0) {
            GL46.glClearNamedFramebufferfv(mId, GL46.GL_DEPTH, 0, buffer);
        }
        // TODO: Stencil

        MemoryUtil.memFree(buffer);
    }

    public void clear(final Vector4i value, final Component components, final int drawBuffer) {
        IntBuffer buffer = MemoryUtil.memAllocInt(4);
        value.get(buffer);

        if ((components.getValue() & Component.COLOR.getValue()) != 0) {
            GL46.glClearNamedFramebufferiv(mId, GL46.GL_COLOR, drawBuffer, buffer);
        }
        if ((components.getValue() & Component.DEPTH.getValue()) != 0) {
            throw new RuntimeException("Cannot clear depth buffer with integer value!");
        }
    }

    public void resize(final Vector2i size) {
        assert mId != GL46.GL_NONE : "Cannot resize backbuffer!";

        mSize = size;

        // As for the texture buffers, we can't resize them so we'll have to destroy them and recreate them
        ArrayList<Texture> textureBuffers = new ArrayList<>(mTextureBuffers.size());
        for (int i = 0; i < mTextureBuffers.size(); i++) {
            Texture texture = mTextureBuffers.get(i);
            textureBuffers.add(new Texture(size, texture.getFormat(), texture.getFilterMode()));
            GL46.glNamedFramebufferTexture(mId, GL46.GL_COLOR_ATTACHMENT0 + i, texture.getId(), 0);
            texture.destroy();
        }

        mTextureBuffers = textureBuffers;

        if (mDepthStencilBuffer != null) {
            Texture depthStencilBuffer = new Texture(size, mDepthStencilBuffer.getFormat(), mDepthStencilBuffer.getFilterMode());
            GL46.glNamedFramebufferTexture(mId, GL46.GL_DEPTH_STENCIL_ATTACHMENT, depthStencilBuffer.getId(), 0);
            mDepthStencilBuffer.destroy();
            mDepthStencilBuffer = depthStencilBuffer;
        }
    }

    public void addTextureBuffer(final Texture.Format format, final Texture.FilterMode filterMode) {
        assert mId != GL46.GL_NONE : "Cannot add texture buffer to backbuffer!";
        assert !mFinalized : "Cannot add texture buffer to finalized framebuffer!";

        if (format.getValue() == Texture.Format.DEPTH24_STENCIL8.getValue() || format.getValue() == Texture.Format.DEPTH32F_STENCIL8.getValue()) {
            assert mDepthStencilBuffer == null : "Framebuffer already has a depth/stencil attachment!";
            mDepthStencilBuffer = new Texture(mSize, format, filterMode);
            GL46.glNamedFramebufferTexture(mId, GL46.GL_DEPTH_STENCIL_ATTACHMENT, mDepthStencilBuffer.getId(), 0);
        } else {
            Texture texture = new Texture(mSize, format, filterMode);
            mTextureBuffers.add(texture);
            GL46.glNamedFramebufferTexture(mId, GL46.GL_COLOR_ATTACHMENT0 + mTextureBuffers.size() - 1, texture.getId(), 0);
        }
    }

    public void finalizeAttachments() {
        assert mId != GL46.GL_NONE : "Cannot finalize backbuffer!";
        assert !mFinalized : "Framebuffer already finalized!";

        // TODO: is there a better way to do this?
        final int[] attachments = new int[mTextureBuffers.size()];
        for (int i = 0; i < mTextureBuffers.size(); i++) {
            attachments[i] = GL46.GL_COLOR_ATTACHMENT0 + i;
        }
        GL46.glNamedFramebufferDrawBuffers(mId, attachments);

        int status = GL46.glCheckNamedFramebufferStatus(mId, GL46.GL_FRAMEBUFFER);
        if (status != GL46.GL_FRAMEBUFFER_COMPLETE) {
            throw new RuntimeException("Framebuffer is not complete! Status: " + status);
        }

        mFinalized = true;
    }

    public static Framebuffer getBackbuffer() {
        return sBackbuffer;
    }

    public static void blit(final Framebuffer src, final Framebuffer dst, final Vector2i srcStart, final Vector2i srcEnd, final Vector2i dstStart, final Vector2i dstEnd, final Component components, final Texture.FilterMode filterMode) {
        GL46.glBlitNamedFramebuffer(
            src.getId(),
            dst.getId(),
            srcStart.x, srcStart.y, srcEnd.x, srcEnd.y,
            dstStart.x, dstStart.y, dstEnd.x, dstEnd.y,
            components.getValue(),
            filterMode.getValue()
        );
    }

    private Framebuffer() {
    }
}
