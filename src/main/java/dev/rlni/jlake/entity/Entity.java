package dev.rlni.jlake.entity;

public abstract class Entity {
    public void init(Object properties) { }
    public void destroy() { }
    public void update(float timeStep) { }

    public abstract Class<?> getDataStructure();
}
