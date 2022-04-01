package de.vincentschweiger.phantomclient.cosmetics.hat;

import de.vincentschweiger.phantomclient.cosmetics.CosmeticUtils;
import de.vincentschweiger.phantomclient.server.ServerInfoProvider;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class HatRenderer<T extends LivingEntity, M extends PlayerEntityModel<T>> extends FeatureRenderer<T, M> {

    private HatModel<T> model;
    public static EntityModelLayer LAYER = new EntityModelLayer(new Identifier("phantom"), "hat");

    public static Identifier texture = new Identifier("phantom", "textures/hat.png");

    public HatRenderer(FeatureRendererContext<T, M> context, EntityModelLoader loader) {
        super(context);
        this.model = new HatModel<>(loader.getModelPart(LAYER));
    }

    @Override
    public void render(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn, T entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float headYaw, float headPitch) {
        if (entityLivingBaseIn instanceof ClientPlayerEntity player) {
                if (CosmeticUtils.checkClientUser(player) && ServerInfoProvider.hasCosmetic(player.getGameProfile().getId(), "hat")) {
                matrixStackIn.push();
                VertexConsumer builder = bufferIn.getBuffer(RenderLayer.getEntityCutoutNoCull(texture));
                if (!player.getEquippedStack(EquipmentSlot.HEAD).isEmpty()) {
                    matrixStackIn.translate(0.0D, -0.07D, 0.0D);
                    this.model.part.pivotY = 26.0F;
                }
                this.model.part.copyTransform(this.getContextModel().getHead());
                this.model.render(matrixStackIn, player, builder, packedLightIn, OverlayTexture.getUv(0.0f, false));
                matrixStackIn.pop();
            }
        }
    }
}