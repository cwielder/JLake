package dev.rlni.jlake;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dev.rlni.jlake.entity.Entity;
import dev.rlni.jlake.entity.component.EntityComponent;
import dev.rlni.jlake.event.IEvent;

import java.util.ArrayList;

public final class Scene {
    private record EntityEntry(
        String type,
        JsonObject properties
    ) { }
    private record SceneData(
        ArrayList<EntityEntry> entities
    ) { }

    private final ArrayList<Entity> mEntities = new ArrayList<>();

    public Scene(final String path) {
        Gson gson = new Gson();
        String json = Utils.readFileAsString(path);
        gson.fromJson(json, SceneData.class).entities.forEach(entityData -> {
            try {
                Entity entity = (Entity) Class.forName(entityData.type()).getConstructor().newInstance();
                var entityDataStructureClass = entity.getDataStructure();
                var entityDataStructure = gson.fromJson(entityData.properties(), entityDataStructureClass);
                entity.init(entityDataStructure);
                mEntities.add(entity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void destroy() {
        this.clearEntities();
    }

    public void update(final float timeStep) {
        for (Entity entity : mEntities) {
            entity.update(timeStep);
        }
    }

    public void onEvent(final IEvent event) {
        for (Entity entity : mEntities) {
            entity.onEvent(event);
        }
    }

    private void clearEntities() {
        for (Entity entity : mEntities) {
            for (EntityComponent component : entity.getComponents()) {
                component.destroy();
            }

            entity.destroy();
        }

        mEntities.clear();
    }

    public ArrayList<Entity> getEntities() {
        return mEntities;
    }
}
