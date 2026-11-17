package com.lowhpwarning;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public final class WarningOverlay {
    private static float lastHealth = Float.NaN;
    private static String lastText;

    private WarningOverlay() { }

    public static void render(GuiGraphics graphics) {
        Minecraft mc = Minecraft.getInstance();
        var player = mc.player;
        if (player == null || mc.level == null || mc.screen != null || mc.options.hideGui || player.isSpectator()) return;

        LowHpConfig config = LowHpConfig.get();
        float health = player.getHealth();
        if (!LowHealth.shouldShow(config.enabled, health, config.threshold,
                player.isAlive(), false, false, false)) return;

        if (health != lastHealth) {
            lastHealth = health;
            lastText = LowHealth.display(health);
        }

        int x = graphics.guiWidth() / 2;
        int y = graphics.guiHeight() / 2 + 20;
        int w = mc.font.width(lastText) + 14;
        int left = x - w / 2;
        int right = left + w;

        graphics.fill(left + 1, y - 3, right - 1, y + 11, 0xDA190D15);
        graphics.fill(left, y - 1, right, y + 9, 0xDA190D15);
        graphics.fill(left + 1, y - 3, right - 1, y - 2, 0xADFF6279);
        graphics.fill(left + 1, y + 10, right - 1, y + 11, 0x59FF6279);
        graphics.drawCenteredString(mc.font, lastText, x, y, 0xFFFFA6B2);
    }
}
