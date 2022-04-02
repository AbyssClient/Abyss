package de.vincentschweiger.phantomclient.mixins;

import de.vincentschweiger.phantomclient.events.impl.MouseButtonEvent;
import de.vincentschweiger.phantomclient.events.impl.MouseCursorEvent;
import de.vincentschweiger.phantomclient.events.impl.MouseRotationEvent;
import de.vincentschweiger.phantomclient.events.impl.MouseScrollEvent;
import net.minecraft.client.Mouse;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MixinMouse {

    /**
     * Hook mouse button event
     */
    @Inject(method = "onMouseButton", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getOverlay()Lnet/minecraft/client/gui/screen/Overlay;", shift = At.Shift.BEFORE))
    private void hookMouseButton(long window, int button, int action, int mods, CallbackInfo callbackInfo) {
        new MouseButtonEvent(window, button, action, mods).call();
    }

    /**
     * Hook mouse scroll event
     */
    @Inject(method = "onMouseScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getOverlay()Lnet/minecraft/client/gui/screen/Overlay;", shift = At.Shift.BEFORE))
    private void hookMouseScroll(long window, double horizontal, double vertical, CallbackInfo callbackInfo) {
        new MouseScrollEvent(window, horizontal, vertical).call();
    }

    /**
     * Hook mouse cursor event
     */
    @Inject(method = "onCursorPos", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getOverlay()Lnet/minecraft/client/gui/screen/Overlay;", shift = At.Shift.BEFORE))
    private void hookCursorPos(long window, double x, double y, CallbackInfo callbackInfo) {
        new MouseCursorEvent(window, x, y).call();
    }

    /**
     * Hook mouse cursor event
     */
    @Redirect(method = "updateMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;changeLookDirection(DD)V"), require = 1, allow = 1)
    private void hookUpdateMouse(ClientPlayerEntity entity, double cursorDeltaX, double cursorDeltaY) {
        final MouseRotationEvent event = new MouseRotationEvent(cursorDeltaX, cursorDeltaY);
        event.call();
        if (event.isCancelled())
            return;
        entity.changeLookDirection(event.getCursorDeltaX(), event.getCursorDeltaY());
    }


}
