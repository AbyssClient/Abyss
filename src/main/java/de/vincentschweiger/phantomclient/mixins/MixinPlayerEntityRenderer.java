package de.vincentschweiger.phantomclient.mixins;

import de.vincentschweiger.phantomclient.cosmetics.cape.CapeRenderer;
import de.vincentschweiger.phantomclient.cosmetics.dragonwings.DragonwingsRenderer;
import de.vincentschweiger.phantomclient.cosmetics.hat.HatRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class MixinPlayerEntityRenderer extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    private MixinPlayerEntityRenderer(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    public void inject(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo ci) {
        //this.addFeature(new CapeRenderer(this));
        //this.addFeature(new DragonwingsRenderer<>(this, ctx.getModelLoader()));
        //this.addFeature(new HatRenderer<>(this, ctx.getModelLoader()));
    }
}