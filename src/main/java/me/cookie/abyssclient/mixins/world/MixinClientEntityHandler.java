package me.cookie.abyssclient.mixins.world;

import me.cookie.abyssclient.event.EventManager;
import me.cookie.abyssclient.event.PlayerJoinWorldEvent;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.world.entity.EntityHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.ClientEntityHandler.class)
public abstract class MixinClientEntityHandler implements EntityHandler<Entity> {
    @Inject(at = @At("RETURN"), method = "startTracking(Lnet/minecraft/entity/Entity;)V")
    public void onStartTracking(Entity entity, CallbackInfo callbackInfo) {
        if (entity instanceof AbstractClientPlayerEntity) {
            EventManager.INSTANCE.callEvent(new PlayerJoinWorldEvent((AbstractClientPlayerEntity) entity));
        }
    }
}
