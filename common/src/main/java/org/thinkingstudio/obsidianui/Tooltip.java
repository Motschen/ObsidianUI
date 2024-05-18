/*
 * Copyright © 2020~2024 LambdAurora <email@lambdaurora.dev>
 * Copyright © 2024 ThinkingStudio
 *
 * This file is part of ObsidianUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.thinkingstudio.obsidianui;

import com.google.common.collect.Queues;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.HoveredTooltipPositioner;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import org.jetbrains.annotations.ApiStatus;
import org.thinkingstudio.obsidianui.widget.SpruceWidget;

import java.util.List;
import java.util.Queue;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;

/**
 * Represents a tooltip.
 *
 * @author LambdAurora
 * @version 5.0.0
 * @since 1.0.0
 */
public class Tooltip implements SprucePositioned {
	private static final Queue<Tooltip> TOOLTIPS = Queues.newConcurrentLinkedQueue();
	private static boolean delayed = false;
	private final int x;
	private final int y;
	private final List<OrderedText> tooltip;

	public Tooltip(int x, int y, String tooltip, int parentWidth) {
		this(x, y, StringVisitable.plain(tooltip), parentWidth);
	}

	public Tooltip(int x, int y, StringVisitable tooltip, int parentWidth) {
		this(x, y, MinecraftClient.getInstance().textRenderer.wrapLines(tooltip, Math.max(parentWidth * 2 / 3, 200)));
	}

	public Tooltip(int x, int y, List<OrderedText> tooltip) {
		this.x = x;
		this.y = y;
		this.tooltip = tooltip;
	}

	public static Tooltip create(int x, int y, String tooltip, int parentWidth) {
		return new Tooltip(x, y, tooltip, parentWidth);
	}

	public static Tooltip create(int x, int y, StringVisitable tooltip, int parentWidth) {
		return new Tooltip(x, y, tooltip, parentWidth);
	}

	public static Tooltip create(int x, int y, List<OrderedText> tooltip) {
		return new Tooltip(x, y, tooltip);
	}

	@Override
	public int getX() {
		return this.x;
	}

	@Override
	public int getY() {
		return this.y;
	}

	/**
	 * Returns whether the tooltip should render or not.
	 *
	 * @return {@code true} if the tooltip should render, else {@code false}
	 */
	public boolean shouldRender() {
		return !this.tooltip.isEmpty();
	}

	/**
	 * Renders the tooltip.
	 *
	 * @param drawContext The DrawContext instance used to render.
	 */
	public void render(DrawContext drawContext) {
		drawContext.drawTooltip(MinecraftClient.getInstance().textRenderer, this.tooltip, HoveredTooltipPositioner.INSTANCE, this.x, this.y);
	}

	/**
	 * Queues the tooltip to render.
	 */
	public void queue() {
		TOOLTIPS.add(this);
	}

	/**
	 * Queues the tooltip of the widget to render.
	 *
	 * @param widget the widget
	 * @param mouseX the mouse X coordinate
	 * @param mouseY the mouse Y coordinate
	 * @param <T> the type of the widget
	 * @since 1.6.0
	 */
	public static <T extends Tooltipable & SpruceWidget> void queueFor(T widget, int mouseX, int mouseY, int tooltipTicks,
                                                                       IntConsumer tooltipTicksSetter,
                                                                       long lastTick,
                                                                       LongConsumer lastTickSetter) {
		if (widget.isVisible()) {
			widget.getTooltip().ifPresent(tooltip -> {
				long currentRender = System.currentTimeMillis();
				if (lastTick != 0) {
					if (currentRender - lastTick >= 20) {
						tooltipTicksSetter.accept(tooltipTicks + 1);
						lastTickSetter.accept(currentRender);
					}
				} else lastTickSetter.accept(currentRender);

				if (!widget.isFocused() && !widget.isMouseHovered())
					tooltipTicksSetter.accept(0);

				if (!tooltip.getString().isEmpty() && tooltipTicks >= 45) {
					var wrappedTooltipText = MinecraftClient.getInstance().textRenderer.wrapLines(
							tooltip, Math.max(widget.getWidth() * 2 / 3, 200));
					if (widget.isMouseHovered())
						create(mouseX, mouseY, wrappedTooltipText).queue();
					else if (widget.isFocused())
						create(widget.getX() - 12, widget.getY() + widget.getHeight() + 16,
								wrappedTooltipText)
								.queue();
				}
			});
		}
	}

	/**
	 * Sets whether tooltip rendering is delayed or not.
	 *
	 * @param delayed true if tooltip rendering is delayed
	 */
	@ApiStatus.Internal
	static void setDelayedRender(boolean delayed) {
		Tooltip.delayed = delayed;
	}

	/**
	 * Renders all the tooltips.
	 *
	 * @param drawContext the GUI drawContext to render from
	 */
	public static void renderAll(DrawContext drawContext) {
		if (delayed)
			return;
		synchronized (TOOLTIPS) {
			Tooltip tooltip;

			while ((tooltip = TOOLTIPS.poll()) != null)
				tooltip.render(drawContext);
		}
	}
}
