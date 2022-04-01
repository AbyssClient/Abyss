package de.vincentschweiger.phantomclient.modules;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.vincentschweiger.phantomclient.Mod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import org.spongepowered.include.com.google.gson.JsonElement;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class UIModule {

    private double x, y;
    private boolean enabled = true; // assigned so it's enabled by default when not using a config. if you implemented load/save methods, un-assign this and assign it from those methods.
    private int state;
    private int maxState;
    public MatrixStack stack = new MatrixStack();

    public UIModule() {
    }

    public void render(boolean enabled) {
        stack.push();
        if (enabled) {
            MinecraftClient.getInstance().textRenderer.drawWithShadow(stack, getText(), (float) this.getX(), (float) this.getY(), getColor().getRGB());
        } else {
            //If you don't want to render the disabled modules in the drag-screen, just remove/comment following line
            MinecraftClient.getInstance().textRenderer.drawWithShadow(stack, new LiteralText(getText()).setStyle(Style.EMPTY.withStrikethrough(true)), (float) this.getX(), (float) this.getY(), getColor().getRGB());
        }
        stack.pop();
    }

    public String getText() {
        return "";
    }

    public Color getColor() {
        return Color.WHITE;
    }

    public double getX() {
        return x * MinecraftClient.getInstance().getWindow().getScaledWidth();
    }

    public double getY() {
        return y * MinecraftClient.getInstance().getWindow().getScaledHeight();
    }

    public void setPosition(double x, double y) {
        this.x = x / MinecraftClient.getInstance().getWindow().getScaledWidth();
        this.y = y / MinecraftClient.getInstance().getWindow().getScaledHeight();
    }

    public abstract String getName();

    public int getWidth() {
        return MinecraftClient.getInstance().textRenderer.getWidth(getText());
    }

    public int getHeight() {
        return 9;
    }

    public void save() {
        File moduleConf = new File(Mod.CONFIG_DIR, getName() + ".json");
        JsonObject obj = new JsonObject();
        obj.addProperty("x", getX());
        obj.addProperty("y", getY());
        try {
            if (!Mod.CONFIG_DIR.exists()) Mod.CONFIG_DIR.mkdir();
            if (!moduleConf.exists()) moduleConf.createNewFile();
            Files.writeString(Path.of(moduleConf.getAbsolutePath()), obj.toString());
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
                setPosition(obj.get("x").getAsDouble(), obj.get("y").getAsDouble());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void switchEnabled() {
        this.enabled = !this.enabled;
    }

    public int getState() {
        return state;
    }

    public void incrState() {
        if (this.state < this.maxState) this.state++;
        else this.state = 0;
    }

    public int getMaxState() {
        return maxState;
    }
}