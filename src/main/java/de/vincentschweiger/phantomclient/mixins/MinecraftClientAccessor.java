package de.vincentschweiger.phantomclient.mixins;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Session;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Sa. 18/12/2021 17:33
 *
 * @author cook1e
 */
@Mixin(MinecraftClient.class)
public interface MinecraftClientAccessor {
    @Accessor("session")
    @Mutable
    @Final
    public void setSession(Session session);

    @Accessor("currentFps")
    int getCurrentFps();
}