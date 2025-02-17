/*
 * Copyright © 2020~2024 LambdAurora <email@lambdaurora.dev>
 * Copyright © 2024 ThinkingStudio
 *
 * This file is part of ObsidianUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.thinkingstudio.obsidianui.widget.text;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.thinkingstudio.obsidianui.Position;
import org.thinkingstudio.obsidianui.background.Background;
import org.thinkingstudio.obsidianui.background.SimpleColorBackground;
import org.thinkingstudio.obsidianui.border.Border;
import org.thinkingstudio.obsidianui.border.SimpleBorder;
import org.thinkingstudio.obsidianui.util.ColorUtil;
import org.thinkingstudio.obsidianui.widget.AbstractSpruceWidget;
import org.thinkingstudio.obsidianui.widget.WithBackground;
import org.thinkingstudio.obsidianui.widget.WithBorder;

/**
 * Represents a text input widget.
 *
 * @author LambdAurora
 * @version 5.0.0
 * @since 2.1.0
 */
public abstract class AbstractSpruceTextInputWidget extends AbstractSpruceWidget implements WithBackground, WithBorder {
	private final Text title;
	private Background background = new SimpleColorBackground(ColorUtil.BLACK);
	private Border border = new SimpleBorder(1, -6250336, ColorUtil.WHITE);

	private int editableColor = ColorUtil.TEXT_COLOR;
	private int uneditableColor = ColorUtil.UNEDITABLE_COLOR;

	public AbstractSpruceTextInputWidget(Position position, int width, int height, Text title) {
		super(position);
		this.width = width;
		this.height = height;
		this.title = title;
	}

	/**
	 * Returns the text from the text input widget.
	 *
	 * @return the text
	 */
	public abstract String getText();

	/**
	 * Sets the text in the text input widget.
	 *
	 * @param text the text
	 */
	public abstract void setText(String text);

	/**
	 * Returns the title of this text input widget.
	 *
	 * @return the title
	 */
	public Text getTitle() {
		return this.title;
	}

	/**
	 * Returns the color for editable text.
	 *
	 * @return the editable text
	 */
	public int getEditableColor() {
		return this.editableColor;
	}

	/**
	 * Sets the color for editable text.
	 *
	 * @param editableColor the editable color
	 */
	public void setEditableColor(int editableColor) {
		this.editableColor = editableColor;
	}

	/**
	 * Returns the color for uneditable text.
	 *
	 * @return the uneditable color
	 */
	public int getUneditableColor() {
		return this.uneditableColor;
	}

	/**
	 * Sets the color for uneditable text.
	 *
	 * @param uneditableColor the uneditable color
	 */
	public void setUneditableColor(int uneditableColor) {
		this.uneditableColor = uneditableColor;
	}

	/**
	 * Returns the text color.
	 *
	 * @return the text color
	 */
	public int getTextColor() {
		return this.isActive() ? this.getEditableColor() : this.getUneditableColor();
	}

	/**
	 * Sets the cursor to the start of the text.
	 */
	public abstract void setCursorToStart();

	/**
	 * Sets the cursor to the end of the text.
	 */
	public abstract void setCursorToEnd();

	@Override
	public Background getBackground() {
		return this.background;
	}

	@Override
	public void setBackground(Background background) {
		this.background = background;
	}

	@Override
	public Border getBorder() {
		return this.border;
	}

	@Override
	public void setBorder(Border border) {
		this.border = border;
	}

	/**
	 * Returns the inner width of the text input widget.
	 *
	 * @return the inner width
	 */
	public int getInnerWidth() {
		return this.getWidth() - 6 - this.getBorder().getThickness() * 2;
	}

	/**
	 * Returns the inner height of the text input widget.
	 *
	 * @return the inner height
	 */
	public int getInnerHeight() {
		return this.getHeight() - 6 - this.getBorder().getThickness() * 2;
	}

	/**
	 * Sanitizes the text input widget.
	 */
	protected abstract void sanitize();

	public boolean isEditorActive() {
		return this.isActive() && this.isFocused();
	}

	/* Rendering */

	@Override
	protected void renderWidget(DrawContext drawContext, int mouseX, int mouseY, float delta) {
		this.getBorder().render(drawContext, this, mouseX, mouseY, delta);
	}

	@Override
	protected void renderBackground(DrawContext drawContext, int mouseX, int mouseY, float delta) {
		this.getBackground().render(drawContext, this, 0, mouseX, mouseY, delta);
	}

	/* Narration */

	@Override
	protected Text getNarrationMessage() {
		return Text.translatable("gui.narrate.editBox", this.getTitle(), this.getText());
	}
}
