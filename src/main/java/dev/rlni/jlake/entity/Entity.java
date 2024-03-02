package dev.rlni.jlake.entity;

import dev.rlni.jlake.Scene;
import dev.rlni.jlake.entity.component.EntityComponent;
import dev.rlni.jlake.event.IEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class Entity {
    private final Map<String, EntityComponent> mComponents = new HashMap<>();
    public Scene mScene = null;

    public void init(Object properties) { }
    public void destroy() { }
    public void update(float timeStep) { }
    public void onEvent(final IEvent event) { }

    public abstract Class<?> getDataStructure();

    public void addComponent(final String identifier, final EntityComponent component) {
        mComponents.put(identifier, component);
    }

    public EntityComponent getComponent(final String identifier) {
        return mComponents.get(identifier);
    }

    public ArrayList<EntityComponent> getComponents(Class<?> type) {
        ArrayList<EntityComponent> components = new ArrayList<>();
        for (EntityComponent component : mComponents.values()) {
            if (type.isInstance(component)) {
                components.add(component);
            }
        }
        return components;
    }

    public ArrayList<EntityComponent> getComponents() {
        return new ArrayList<>(mComponents.values());
    }
}
