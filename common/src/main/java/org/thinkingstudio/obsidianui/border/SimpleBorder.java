/*
 * Copyright © 2020~2024 LambdAurora <email@lambdaurora.dev>
 * Copyright © 2024 ThinkingStudio
 *
 * This file is part of ObsidianUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.thinkingstudio.obsidianui.border;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import org.thinkingstudio.obsidianui.util.ColorUtil;
import org.thinkingstudio.obsidianui.widget.SpruceWidget;

import java.util.Arrays;

/**
 * Represents a simple solid border to draw around a widget.
 *
 * @author LambdAurora
 * @version 5.0.0
 * @since 2.0.0
 */
public final class SimpleBorder implements Border {
	public static final SimpleBorder SIMPLE_BORDER = new SimpleBorder(1, 192, 192, 192, 255);

	private final int thickness;
	private final int[] color;
	private final int[] focusedColor;

	public SimpleBorder(int thickness, int color) {
		this(thickness, color, color);
	}

	public SimpleBorder(int thickness, int color, int focusedColor) {
		this.thickness = thickness;
		this.color = ColorUtil.unpackARGBColor(color);
		this.focusedColor = ColorUtil.unpackARGBColor(focusedColor);
	}

	public SimpleBorder(int thickness, int red, int green, int blue, int alpha) {
		this(thickness, red, green, blue, alpha, red, green, blue, alpha);
	}

	public SimpleBorder(int thickness, int red, int green, int blue, int alpha, int focusedRed, int focusedGreen, int focusedBlue, int focusedAlpha) {
		this.thickness = thickness;
		this.color = new int[]{red, green, blue, alpha};
		this.focusedColor = new int[]{focusedRed, focusedGreen, focusedBlue, focusedAlpha};
	}

	@Override
	public void render(DrawContext drawContext, SpruceWidget widget, int mouseX, int mouseY, float delta) {
		var tessellator = Tessellator.getInstance();
		var buffer = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
		RenderSystem.setShader(GameRenderer::getPositionColorProgram);
		int x = widget.getX();
		int y = widget.getY();
		int right = x + widget.getWidth();
		int bottom = y + widget.getHeight();
		boolean focused = widget.isFocused();
		// Top border
		this.vertex(buffer, x, y + this.thickness, focused);
		this.vertex(buffer, right, y + this.thickness, focused);
		this.vertex(buffer, right, y, focused);
		this.vertex(buffer, x, y, focused);
		// Right border
		this.vertex(buffer, right - this.thickness, bottom, focused);
		this.vertex(buffer, right, bottom, focused);
		this.vertex(buffer, right, y, focused);
		this.vertex(buffer, right - this.thickness, y, focused);
		// Bottom
		this.vertex(buffer, x, bottom, focused);
		this.vertex(buffer, right, bottom, focused);
		this.vertex(buffer, right, bottom - this.thickness, focused);
		this.vertex(buffer, x, bottom - this.thickness, focused);
		// Left border
		this.vertex(buffer, x, bottom, focused);
		this.vertex(buffer, x + this.thickness, bottom, focused);
		this.vertex(buffer, x + this.thickness, y, focused);
		this.vertex(buffer, x, y, focused);
		BufferRenderer.drawWithGlobalProgram(buffer.end());

	}

	private void vertex(BufferBuilder buffer, int x, int y, boolean focused) {
		int[] color = focused ? this.focusedColor : this.color;
		buffer.vertex(x, y, 0).color(color[0], color[1], color[2], color[3]);
	}

	@Override
	public int getThickness() {
		return this.thickness;
	}

	@Override
	public String toString() {
		return "SimpleBorder{" +
				"thickness=" + this.thickness +
				", color=" + Arrays.toString(this.color) +
				", focusedColor=" + Arrays.toString(this.focusedColor) +
				'}';
	}
}
