package de.vincentschweiger.phantomclient.mixins;

import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntityModelLayers.class)
public interface InvokerEntityModelLayers {
    @Invoker("registerMain")
    public static EntityModelLayer invokeRegisterMain(String id) {
        throw new AssertionError();
    }
}