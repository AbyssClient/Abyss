package de.vincentschweiger.phantomclient.cosmetics.cape;

import de.vincentschweiger.phantomclient.cosmetics.CosmeticUtils;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

public class CapeRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    private float capeRotateX;
    private float capeRotateY;
    private float capeRotateZ;
    public static boolean useAsElytra = false;

    private Identifier texture = new Identifier("phantom", "textures/cape.png");

    public CapeRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureRendererContext) {
        super(featureRendererContext);
    }

    public void render(MatrixStack pMatrixStack, VertexConsumerProvider pBuffer, int pPackedLight, AbstractClientPlayerEntity pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
            if (!CosmeticUtils.checkClientUser(pLivingEntity)) return;
            pMatrixStack.push();
            pMatrixStack.translate(0.0D, 0.0D, 0.125D);
            double d0 = MathHelper.lerp(pPartialTicks, pLivingEntity.prevCapeX, pLivingEntity.capeX) - MathHelper.lerp(pPartialTicks, pLivingEntity.prevX, pLivingEntity.getX());
            double d1 = MathHelper.lerp(pPartialTicks, pLivingEntity.prevCapeY, pLivingEntity.capeY) - MathHelper.lerp(pPartialTicks, pLivingEntity.prevY, pLivingEntity.getY());
            double d2 = MathHelper.lerp(pPartialTicks, pLivingEntity.prevCapeZ, pLivingEntity.capeZ) - MathHelper.lerp(pPartialTicks, pLivingEntity.prevZ, pLivingEntity.getZ());
            float f = pLivingEntity.prevBodyYaw + (pLivingEntity.bodyYaw - pLivingEntity.prevBodyYaw);
            double d3 = MathHelper.sin(f * ((float) Math.PI / 180F));
            double d4 = -MathHelper.cos(f * ((float) Math.PI / 180F));
            float f1 = (float) d1 * 10.0F;
            f1 = MathHelper.clamp(f1, -6.0F, 32.0F);
            float f2 = (float) (d0 * d3 + d2 * d4) * 100.0F;
            f2 = MathHelper.clamp(f2, 0.0F, 150.0F);
            float f3 = (float) (d0 * d4 - d2 * d3) * 100.0F;
            f3 = MathHelper.clamp(f3, -20.0F, 20.0F);

            if (f2 < 0.0F) {
                f2 = 0.0F;
            }

            if (f2 > 165.0F) {
                f2 = 165.0F;
            }

            if (f1 < -5.0F) {
                f1 = -5.0F;
            }

            float f4 = MathHelper.lerp(pPartialTicks, pLivingEntity.prevStrideDistance, pLivingEntity.strideDistance);
            f1 += MathHelper.sin(MathHelper.lerp(pPartialTicks, pLivingEntity.prevHorizontalSpeed, pLivingEntity.horizontalSpeed) * 6.0F) * 32.0F * f4;

            if (pLivingEntity.isSneaking()) {
                f1 += 25.0F;
            }

            float f5 = StutterFix.getAverageFrameTimeSec() * 20.0F;
            f5 = StutterFix.limit(f5, 0.02F, 1.0F);
            this.capeRotateX = MathHelper.lerp(f5, capeRotateX, 6.0F + f2 / 2.0F + f1);
            this.capeRotateZ = MathHelper.lerp(f5, capeRotateZ, f3 / 2.0F);
            this.capeRotateY = MathHelper.lerp(f5, capeRotateY, 180.0F - f3 / 2.0F);
            pMatrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(capeRotateX));
            pMatrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(capeRotateZ));
            pMatrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(capeRotateY));
            VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderLayer.getEntitySolid(texture));
            boolean elytraEquipped = pLivingEntity.getEquippedStack(EquipmentSlot.CHEST).isOf(Items.ELYTRA);
            if (CapeRenderer.useAsElytra) {
                if (elytraEquipped)
                    this.getContextModel().renderCape(pMatrixStack, vertexconsumer, pPackedLight, OverlayTexture.DEFAULT_UV);
            } else {
                if (!elytraEquipped)
                    this.getContextModel().renderCape(pMatrixStack, vertexconsumer, pPackedLight, OverlayTexture.DEFAULT_UV);
            }
            pMatrixStack.pop();
        }
    }
