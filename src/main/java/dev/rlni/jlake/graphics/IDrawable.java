package dev.rlni.jlake.graphics;

import dev.rlni.jlake.entity.component.CameraComponent;

public interface IDrawable {
    record RenderInfo(
        Framebuffer framebuffer,
        CameraComponent camera
    ) { }

    void draw(final RenderInfo renderInfo);
    int getLayerHash();
}
