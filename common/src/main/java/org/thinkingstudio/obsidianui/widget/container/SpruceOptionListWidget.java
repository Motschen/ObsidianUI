/*
 * Copyright © 2020~2024 LambdAurora <email@lambdaurora.dev>
 * Copyright © 2024 ThinkingStudio
 *
 * This file is part of ObsidianUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.thinkingstudio.obsidianui.widget.container;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.thinkingstudio.obsidianui.Position;
import org.thinkingstudio.obsidianui.navigation.NavigationDirection;
import org.thinkingstudio.obsidianui.navigation.NavigationUtils;
import org.thinkingstudio.obsidianui.option.SpruceOption;
import org.thinkingstudio.obsidianui.widget.AbstractSpruceWidget;
import org.thinkingstudio.obsidianui.widget.SpruceWidget;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a list widget holding {@link SpruceOption} entries.
 * <p>
 * A {@link SpruceOption} allows to have an easy control over the widgets present in the list.
 *
 * @author LambdAurora
 * @version 5.0.0
 * @since 2.0.0
 */
public class SpruceOptionListWidget extends SpruceEntryListWidget<SpruceOptionListWidget.OptionEntry> {
	private int lastIndex = 0;

	public SpruceOptionListWidget(Position position, int width, int height) {
		super(position, width, height, 4, OptionEntry.class);
	}

	/**
	 * Adds a single option entry. The option will use all the width available.
	 *
	 * @param option the option
	 * @return the index of the added entry
	 */
	public int addSingleOptionEntry(SpruceOption option) {
		return this.addEntry(OptionEntry.create(this, option, false));
	}

	/**
	 * Adds a single option entry. The option will center the element and will not use the full width.
	 *
	 * @param option the option
	 * @return the index of the added entry
	 */
	public int addSmallSingleOptionEntry(SpruceOption option) {
		return this.addEntry(OptionEntry.create(this, option, true));
	}

	/**
	 * Adds two option as one entry of the list. The second option can be {@code null}.
	 * <p>
	 * If no second option is specified, the first option will not use the full width.
	 *
	 * @param firstOption the first option
	 * @param secondOption the second option
	 */
	public void addOptionEntry(SpruceOption firstOption, @Nullable SpruceOption secondOption) {
		this.addEntry(OptionEntry.create(this, firstOption, secondOption));
	}

	public void addAll(SpruceOption[] options) {
		for (int i = 0; i < options.length; i += 2) {
			this.addOptionEntry(options[i], i < options.length - 1 ? options[i + 1] : null);
		}
	}

	/* Narration */

	@Override
	public void appendNarrations(NarrationMessageBuilder builder) {
		this.children()
				.stream()
				.filter(AbstractSpruceWidget::isMouseHovered)
				.findFirst()
				.ifPresentOrElse(
						hoveredEntry -> {
							hoveredEntry.appendNarrations(builder.nextMessage());
							this.appendPositionNarrations(builder, hoveredEntry);
						}, () -> {
							var focusedEntry = this.getFocused();
							if (focusedEntry != null) {
								focusedEntry.appendNarrations(builder.nextMessage());
								this.appendPositionNarrations(builder, focusedEntry);
							}
						}
				);

		builder.put(NarrationPart.USAGE, Text.translatable("narration.component_list.usage"));
	}

	public static class OptionEntry extends Entry implements SpruceParentWidget<SpruceWidget> {
		private final List<SpruceWidget> children = new ArrayList<>();
		private final SpruceOptionListWidget parent;
		private @Nullable SpruceWidget focused;
		private boolean dragging;

		private OptionEntry(SpruceOptionListWidget parent) {
			this.parent = parent;
		}

		public static OptionEntry create(SpruceOptionListWidget parent, SpruceOption option, boolean small) {
			var entry = new OptionEntry(parent);
			entry.children.add(option.createWidget(Position.of(entry, entry.getWidth() / 2 - (small ? 75 : 155), 2),
					small ? 150 : 310));
			return entry;
		}

