package de.vincentschweiger.phantomclient.cosmetics.dragonwings;

import de.vincentschweiger.phantomclient.cosmetics.CosmeticUtils;
import de.vincentschweiger.phantomclient.mixins.InvokerEntityModelLayers;
import de.vincentschweiger.phantomclient.server.ServerInfoProvider;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class DragonwingsRenderer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {

    private DragonwingsModel<T> model;
    public static EntityModelLayer LAYER = InvokerEntityModelLayers.invokeRegisterMain("phantom_dragonwings");
    public static double anglePitch = -65.0D;
    public static double angleYaw = 45.0D;
    public static double angleRoll = 20.0D;
    public static boolean useAsElytra = false;
    public static double scale = 70.0D;
    public static Identifier texture = new Identifier("phantom", "textures/dragonwings.png");

    public DragonwingsRenderer(FeatureRendererContext<T, M> context, EntityModelLoader loader) {
        super(context);
        this.model = new DragonwingsModel<>(loader.getModelPart(LAYER));
    }

    @Override
    public void render(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn, T entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float headYaw, float headPitch) {
        if (entityLivingBaseIn instanceof ClientPlayerEntity
                && CosmeticUtils.checkClientUser((ClientPlayerEntity) entityLivingBaseIn)
                && ServerInfoProvider.hasCosmetic(
                        ((ClientPlayerEntity) entityLivingBaseIn).getGameProfile().getId(),
                "wings"
        )) {
            boolean elytraEquipped = entityLivingBaseIn.getEquippedStack(EquipmentSlot.CHEST).isOf(Items.ELYTRA);
            if (DragonwingsRenderer.useAsElytra) {
                if (elytraEquipped) {
                    if (entityLivingBaseIn.getUuid().equals(MinecraftClient.getInstance().getSession().getProfile().getId())) {
                        ClientPlayerEntity player = (ClientPlayerEntity) entityLivingBaseIn;
                        matrixStackIn.push();
                        VertexConsumer builder = bufferIn.getBuffer(RenderLayer.getArmorCutoutNoCull(texture));
                        this.model.render(player, matrixStackIn, builder, packedLightIn, OverlayTexture.DEFAULT_UV, partialTicks);
                        matrixStackIn.pop();
                    }
                }
            } else {
                if (!elytraEquipped) {
                    if (entityLivingBaseIn.getUuid().equals(MinecraftClient.getInstance().getSession().getProfile().getId())) {
                        ClientPlayerEntity p = (ClientPlayerEntity) entityLivingBaseIn;
                        matrixStackIn.push();
                        VertexConsumer builder = bufferIn.getBuffer(RenderLayer.getArmorCutoutNoCull(texture));
                        this.model.render(p, matrixStackIn, builder, packedLightIn, OverlayTexture.DEFAULT_UV, partialTicks);
                        matrixStackIn.pop();
                    }
                }
            }
        }
    }
}