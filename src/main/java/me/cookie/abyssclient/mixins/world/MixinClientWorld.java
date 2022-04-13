package me.cookie.abyssclient.mixins.world;

import me.cookie.abyssclient.event.EventManager;
import me.cookie.abyssclient.event.WorldDisconnectEvent;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public class MixinClientWorld {
    /**
     * Hook world leaving
     *
     * @param callbackInfo not used
     */
    @Inject(method = "disconnect", at = @At("RETURN"))
    private void injectWorldCloseEvent(CallbackInfo callbackInfo) {
        EventManager.INSTANCE.callEvent(new WorldDisconnectEvent());
    }
}
