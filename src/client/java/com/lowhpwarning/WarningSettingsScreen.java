package com.lowhpwarning;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

final class WarningSettingsScreen extends Screen {
    private static final int ROW_WIDTH = 200;
    private final Screen parent;
    private boolean enabled;
    private EditBox threshold;
    private Button toggle;
    private Button done;
    private Button cancel;

    WarningSettingsScreen(Screen parent) {
        super(Component.literal("Low HP Warning Settings"));
        this.parent = parent;
        this.enabled = LowHpConfig.get().enabled;
    }

    @Override protected void init() {
        String value = threshold == null ? LowHealth.thresholdText(LowHpConfig.get().threshold) : threshold.getValue();
        toggle = addRenderableWidget(Button.builder(toggleText(), b -> {
            enabled = !enabled;
            b.setMessage(toggleText());
        }).size(ROW_WIDTH, 20).build());
        threshold = addRenderableWidget(new EditBox(font, 0, 0, ROW_WIDTH, 20, null,
                Component.literal("Warning threshold in HP")));
        threshold.setMaxLength(7);
        threshold.setFilter(text -> text.matches("[0-9]{0,4}(?:\\.[0-9]{0,2})?"));
        threshold.setHint(Component.literal("HP"));
        done = addRenderableWidget(Button.builder(Component.literal("Done"), button -> save()).size(98, 20).build());
        cancel = addRenderableWidget(Button.builder(Component.literal("Cancel"), button -> onClose()).size(98, 20).build());
        threshold.setResponder(text -> done.active = LowHealth.parseThreshold(text) != null);
        threshold.setValue(value);
        done.active = LowHealth.parseThreshold(value) != null;
        repositionElements();
    }

    private Component toggleText() {
        return Component.literal(enabled ? "Warning: ON" : "Warning: OFF");
    }

    private int contentTop() {
        return Math.max(42, height / 2 - 57);
    }

    @Override protected void repositionElements() {
        if (toggle == null) return;
        int x = (width - ROW_WIDTH) / 2;
        int y = contentTop();
        toggle.setPosition(x, y);
        threshold.setPosition(x, y + 45);
        done.setPosition(x, y + 94);
        cancel.setPosition(x + 102, y + 94);
    }

    @Override public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        int x = (width - ROW_WIDTH) / 2;
        int y = contentTop();
        graphics.drawCenteredString(font, getTitle(), width / 2, y - 24, 0xFFFFFFFF);
        graphics.drawString(font, "Show when HP is below", x, y + 32, 0xFFFFFFFF);
        boolean valid = threshold != null && LowHealth.parseThreshold(threshold.getValue()) != null;
        graphics.drawString(font, valid ? "1 heart = 2 HP  /  Default: 10"
                : "Enter a value from 0.01 to 1024 HP", x, y + 71, valid ? 0xFFAAAAAA : 0xFFFF8C9C);
    }

    private void save() {
        Float value = LowHealth.parseThreshold(threshold.getValue());
        if (value == null) return;
        LowHpConfig.get().apply(enabled, value);
        onClose();
    }

    @Override public void onClose() {
        if (minecraft != null) minecraft.setScreen(parent);
    }
}
