package dev.rlni.jlake.graphics.shape;

import org.joml.Vector2f;
import org.joml.Vector4f;

public record Line (
    Vector2f start,
    Vector2f end,
    Vector4f color
) { }
