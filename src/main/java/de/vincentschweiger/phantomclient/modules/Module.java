package de.vincentschweiger.phantomclient.modules;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.vincentschweiger.phantomclient.Mod;
import de.vincentschweiger.phantomclient.modules.settings.impl.BooleanSetting;
import de.vincentschweiger.phantomclient.modules.settings.impl.DoubleSetting;
import de.vincentschweiger.phantomclient.modules.settings.impl.Setting;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public abstract class Module {
    private boolean enabled = true;
    public Map<String, Setting> settings = new HashMap<>();

    public Module() {
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public abstract String getName();

    public void switchEnabled() {
        this.enabled = !this.enabled;
    }

    public void save() {
        try {
            File moduleConf = new File(Mod.CONFIG_DIR, getName() + ".json");
            Mod.getGson().toJson(settings);
            if (!Mod.CONFIG_DIR.exists()) Mod.CONFIG_DIR.mkdir();
            if (!moduleConf.exists()) moduleConf.createNewFile();
            Files.writeString(Path.of(moduleConf.getAbsolutePath()), Mod.getGson().toJson(settings));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        try {
            File moduleConf = new File(Mod.CONFIG_DIR, getName() + ".json");
            if (moduleConf.exists()) {
                String content = Files.readString(Path.of(moduleConf.getAbsolutePath()));
                JsonObject obj = JsonParser.parseString(content).getAsJsonObject();
                for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                    String type = entry.getValue().getAsJsonObject().get("type").getAsString();
                    switch (type) {
                        case "boolean":
                            settings.put(entry.getKey(), new BooleanSetting(
                                    entry.getValue().getAsJsonObject().get("value")
                                            .getAsBoolean()
                            ));
                            break;
                        case "double":
                            settings.put(entry.getKey(), new DoubleSetting(
                                    entry.getValue().getAsJsonObject().get("value")
                                            .getAsDouble()
                            ));
                            break;
                        default:
                            Mod.LOGGER.warn("Unrecognized setting type in " + moduleConf.getName());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
