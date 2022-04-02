package de.vincentschweiger.phantomclient.mixins;

import de.vincentschweiger.phantomclient.event.EventManager;
import de.vincentschweiger.phantomclient.event.GameRenderEvent;
import de.vincentschweiger.phantomclient.event.ScreenRenderEvent;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {
    /**
     * Hook game render event
     */
    @Inject(method = "render", at = @At("HEAD"))
    public void hookGameRender(CallbackInfo callbackInfo) {
        EventManager.INSTANCE.callEvent(new GameRenderEvent());
    }

    /**
     * Hook screen render event
     */
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V"))
    public void hookScreenRender(Screen screen, MatrixStack matrices, int mouseX, int mouseY, float delta) {
        screen.render(matrices, mouseX, mouseY, delta);
        EventManager.INSTANCE.callEvent(new ScreenRenderEvent(screen, matrices, mouseX, mouseY, delta));
    }

}
