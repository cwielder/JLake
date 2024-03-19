package dev.rlni.jlake;

import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Utils {
    private Utils() {
    }

    /**
     * Reads a file from the assets folder and returns it as a ByteBuffer. Remember to free the buffer after use.
     * @param path The path to the file.
     * @return The file as a ByteBuffer.
     */
    public static ByteBuffer readFileAsBuffer(final String path) {
        ByteBuffer content;
        try {
            var bytes = Utils.class.getResourceAsStream("/assets/" + path).readAllBytes();
            content = MemoryUtil.memAlloc(bytes.length).put(bytes).flip();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return content;
    }

    public static String readFileAsString(final String path) {
        String content;
        try {
            content = new String(Utils.class.getResourceAsStream("/assets/" + path).readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return content;
    }

    public static float clamp(final float value, final float min, final float max) {
        return Math.max(min, Math.min(max, value));
    }
}
