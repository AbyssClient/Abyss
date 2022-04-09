package me.cookie.abyssclient.mixins.client;

import me.cookie.abyssclient.event.EventManager;
import me.cookie.abyssclient.event.WindowFocusEvent;
import me.cookie.abyssclient.event.WindowResizeEvent;
import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Window.class)
public class MixinWindow {

    @Shadow
    @Final
    private long handle;

    /**
     * Hook window resize
     */
    @Inject(method = "onWindowSizeChanged", at = @At("HEAD"))
    public void hookResize(long window, int width, int height, CallbackInfo callbackInfo) {
        if (window == handle) {
            EventManager.INSTANCE.callEvent(new WindowResizeEvent(window, width, height));
        }
    }

    /**
     * Hook window resize
     */
    @Inject(method = "onWindowFocusChanged", at = @At(value = "FIELD", target = "Lnet/minecraft/client/util/Window;eventHandler:Lnet/minecraft/client/WindowEventHandler;"))
    public void hookFocus(long window, boolean focused, CallbackInfo callbackInfo) {
        EventManager.INSTANCE.callEvent(new WindowFocusEvent(window, focused));
    }
}
