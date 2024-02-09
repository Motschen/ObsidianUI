/*
 * Copyright © 2020-2022 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.thinkingstudio.obsidianui.border;

import org.thinkingstudio.obsidianui.widget.SpruceWidget;
import net.minecraft.client.util.math.MatrixStack;

/**
 * Represents a border to draw around a widget.
 *
 * @author LambdAurora
 * @version 3.1.0
 * @since 2.0.0
 */
public interface Border {
	void render(MatrixStack matrices, SpruceWidget widget, int mouseX, int mouseY, float delta);

	/**
	 * Returns the thickness of the border.
	 *
	 * @return the thickness
	 */
	int getThickness();
}
