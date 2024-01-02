/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.thinkingstudio.obsidianui.option;

import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.thinkingstudio.obsidianui.Position;
import org.thinkingstudio.obsidianui.widget.SpruceButtonWidget;
import org.thinkingstudio.obsidianui.widget.SpruceWidget;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Represents a cycling option.
 * <p>
 * Works the same as the vanilla one but can provide a tooltip.
 *
 * @author LambdAurora
 * @version 3.0.0
 * @since 1.0.0
 */
public class SpruceCyclingOption extends SpruceOption {
	private final Consumer<Integer> setter;
	private final Function<SpruceCyclingOption, Text> messageProvider;

	public SpruceCyclingOption(String key, Consumer<Integer> setter, Function<SpruceCyclingOption, Text> messageProvider, @Nullable Text tooltip) {
		super(key);
		this.setter = setter;
		this.messageProvider = messageProvider;
		this.setTooltip(tooltip);
	}

	/**
	 * Cycles the option.
	 *
	 * @param amount The amount to cycle.
	 */
	public void cycle(int amount) {
		this.setter.accept(amount);
	}

	@Override
	public SpruceWidget createWidget(Position position, int width) {
		var button = new SpruceButtonWidget(position, width, 20, this.getMessage(), btn -> {
			this.cycle(1);
			btn.setMessage(this.getMessage());
		});
		this.getOptionTooltip().ifPresent(button::setTooltip);
		return button;
	}

	/**
	 * Gets the option message.
	 *
	 * @return The option message.
	 */
	public Text getMessage() {
		return this.messageProvider.apply(this);
	}
}
