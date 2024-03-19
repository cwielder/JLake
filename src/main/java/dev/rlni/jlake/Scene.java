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
    private final ArrayList<Entity> mEntitiesToAdd = new ArrayList<>();

    public Scene(final String path) {
        Gson gson = new Gson();
        String json = Utils.readFileAsString(path);
        gson.fromJson(json, SceneData.class).entities.forEach(entityData ->
            this.spawnEntity(entityData.type(), entityData.properties())
        );
    }

    public Entity spawnEntity(final Class<? extends Entity> type, final Object properties) {
        try {
            Entity entity = type.getConstructor().newInstance();
            entity.mScene = this;
            entity.init(properties);
            mEntitiesToAdd.add(entity);
            return entity;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void spawnEntity(final String type, final JsonObject properties) {
        try {
            Entity entity = (Entity) Class.forName(type).getConstructor().newInstance();
            entity.mScene = this;
            entity.init(new Gson().fromJson(properties, entity.getDataStructure()));
            mEntities.add(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
        this.clearEntities();
    }

    public void update(final float timeStep) {
        mEntities.addAll(mEntitiesToAdd);
        mEntitiesToAdd.clear();

        ArrayList<Entity> entitiesToDestroy = new ArrayList<>();
        for (Entity entity : mEntities) {
            if (!entity.isAlive()) {
                entitiesToDestroy.add(entity);
            }
        }

        for (Entity entity : entitiesToDestroy) {
            mEntities.remove(entity);
            entity.destroy();
        }

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
