#version 460 core

layout (location = 0) in vec2 aPos;

layout (location = 0) uniform mat4 uViewProjection;
layout (location = 1) uniform vec2 uPoint1;
layout (location = 2) uniform vec2 uPoint2;

void main() {
    if (gl_VertexID == 0) {
        gl_Position = uViewProjection * vec4(uPoint1, 0.0, 1.0);
    } else {
        gl_Position = uViewProjection * vec4(uPoint2, 0.0, 1.0);
    }
}

