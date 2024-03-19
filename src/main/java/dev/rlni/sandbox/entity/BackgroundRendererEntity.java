package dev.rlni.sandbox.entity;

import dev.rlni.jlake.entity.Entity;
import dev.rlni.sandbox.entity.component.BackgroundRendererComponent;

public class BackgroundRendererEntity extends Entity {
    public record Data(

    ) { }

    @Override
    public void init(Object properties) {
        BackgroundRendererComponent renderer = new BackgroundRendererComponent();
        this.addComponent("renderer", renderer);
    }

    @Override
    public void update(final float timeStep) {
        ((BackgroundRendererComponent) this.getComponent("renderer")).update(timeStep);
    }

    @Override
    public Class<?> getDataStructure() {
        return Data.class;
    }
}

