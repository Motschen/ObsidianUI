/*
 * Copyright © 2020~2024 LambdAurora <email@lambdaurora.dev>
 * Copyright © 2024 ThinkingStudio
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.thinkingstudio.obsidianui.mixin;

import org.thinkingstudio.obsidianui.event.OpenScreenCallback;
import org.thinkingstudio.obsidianui.event.ResolutionChangeCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Represents the injection point for the {@link OpenScreenCallback} and {@link ResolutionChangeCallback} events.
 *
 * @author LambdAurora
 * @version 1.4.0
 * @since 1.2.0
 */
@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "openScreen", at = @At("HEAD"))
    private void spruceui_onScreenPre(Screen screen, CallbackInfo ci) {
        OpenScreenCallback.PRE.invoker().apply((MinecraftClient) (Object) this, screen);
    }

    @Inject(method = "openScreen", at = @At("RETURN"))
    private void spruceui_onScreenChange(Screen screen, CallbackInfo ci) {
        OpenScreenCallback.EVENT.invoker().apply((MinecraftClient) (Object) this, screen);
    }

    @Inject(method = "onResolutionChanged", at = @At("RETURN"))
    private void spruceui_onResolutionChanged(CallbackInfo ci) {
        ResolutionChangeCallback.EVENT.invoker().apply((MinecraftClient) (Object) this);
    }
}
