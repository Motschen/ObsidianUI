/*
 * Copyright © 2020~2024 LambdAurora <email@lambdaurora.dev>
 * Copyright © 2024 ThinkingStudio
 *
 * This file is part of ObsidianUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.thinkingstudio.obsidianui.hud.component;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.thinkingstudio.obsidianui.hud.HudComponent;

/**
 * Represents a text HUD component.
 *
 * @author LambdAurora
 * @version 5.0.0
 * @since 1.3.5
 */
public class TextHudComponent extends HudComponent {
	protected MinecraftClient client;
	protected Text text;
	protected int color;

	public TextHudComponent(Identifier identifier, int x, int y, Text text) {
		this(identifier, x, y, text, 0xffffffff);
	}

	public TextHudComponent(Identifier identifier, int x, int y, Text text, int color) {
		super(identifier, x, y);
		this.client = MinecraftClient.getInstance();
		this.text = text;
		this.color = color;
	}

	/**
	 * Gets this component's text.
	 *
	 * @return the component's text
	 */
	public Text getText() {
		return this.text;
	}

	/**
	 * Sets this component's text.
	 *
	 * @param text the text
	 */
	public void setText(Text text) {
		this.text = text;
	}

	/**
	 * Gets this component's text color.
	 *
	 * @return the text color
	 */
	public int getColor() {
		return this.color;
	}

	/**
	 * Sets this component's text color.
	 *
	 * @param color the text color
	 */
	public void setColor(int color) {
		this.color = color;
	}

	@Override
	public void render(DrawContext drawContext, float tickDelta) {
		drawContext.drawTextWithShadow(this.client.textRenderer, this.text, this.x, this.y, this.color);
	}
}
