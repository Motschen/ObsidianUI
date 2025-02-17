/*
 * Copyright © 2020~2024 LambdAurora <email@lambdaurora.dev>
 * Copyright © 2024 ThinkingStudio
 *
 * This file is part of ObsidianUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.thinkingstudio.obsidianui.widget;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.thinkingstudio.obsidianui.Position;

public class SpruceIconButtonWidget extends AbstractSpruceIconButtonWidget {
	public SpruceIconButtonWidget(Position position, int width, int height, Text message, PressAction action) {
		super(position, width, height, message, action);
	}

	/**
	 * Renders the icon of the button.
	 *
	 * @return the x-offset the icon creates
	 */
	protected int renderIcon(DrawContext drawContext, int mouseX, int mouseY, float delta) {
		return 0;
	}
}
