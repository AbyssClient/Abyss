package me.cookie.abyssclient.mixins.gui;

import me.cookie.abyssclient.event.EventManager;
import me.cookie.abyssclient.event.OverlayRenderEvent;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class MixinInGameHud {

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderAutosaveIndicator(Lnet/minecraft/client/util/math/MatrixStack;)V", shift = At.Shift.AFTER))
    public void inject(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        EventManager.INSTANCE.callEvent(new OverlayRenderEvent(matrices, tickDelta));
    }
}