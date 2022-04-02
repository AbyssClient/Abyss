package de.vincentschweiger.phantomclient.modules;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.NbtText;

public class ModulePositionScreen extends Screen {

    private UIModule draggedModule = null;

    public ModulePositionScreen() {
        super(LiteralText.EMPTY);
    }

    @Override
    protected void init() {
        Modules.getRegisteredModules().forEach(this::checkOutOfBounds);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float pTicks) {
        renderBackground(matrixStack);
        Modules.getRegisteredModules().forEach(m -> m.render(m.isEnabled()));
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double distX, double distY) {
        if (draggedModule != null)
            if (((draggedModule.getX() + draggedModule.getWidth()) + distX < width) && ((draggedModule.getY() + draggedModule.getHeight()) + distY < height) && (draggedModule.getX() + distX >= 0) && (draggedModule.getY() + distY > 0))
                draggedModule.setPosition(draggedModule.getX() + distX, draggedModule.getY() + distY);
        return super.mouseDragged(mouseX, mouseY, button, distX, distY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Left click to drag
        // Right click to toggle
        if (button == 0) {
            Modules.getRegisteredModules().forEach(m -> {
                if (mouseX >= m.getX() && mouseY >= m.getY() && mouseX <= m.getX() + m.getWidth() && mouseY <= m.getY() + m.getHeight())
                    this.draggedModule = m;
            });
        } else if (button == 1) {
            Modules.getRegisteredModules().forEach(m -> {
                if (mouseX >= m.getX() && mouseY >= m.getY() && mouseX <= m.getX() + m.getWidth() && mouseY <= m.getY() + m.getHeight()) {
                    m.switchEnabled();
                }
            });
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0)
            this.draggedModule = null;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void close() {
        super.close();
        Modules.getRegisteredModules().forEach(UIModule::save);
    }

    private void checkOutOfBounds(UIModule m) {
        if (m.getX() > width - m.getWidth() + 2 || m.getY() > height - m.getHeight() + 2 || m.getX() < 0 || m.getY() < 0)
            m.setPosition(0, 0);
    }
}