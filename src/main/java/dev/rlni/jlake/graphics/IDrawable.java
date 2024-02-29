package dev.rlni.jlake.graphics;

import dev.rlni.jlake.entity.component.CameraComponent;

public interface IDrawable {
    public record RenderInfo(
        Framebuffer framebuffer,
        CameraComponent camera
    ) { }

    public void draw(final RenderInfo renderInfo);
    public int getLayerHash();
}
