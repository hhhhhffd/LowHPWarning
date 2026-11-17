package com.lowhpwarning;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class LowHpConfig {
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("lowhpwarning.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Logger LOGGER = LoggerFactory.getLogger("Low HP Warning");
    private static LowHpConfig current;

    boolean enabled = true;
    float threshold = LowHealth.DEFAULT_THRESHOLD;

    static LowHpConfig get() {
        if (current == null) current = load();
        return current;
    }

    private static LowHpConfig load() {
        if (!Files.isRegularFile(FILE)) return new LowHpConfig();
        try (var reader = Files.newBufferedReader(FILE)) {
            LowHpConfig saved = GSON.fromJson(reader, LowHpConfig.class);
            if (saved != null) {
                if (!LowHealth.validThreshold(saved.threshold)) saved.threshold = LowHealth.DEFAULT_THRESHOLD;
                return saved;
            }
        } catch (IOException | RuntimeException e) {
            LOGGER.warn("Could not read Low HP Warning settings; using defaults", e);
        }
        return new LowHpConfig();
    }

    void apply(boolean enabled, float threshold) {
        this.enabled = enabled;
        this.threshold = threshold;
        Path temp = null;
        try {
            Files.createDirectories(FILE.getParent());
            temp = Files.createTempFile(FILE.getParent(), "lowhpwarning-", ".tmp");
            try (var writer = Files.newBufferedWriter(temp)) {
                GSON.toJson(this, writer);
            }
            try {
                Files.move(temp, FILE, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException ignored) {
                Files.move(temp, FILE, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            LOGGER.warn("Could not save Low HP Warning settings", e);
        } finally {
            if (temp != null) {
                try { Files.deleteIfExists(temp); } catch (IOException ignored) { }
            }
        }
    }
}
