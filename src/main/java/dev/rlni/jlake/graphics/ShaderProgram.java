package dev.rlni.jlake.graphics;

import dev.rlni.jlake.Utils;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL46;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public final class ShaderProgram {
    private class Shader {
        private final int mId;

        private static final Map<String, String> sSourceCache = new HashMap<>();

        public Shader(final String path, final int type) {
            Function<String, Integer> compile = (final String code) -> {
                System.out.println("Compiling shader: " + path);

                final int id = GL46.glCreateShader(type);

                GL46.glShaderSource(id, code);
                GL46.glCompileShader(id);

                int success = GL46.glGetShaderi(id, GL46.GL_COMPILE_STATUS);
                if (success == GL46.GL_FALSE) {
                    String infoLog = GL46.glGetShaderInfoLog(id);
                    System.out.println("Shader compilation failed: " + infoLog);
                }

                return id;
            };

            if (sSourceCache.containsKey(path)) {
                mId = compile.apply(sSourceCache.get(path));
                return;
            }

            String code = Utils.readFileAsString(path);
            mId = compile.apply(code);
            sSourceCache.put(path, code);
        }

        public void destroy() {
            GL46.glDeleteShader(mId);
        }

        public int getId() {
            return mId;
        }
    }

    private final int mId;
    private final Map<String, Integer> mUniformLocations;

    public ShaderProgram(final String vshPath, final String fshPath) {
        mId = GL46.glCreateProgram();

        // Compile and link shaders

        final Shader vsh = new Shader(vshPath, GL46.GL_VERTEX_SHADER);
        final Shader fsh = new Shader(fshPath, GL46.GL_FRAGMENT_SHADER);

        GL46.glAttachShader(mId, vsh.mId);
        GL46.glAttachShader(mId, fsh.mId);
        GL46.glLinkProgram(mId);

        int success = GL46.glGetProgrami(mId, GL46.GL_LINK_STATUS);
        if (success == GL46.GL_FALSE) {
            String infoLog = GL46.glGetProgramInfoLog(mId);
            System.out.println("Shader program linking failed: " + infoLog);
        }

        GL46.glDetachShader(mId, vsh.getId());
        GL46.glDetachShader(mId, fsh.getId());

        vsh.destroy();
        fsh.destroy();

        // Validate program

        GL46.glValidateProgram(mId);

        success = GL46.glGetProgrami(mId, GL46.GL_VALIDATE_STATUS);
        if (success == GL46.GL_FALSE) {
            String infoLog = GL46.glGetProgramInfoLog(mId);
            System.out.println("Shader program validation failed: " + infoLog);
        }

        // Cache uniforms

        int uniformCount = GL46.glGetProgrami(mId, GL46.GL_ACTIVE_UNIFORMS);
        mUniformLocations = new HashMap<>(uniformCount);
        for (int i = 0; i < uniformCount; i++) {
            String name = GL46.glGetActiveUniformName(mId, i);
            int location = GL46.glGetUniformLocation(mId, name);
            mUniformLocations.put(name, location);
        }
    }

    public void destroy() {
        GL46.glDeleteProgram(mId);
    }

    public void bind() {
        GL46.glUseProgram(mId);
    }

    public void setInt(final int location, final int value) {
        GL46.glUniform1i(location, value);
    }

    public void setInt(final String name, final int value) {
        this.setInt(this.getUniformLocation(name), value);
    }

    public void setOptionalInt(final String name, final Integer value) {
        final int location = this.getUniformLocation(name);
        if (location != -1) {
            this.setInt(location, value);
        }
    }

    public void setFloat(final int location, final float value) {
        GL46.glUniform1f(location, value);
    }

    public void setFloat(final String name, final float value) {
        this.setFloat(this.getUniformLocation(name), value);
    }

    public void setOptionalFloat(final String name, final Float value) {
        final int location = this.getUniformLocation(name);
        if (location != -1) {
            this.setFloat(location, value);
        }
    }

    public void setVec2(final int location, final Vector2f value) {
        GL46.glUniform2f(location, value.x, value.y);
    }

    public void setVec2(final String name, final Vector2f value) {
        this.setVec2(this.getUniformLocation(name), value);
    }

    public void setOptionalVec2(final String name, final Vector2f value) {
        final int location = this.getUniformLocation(name);
        if (location != -1) {
            this.setVec2(location, value);
        }
    }

    public void setVec3(final int location, final Vector3f value) {
        GL46.glUniform3f(location, value.x, value.y, value.z);
    }

    public void setVec3(final String name, final Vector3f value) {
        this.setVec3(this.getUniformLocation(name), value);
    }

    public void setOptionalVec3(final String name, final Vector3f value) {
        final int location = this.getUniformLocation(name);
        if (location != -1) {
            this.setVec3(location, value);
        }
    }

    public void setVec4(final int location, final Vector4f value) {
        GL46.glUniform4f(location, value.x, value.y, 0.0f, 0.0f);
    }

    public void setVec4(final String name, final Vector4f value) {
        this.setVec4(this.getUniformLocation(name), value);
    }

    public void setOptionalVec4(final String name, final Vector4f value) {
        final int location = this.getUniformLocation(name);
        if (location != -1) {
            this.setVec4(location, value);
        }
    }

    public void setMat4(final int location, final Matrix4f value) {
        GL46.glUniformMatrix4fv(location, false, value.get(new float[16]));
    }

    public void setMat4(final String name, final Matrix4f value) {
        this.setMat4(this.getUniformLocation(name), value);
    }

    public void setOptionalMat4(final String name, final Matrix4f value) {
        final int location = this.getUniformLocation(name);
        if (location != -1) {
            this.setMat4(location, value);
        }
    }

    private int getUniformLocation(final String name) {
        Integer location = mUniformLocations.get(name);
        if (location == null) {
            return -1;
        }

        return location;
    }
}
