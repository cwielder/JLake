#version 460 core

layout (location = 3) uniform vec4 uColor;

out vec4 FragColor;

void main()  {
    FragColor = uColor;
}