		public static OptionEntry create(SpruceOptionListWidget parent, SpruceOption firstOption, @Nullable SpruceOption secondOption) {
			var entry = new OptionEntry(parent);
			entry.children.add(firstOption.createWidget(Position.of(entry, entry.getWidth() / 2 - 155, 2), 150));
			if (secondOption != null) {
				entry.children.add(secondOption.createWidget(Position.of(entry, entry.getWidth() / 2 - 155 + 160, 2), 150));
			}
			return entry;
		}

		@Override
		public int getWidth() {
			return this.parent.getWidth() - (this.parent.getBorder().getThickness() * 2);
		}

		@Override
		public int getHeight() {
			return this.children.stream().mapToInt(SpruceWidget::getHeight).reduce(Integer::max).orElse(0) + 4;
		}

		@Override
		public List<SpruceWidget> children() {
			return this.children;
		}

		@Override
		public @Nullable SpruceWidget getFocused() {
			return this.focused;
		}

		@Override
		public void setFocused(@Nullable SpruceWidget focused) {
			if (this.focused == focused)
				return;
			if (this.focused != null)
				this.focused.setFocused(false);
			this.focused = focused;
		}

		@Override
		public void setFocused(boolean focused) {
			super.setFocused(focused);
			if (!focused) {
				this.setFocused(null);
			}
		}

		/* Input */

		@Override
		protected boolean onMouseClick(double mouseX, double mouseY, int button) {
			var it = this.iterator();

			SpruceWidget element;
			do {
				if (!it.hasNext()) {
					return false;
				}

				element = it.next();
			} while (!element.mouseClicked(mouseX, mouseY, button));

			this.setFocused(element);
			if (button == GLFW.GLFW_MOUSE_BUTTON_1)
				this.dragging = true;

			return true;
		}

		@Override
		protected boolean onMouseRelease(double mouseX, double mouseY, int button) {
			this.dragging = false;
			return this.hoveredElement(mouseX, mouseY).filter(element -> element.mouseReleased(mouseX, mouseY, button)).isPresent();
		}

		@Override
		protected boolean onMouseDrag(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
			return this.getFocused() != null && this.dragging && button == GLFW.GLFW_MOUSE_BUTTON_1
					&& this.getFocused().mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
		}

		@Override
		protected boolean onKeyPress(int keyCode, int scanCode, int modifiers) {
			return this.focused != null && this.focused.keyPressed(keyCode, scanCode, modifiers);
		}

		@Override
		protected boolean onKeyRelease(int keyCode, int scanCode, int modifiers) {
			return this.focused != null && this.focused.keyReleased(keyCode, scanCode, modifiers);
		}

		@Override
		protected boolean onCharTyped(char chr, int keyCode) {
			return this.focused != null && this.focused.charTyped(chr, keyCode);
		}

		/* Rendering */

		@Override
		protected void renderWidget(DrawContext drawContext, int mouseX, int mouseY, float delta) {
			this.forEach(widget -> widget.render(drawContext, mouseX, mouseY, delta));
		}

		/* Narration */

		@Override
		public void appendNarrations(NarrationMessageBuilder builder) {
			var focused = this.getFocused();
			if (focused != null) focused.appendNarrations(builder);
		}

		/* Navigation */

		@Override
		public boolean onNavigation(NavigationDirection direction, boolean tab) {
			if (this.requiresCursor()) return false;
			if (!tab && direction.isVertical()) {
				if (this.isFocused()) {
					this.setFocused(null);
					return false;
				}
				int lastIndex = this.parent.lastIndex;
				if (lastIndex >= this.children.size())
					lastIndex = this.children.size() - 1;
				if (!this.children.get(lastIndex).onNavigation(direction, tab))
					return false;
				this.setFocused(this.children.get(lastIndex));
				return true;
			}

			boolean result = NavigationUtils.tryNavigate(direction, tab, this.children, this.focused, this::setFocused, true);
			if (result) {
				this.setFocused(true);
				if (direction.isHorizontal() && this.getFocused() != null) {
					this.parent.lastIndex = this.children.indexOf(this.getFocused());
				}
			}
			return result;
		}
	}
}
