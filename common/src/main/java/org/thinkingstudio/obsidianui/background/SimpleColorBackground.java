/*
 * Copyright © 2020~2024 LambdAurora <email@lambdaurora.dev>
 * Copyright © 2024 ThinkingStudio
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.thinkingstudio.obsidianui.background;

import org.thinkingstudio.obsidianui.util.ColorUtil;
import org.thinkingstudio.obsidianui.widget.SpruceWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

public class SimpleColorBackground extends DrawableHelper implements Background {
    private final MinecraftClient client = MinecraftClient.getInstance();
    private final int color;

    public SimpleColorBackground(int color) {
        this.color = color;
    }

    public SimpleColorBackground(int red, int green, int blue, int alpha) {
        this(ColorUtil.packARGBColor(red, green, blue, alpha));
    }

    @Override
    public void render(MatrixStack matrices, SpruceWidget widget, int vOffset, int mouseX, int mouseY, float delta) {
        int x = widget.getX();
        int y = widget.getY();
        fill(matrices, x, y, x + widget.getWidth(), y + widget.getHeight(), this.color);
    }

    @Override
    public String toString() {
        return "SimpleColorBackground{" +
                ", color=" + this.color +
                '}';
    }
}
