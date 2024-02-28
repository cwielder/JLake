package dev.rlni.jlake.graphics;

import org.lwjgl.opengl.GL46;

public class GraphicsContext {
    public enum CullFace {
        FRONT(GL46.GL_FRONT),
        BACK(GL46.GL_BACK),
        FRONT_AND_BACK(GL46.GL_FRONT_AND_BACK);

        private final int mValue;

        CullFace(final int value) {
            mValue = value;
        }

        public int getValue() {
            return mValue;
        }
    }

    public enum CullDirection {
        CLOCKWISE(GL46.GL_CW),
        COUNTER_CLOCKWISE(GL46.GL_CCW);

        private final int mValue;

        CullDirection(final int value) {
            mValue = value;
        }

        public int getValue() {
            return mValue;
        }
    }

    public enum DepthFunction {
        NEVER(GL46.GL_NEVER),
        LESS(GL46.GL_LESS),
        EQUAL(GL46.GL_EQUAL),
        LESS_OR_EQUAL(GL46.GL_LEQUAL),
        GREATER(GL46.GL_GREATER),
        NOT_EQUAL(GL46.GL_NOTEQUAL),
        GREATER_OR_EQUAL(GL46.GL_GEQUAL),
        ALWAYS(GL46.GL_ALWAYS);

        private final int mValue;

        DepthFunction(final int value) {
            mValue = value;
        }

        public int getValue() {
            return mValue;
        }
    }

    public enum BlendFunction {
        ZERO(GL46.GL_ZERO),
        ONE(GL46.GL_ONE),
        SRC_COLOR(GL46.GL_SRC_COLOR),
        ONE_MINUS_SRC_COLOR(GL46.GL_ONE_MINUS_SRC_COLOR),
        DST_COLOR(GL46.GL_DST_COLOR),
        ONE_MINUS_DST_COLOR(GL46.GL_ONE_MINUS_DST_COLOR),
        SRC_ALPHA(GL46.GL_SRC_ALPHA),
        ONE_MINUS_SRC_ALPHA(GL46.GL_ONE_MINUS_SRC_ALPHA),
        DST_ALPHA(GL46.GL_DST_ALPHA),
        ONE_MINUS_DST_ALPHA(GL46.GL_ONE_MINUS_DST_ALPHA),
        CONSTANT_COLOR(GL46.GL_CONSTANT_COLOR),
        ONE_MINUS_CONSTANT_COLOR(GL46.GL_ONE_MINUS_CONSTANT_COLOR),
        CONSTANT_ALPHA(GL46.GL_CONSTANT_ALPHA),
        ONE_MINUS_CONSTANT_ALPHA(GL46.GL_ONE_MINUS_CONSTANT_ALPHA);

        private final int mValue;

        BlendFunction(final int value) {
            mValue = value;
        }

        public int getValue() {
            return mValue;
        }
    }

    public enum BlendEquation {
        ADD(GL46.GL_FUNC_ADD),
        SUBTRACT(GL46.GL_FUNC_SUBTRACT),
        REVERSE_SUBTRACT(GL46.GL_FUNC_REVERSE_SUBTRACT),
        MIN(GL46.GL_MIN),
        MAX(GL46.GL_MAX);

        private final int mValue;

        BlendEquation(final int value) {
            mValue = value;
        }

        public int getValue() {
            return mValue;
        }
    }

    boolean mDepthTest = true;
    boolean mDepthWrite = true;
    boolean mCullEnabled = false;
    boolean mBlendEnabled = false;

    DepthFunction mDepthFunction = DepthFunction.LESS;

    CullFace mCullFace = CullFace.BACK;
    CullDirection mCullDirection = CullDirection.COUNTER_CLOCKWISE;

    BlendFunction mBlendSrcRGB = BlendFunction.SRC_ALPHA, mBlendSrcA = BlendFunction.SRC_ALPHA;
    BlendFunction mBlendDstRGB = BlendFunction.ONE_MINUS_SRC_ALPHA, mBlendDstA = BlendFunction.ONE_MINUS_SRC_ALPHA;
    BlendEquation mBlendEquation = BlendEquation.ADD;

    GraphicsContext depth(final DepthFunction function, final boolean write) {
        mDepthTest = true;
        mDepthFunction = function;
        mDepthWrite = write;
        return this;
    }

    GraphicsContext depth(final boolean write) {
        assert !write : "True value passed to depth disabler. Use depth(DepthFunction, boolean) instead.";

        mDepthTest = false;

        return this;
    }

    GraphicsContext cull(final CullFace face, final CullDirection direction) {
        mCullEnabled = true;
        mCullFace = face;
        mCullDirection = direction;
        return this;
    }

    GraphicsContext cull(final boolean enable) {
        assert !enable : "True value passed to cull disabler. Use cull(CullFace, CullDirection) instead.";

        mCullEnabled = false;

        return this;
    }

    GraphicsContext blend(final BlendFunction srcRGB, final BlendFunction dstRGB, final BlendFunction srcA, final BlendFunction dstA, final BlendEquation equation) {
        mBlendEnabled = true;
        mBlendSrcRGB = srcRGB;
        mBlendDstRGB = dstRGB;
        mBlendSrcA = srcA;
        mBlendDstA = dstA;
        mBlendEquation = equation;
        return this;
    }

    GraphicsContext blend(final boolean enable) {
        assert !enable : "True value passed to blend disabler. Use blend(BlendFunction, BlendFunction, BlendFunction, BlendFunction, BlendEquation) instead.";

        mBlendEnabled = false;

        return this;
    }

    void apply() {
        if (mDepthTest) {
            GL46.glEnable(GL46.GL_DEPTH_TEST);
            GL46.glDepthFunc(mDepthFunction.getValue());
            GL46.glDepthMask(mDepthWrite);
        } else {
            GL46.glDisable(GL46.GL_DEPTH_TEST);
        }

        if (mCullEnabled) {
            GL46.glEnable(GL46.GL_CULL_FACE);
            GL46.glCullFace(mCullFace.getValue());
            GL46.glFrontFace(mCullDirection.getValue());
        } else {
            GL46.glDisable(GL46.GL_CULL_FACE);
        }

        if (mBlendEnabled) {
            GL46.glEnable(GL46.GL_BLEND);
            GL46.glBlendFuncSeparate(mBlendSrcRGB.getValue(), mBlendDstRGB.getValue(), mBlendSrcA.getValue(), mBlendDstA.getValue());
            GL46.glBlendEquation(mBlendEquation.getValue());
        } else {
            GL46.glDisable(GL46.GL_BLEND);
        }
    }
}
