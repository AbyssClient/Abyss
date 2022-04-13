package me.cookie.abyssclient.mixins.client;

import me.cookie.abyssclient.event.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {

    /**
     * Hook input handling
     */
    @Inject(method = "handleInputEvents", at = @At("RETURN"))
    private void hookHandleInputEvent(CallbackInfo callbackInfo) {
        EventManager.INSTANCE.callEvent(new InputHandleEvent());
    }

    /**
     * Hook game tick event at HEAD
     */
    @Inject(method = "tick", at = @At("HEAD"))
    private void hookTickEvent(CallbackInfo callbackInfo) {
        EventManager.INSTANCE.callEvent(new GameTickEvent());
    }


    /**
     * Entry point of our client
     *
     * @param callback not needed
     */
    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;onResolutionChanged()V"))
    private void startClient(CallbackInfo callback) {
        EventManager.INSTANCE.callEvent(new ClientStartEvent());
    }

    /**
     * Exit point of our client
     *
     * @param callback not needed
     */
    @Inject(method = "stop", at = @At("HEAD"))
    private void stopClient(CallbackInfo callback) {
        EventManager.INSTANCE.callEvent(new ClientShutdownEvent());
    }

    /**
     * Handle opening screens
     *
     * @param screen       to be opened (null = no screen at all)
     * @param callbackInfo callback
     */
    @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
    private void hookScreen(Screen screen, CallbackInfo callbackInfo) {
        final ScreenEvent event = new ScreenEvent(screen);
        EventManager.INSTANCE.callEvent(event);
        if (event.isCancelled())
            callbackInfo.cancel();
    }

    /**
     * Hook world joining
     *
     * @param world        joined world
     * @param callbackInfo not needed
     */
    @Inject(method = "joinWorld", at = @At("RETURN"))
    private void onJoinWorld(ClientWorld world, CallbackInfo callbackInfo) {
        EventManager.INSTANCE.callEvent(new WorldJoinEvent(world));
    }
}