/*
 * Copyright © 2020~2024 LambdAurora <email@lambdaurora.dev>
 * Copyright © 2024 ThinkingStudio
 *
 * This file is part of ObsidianUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.thinkingstudio.obsidianui.screen;

import org.thinkingstudio.obsidianui.SprucePositioned;
import org.thinkingstudio.obsidianui.Tooltip;
import org.thinkingstudio.obsidianui.navigation.NavigationDirection;
import org.thinkingstudio.obsidianui.util.ScissorManager;
import org.thinkingstudio.obsidianui.widget.SpruceElement;
import org.thinkingstudio.obsidianui.widget.SpruceWidget;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

/**
 * Represents an handled screen.
 *
 * @param <T> the type of the screen handler
 * @author LambdAurora
 * @version 3.3.0
 * @since 3.3.0
 */
public abstract class SpruceHandledScreen<T extends ScreenHandler> extends HandledScreen<T> implements SprucePositioned, SpruceElement {
	protected double scaleFactor;

	public SpruceHandledScreen(T handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	@Override
	public void setFocused(Element focused) {
		var old = this.getFocused();
		if (old == focused) return;
		if (old instanceof SpruceWidget)
			((SpruceWidget) old).setFocused(false);
		super.setFocused(focused);
		if (focused instanceof SpruceWidget)
			((SpruceWidget) focused).setFocused(true);
	}

	@Override
	protected void init() {
		this.scaleFactor = this.client.getWindow().getScaleFactor();
	}

	/* Input */

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		return super.keyPressed(keyCode, scanCode, modifiers)
				|| NavigationDirection.fromKey(keyCode, Screen.hasShiftDown())
				.map(dir -> this.onNavigation(dir, keyCode == GLFW.GLFW_KEY_TAB))
				.orElseGet(() -> super.keyPressed(keyCode, scanCode, modifiers));
	}

	/* Navigation */

	@Override
	public boolean onNavigation(NavigationDirection direction, boolean tab) {
		if (this.requiresCursor()) return false;
		var focused = this.getFocused();
		boolean isNonNull = focused != null;
		if (!isNonNull || !this.tryNavigating(focused, direction, tab)) {
			var children = this.children();
			int i = children.indexOf(focused);
			int next;
			if (isNonNull && i >= 0) next = i + (direction.isLookingForward() ? 1 : 0);
			else if (direction.isLookingForward()) next = 0;
			else next = children.size();

			var iterator = children.listIterator(next);
			BooleanSupplier hasNext = direction.isLookingForward() ? iterator::hasNext : iterator::hasPrevious;
			Supplier<Element> nextGetter = direction.isLookingForward() ? iterator::next : iterator::previous;

			Element nextElement;
			do {
				if (!hasNext.getAsBoolean()) {
					this.setFocused(null);
					return false;
				}

				nextElement = nextGetter.get();
			} while (!this.tryNavigating(nextElement, direction, tab));

			this.setFocused(nextElement);
		}
		return true;
	}

	private boolean tryNavigating(Element element, NavigationDirection direction, boolean tab) {
		if (element instanceof SpruceElement) {
			return ((SpruceElement) element).onNavigation(direction, tab);
		}
		return element.changeFocus(direction.isLookingForward());
	}

	/* Render */

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		ScissorManager.pushScaleFactor(this.scaleFactor);
		super.render(matrices, mouseX, mouseY, delta);
		this.renderWidgets(matrices, mouseX, mouseY, delta);
		this.renderTitle(matrices, mouseX, mouseY, delta);
		Tooltip.renderAll(this, matrices);
		ScissorManager.popScaleFactor();
	}

	public void renderTitle(MatrixStack matrices, int mouseX, int mouseY, float delta) {
	}

	public void renderWidgets(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		for (var element : this.children()) {
			if (element instanceof Drawable drawable)
				drawable.render(matrices, mouseX, mouseY, delta);
		}
	}
}
