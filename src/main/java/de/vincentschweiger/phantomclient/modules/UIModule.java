package de.vincentschweiger.phantomclient.modules;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.vincentschweiger.phantomclient.Mod;
import de.vincentschweiger.phantomclient.modules.settings.impl.DoubleSetting;
import de.vincentschweiger.phantomclient.modules.settings.impl.Setting;
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

public abstract class UIModule extends Module {

    private double x, y;
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

    public int getWidth() {
        return MinecraftClient.getInstance().textRenderer.getWidth(getText());
    }

    public int getHeight() {
        return 9;
    }

    public void save() {
        settings.put("x", new DoubleSetting(getX()));
        settings.put("y", new DoubleSetting(getY()));
        super.save();
    }


    public void load() {
        super.load();
        double x = ((DoubleSetting) settings.get("x")).get();
        double y = ((DoubleSetting) settings.get("y")).get();
        setPosition(x, y);
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