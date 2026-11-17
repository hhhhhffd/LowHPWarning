package com.lowhpwarning;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class LowHealth {
    public static final float DEFAULT_THRESHOLD = 10f;
    public static final float MAX_THRESHOLD = 1024f;
    public static final float MIN_THRESHOLD = 0.01f;

    private LowHealth() { }

    public static boolean shouldShow(boolean enabled, float health, float threshold, boolean alive,
                                     boolean spectator, boolean hudHidden, boolean menuOpen) {
        return enabled && alive && !spectator && !hudHidden && !menuOpen
                && Float.isFinite(health) && health > 0f && health < threshold;
    }

    public static boolean validThreshold(float value) {
        return Float.isFinite(value) && value >= MIN_THRESHOLD && value <= MAX_THRESHOLD;
    }

    public static String display(float health) {
        if (health < MIN_THRESHOLD) return "<0.01 HP";
        return new BigDecimal(Float.toString(health)).setScale(2, RoundingMode.FLOOR)
                .stripTrailingZeros().toPlainString() + " HP";
    }

    public static String thresholdText(float threshold) {
        return new BigDecimal(Float.toString(threshold)).stripTrailingZeros().toPlainString();
    }

    public static Float parseThreshold(String text) {
        if (!text.matches("[0-9]{0,4}(?:\\.[0-9]{0,2})?")) return null;
        try {
            float value = Float.parseFloat(text);
            return validThreshold(value) ? value : null;
        } catch (NumberFormatException ignored) {
            return null;
        }
    }
}
