package de.vincentschweiger.phantomclient.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;

import java.awt.*;

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

    public int getWidth() {
        return MinecraftClient.getInstance().textRenderer.getWidth(getText());
    }

    public int getHeight() {
        return 9;
    }

    /**
     * Use this to save your positions etc to a config file.
     */
    public void save() {
        //Your save-logic
    }

    /**
     * Use this to load your positions etc from a config file.
     */

    public void load() {
        //Your load-logic
        save();
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