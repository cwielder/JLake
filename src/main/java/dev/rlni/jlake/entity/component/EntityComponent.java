package dev.rlni.jlake.entity.component;

import dev.rlni.jlake.event.IEvent;

public interface EntityComponent {
    void destroy();
    default void onEvent(final IEvent event) {

    }
}
