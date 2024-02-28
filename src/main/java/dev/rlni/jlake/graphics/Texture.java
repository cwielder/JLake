package dev.rlni.jlake.graphics;

import dev.rlni.jlake.Utils;
import org.joml.Vector2i;
import org.lwjgl.opengl.GL46;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public final class Texture {
    public enum FilterMode {
        NEAREST(GL46.GL_NEAREST),
        LINEAR(GL46.GL_LINEAR),
        NEAREST_MIPMAP_NEAREST(GL46.GL_NEAREST_MIPMAP_NEAREST),
        LINEAR_MIPMAP_NEAREST(GL46.GL_LINEAR_MIPMAP_NEAREST),
        NEAREST_MIPMAP_LINEAR(GL46.GL_NEAREST_MIPMAP_LINEAR),
        LINEAR_MIPMAP_LINEAR(GL46.GL_LINEAR_MIPMAP_LINEAR);

        private final int mValue;

        FilterMode(final int value) {
            mValue = value;
        }

        public int getValue() {
            return mValue;
        }
    }

    public enum Format {
        RGB16F(GL46.GL_RGB16F),
        RGB32F(GL46.GL_RGB32F),
        R8(GL46.GL_R8),
        RGB8(GL46.GL_RGB8),
        RGBA8(GL46.GL_RGBA8),
        RGBA32F(GL46.GL_RGBA32F),
        DEPTH24_STENCIL8(GL46.GL_DEPTH24_STENCIL8),
        DEPTH32_STENCIL8(GL46.GL_DEPTH32F_STENCIL8),

        R(GL46.GL_R),
        RGB(GL46.GL_RGB),
        RGBA(GL46.GL_RGBA);

        private final int mValue;

        Format(final int value) {
            mValue = value;
        }

        public int getValue() {
            return mValue;
        }
    }

    public record ImageData(
        ByteBuffer data,
        Vector2i size,
        int channelCount
    ) { }

    private static final Map<String, ImageData> sTextureCache = new HashMap<>();

    private int mId = GL46.GL_NONE;
    private Vector2i mSize = null;
    private Format mFormat = null;
    private FilterMode mFilterMode = null;

    public Texture(final String texturePath, final FilterMode filterMode) {
        mId = GL46.glCreateTextures(GL46.GL_TEXTURE_2D);

        this.initFromFile(texturePath, filterMode);
    }

    public Texture(final Vector2i size, final Format format, final FilterMode filterMode) {
        mId = GL46.glCreateTextures(GL46.GL_TEXTURE_2D);

        GL46.glTextureStorage2D(mId, 1, format.getValue(), size.x, size.y);
        GL46.glTextureParameteri(mId, GL46.GL_TEXTURE_MIN_FILTER, filterMode.getValue());
        GL46.glTextureParameteri(mId, GL46.GL_TEXTURE_MAG_FILTER, filterMode.getValue());

        mSize = size;
        mFormat = format;
        mFilterMode = filterMode;
    }

    public void destroy() {
        if (mId != GL46.GL_NONE) {
            GL46.glDeleteTextures(mId);
            mId = 0;
        }
    }

    public void bind(final int slot) {
        assert mId != GL46.GL_NONE : "Cannot bind invalid texture";
        GL46.glBindTextureUnit(slot, mId);
    }

    public void initFromFile(final String texturePath, final FilterMode filterMode) {
        assert mId != GL46.GL_NONE : "Cannot initialize invalid texture";

        final Function<String, ImageData> loadImage = (path) -> {
            for (var entry : sTextureCache.entrySet()) {
                if (entry.getKey().equals(path)) {
                    return entry.getValue();
                }
            }

            int[] width = new int[1], height = new int[1], channels = new int[1];
            STBImage.stbi_set_flip_vertically_on_load(true);
            final ByteBuffer file = Utils.readFileAsBuffer(path);
            assert file != null : "Failed to read file: " + texturePath;
            final ByteBuffer data = STBImage.stbi_load_from_memory(file, width, height, channels, 0);
            assert data != null : "Failed to load image: " + texturePath;
            MemoryUtil.memFree(file);

            ImageData imageData = new ImageData(data, new Vector2i(width[0], height[0]), channels[0]);
            sTextureCache.put(path, imageData);

            System.out.println("Loaded texture: " + texturePath);
            return imageData;
        };

        ImageData imageData = loadImage.apply(texturePath);
        this.initFromData(imageData, filterMode);
    }

    public void initFromData(final ImageData imageData, final FilterMode filterMode) {
        assert mId != GL46.GL_NONE : "Cannot initialize invalid texture";

        mSize = imageData.size();
        mFilterMode = filterMode;

        GL46.glTextureParameteri(mId, GL46.GL_TEXTURE_MIN_FILTER, mFilterMode.getValue());
        GL46.glTextureParameteri(mId, GL46.GL_TEXTURE_MAG_FILTER, mFilterMode.getValue());

        Consumer<Format> safeSubImage = (format) -> {
            if (imageData.data() == null) {
                return;
            }

            GL46.glTextureSubImage2D(mId, 0, 0, 0, mSize.x, mSize.y, format.getValue(), GL46.GL_UNSIGNED_BYTE, imageData.data());
            mFormat = format;
        };

        switch (imageData.channelCount()) {
            case 4 -> {
                GL46.glTextureStorage2D(mId, 1, GL46.GL_RGBA8, mSize.x, mSize.y);
                safeSubImage.accept(Format.RGBA);
            }
            case 3 -> {
                GL46.glTextureStorage2D(mId, 1, GL46.GL_RGB8, mSize.x, mSize.y);
                safeSubImage.accept(Format.RGB);
            }
            case 1 -> {
                GL46.glTextureStorage2D(mId, 1, GL46.GL_R8, mSize.x, mSize.y);
                safeSubImage.accept(Format.R);
            }
            default -> throw new UnsupportedOperationException("Unsupported channel count: " + imageData.channelCount());
        }

        GL46.glTextureParameteri(mId, GL46.GL_TEXTURE_BASE_LEVEL, 0);
        GL46.glTextureParameteri(mId, GL46.GL_TEXTURE_MAX_LEVEL, 6);
        GL46.glGenerateTextureMipmap(mId);
    }

    public static void clearCache() {
        for (var entry : sTextureCache.entrySet()) {
            STBImage.stbi_image_free(entry.getValue().data());
        }

        sTextureCache.clear();
    }
}
