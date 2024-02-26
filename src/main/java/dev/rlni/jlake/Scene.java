package dev.rlni.jlake;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dev.rlni.jlake.entity.Entity;

import java.util.ArrayList;

public class Scene {
    private record EntityEntry(
        String type,
        JsonObject properties
    ) { }
    private record SceneData(
        ArrayList<EntityEntry> entities
    ) { }

    private final ArrayList<Entity> mEntities = new ArrayList<>();

    public Scene(final String name) {
        Gson gson = new Gson();
        String json = Utils.readFile("/scenes/" + name + ".json");
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

        for (Entity entity : mEntities) {
            System.out.println(entity.getClass().getName());
        }
    }

    public void destroy() {
        for (Entity entity : mEntities) {
            entity.destroy();
        }

        mEntities.clear();
    }

    public void update(final float timeStep) {
        for (Entity entity : mEntities) {
            entity.update(timeStep);
        }
    }
}
