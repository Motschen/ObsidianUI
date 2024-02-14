/*
 * Copyright © 2020~2024 LambdAurora <email@lambdaurora.dev>
 * Copyright © 2024 ThinkingStudio
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.thinkingstudio.obsidianui.option;

import org.thinkingstudio.obsidianui.Position;
import org.thinkingstudio.obsidianui.widget.SpruceCheckboxWidget;
import org.thinkingstudio.obsidianui.widget.SpruceWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Represents a boolean option.
 * <p>
 * Works the as {@link SpruceBooleanOption} but uses a checkbox instead.
 *
 * @author LambdAurora
 * @version 2.1.0
 * @since 1.6.0
 */
public class SpruceCheckboxBooleanOption extends SpruceBooleanOption {
    public SpruceCheckboxBooleanOption(String key, Supplier<Boolean> getter, Consumer<Boolean> setter, @Nullable Text tooltip) {
        super(key, getter, setter, tooltip);
    }

    public SpruceCheckboxBooleanOption(String key, Supplier<Boolean> getter, Consumer<Boolean> setter, @Nullable Text tooltip, boolean colored) {
        super(key, getter, setter, tooltip, colored);
    }

    @Override
    public SpruceWidget createWidget(Position position, int width) {
        SpruceCheckboxWidget button = new SpruceCheckboxWidget(position, width, 20, this.getDisplayText(), (btn, newValue) -> {
            this.set();
            btn.setMessage(this.getDisplayText());
        }, this.get());
        button.setColored(this.isColored());
        this.getOptionTooltip().ifPresent(button::setTooltip);
        return button;
    }

    @Override
    public Text getDisplayText() {
        return this.getPrefix();
    }

    @Override
    public Text getDisplayText(@NotNull Text value) {
        return this.getPrefix();
    }
}
