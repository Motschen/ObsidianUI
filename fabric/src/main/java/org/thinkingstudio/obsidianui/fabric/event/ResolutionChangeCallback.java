/*
 * Copyright © 2020~2024 LambdAurora <email@lambdaurora.dev>
 * Copyright © 2024 ThinkingStudio
 *
 * This file is part of ObsidianUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.thinkingstudio.obsidianui.fabric.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;

/**
 * Represents an event callback which is fired when the Minecraft's resolution is changed.
 *
 * @author LambdAurora
 * @version 3.3.0
 * @since 1.2.0
 */
@FunctionalInterface
public interface ResolutionChangeCallback {
    Event<ResolutionChangeCallback> EVENT = EventFactory.createArrayBacked(ResolutionChangeCallback.class, listeners -> client -> {
        for (ResolutionChangeCallback event : listeners) {
            event.apply(client);
        }
    });

    void apply(MinecraftClient client);
}
