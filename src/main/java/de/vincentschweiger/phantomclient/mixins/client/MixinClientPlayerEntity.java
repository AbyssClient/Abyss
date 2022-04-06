package de.vincentschweiger.phantomclient.mixins.client;

import de.vincentschweiger.phantomclient.event.EventManager;
import de.vincentschweiger.phantomclient.event.PlayerMovementTickEvent;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class MixinClientPlayerEntity {
    /**
     * Hook entity movement tick event
     */
    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void hookMovementTickEvent(CallbackInfo callbackInfo) {
        EventManager.INSTANCE.callEvent(new PlayerMovementTickEvent());
    }
}
